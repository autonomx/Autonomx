import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dependency-free local web application used by the Autonomx functional/UI tests.
 *
 * It intentionally emulates only the observable contract the tests depended on
 * from the old hosted Strapi admin application: admin login, user CRUD APIs,
 * and the legacy DOM locators used by the page objects.
 */
public final class FrameworkTestWebApp {
    private static final String HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 18080;
    private static final String ADMIN_EMAIL = "autouser313@gmail.com";
    private static final String ADMIN_USERNAME = "autoAdmin1";
    private static final String ADMIN_PASSWORD = "autoPass1";
    private static final String ADMIN_TOKEN = "autonomx-local-test-token";
    private static final String SESSION_COOKIE = "AUTONOMX_TEST_SESSION=admin";
    private static final String USER_API = "/content-manager/collection-types/plugins::users-permissions.user";

    private static final AtomicInteger NEXT_USER_ID = new AtomicInteger(300);
    private static final Map<Integer, TestUser> USERS = new ConcurrentHashMap<Integer, TestUser>();

    private FrameworkTestWebApp() {
    }

    public static void main(String[] args) throws Exception {
        int port = DEFAULT_PORT;
        if (args.length > 0 && args[0] != null && !args[0].trim().isEmpty()) {
            port = Integer.parseInt(args[0].trim());
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(HOST, port), 0);
        server.createContext("/", new AppHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("Autonomx framework test webapp started at http://" + HOST + ":" + port);
        System.out.println("Admin UI: http://" + HOST + ":" + port + "/admin");
        System.out.println("Health:   http://" + HOST + ":" + port + "/health");
    }

    private static final class AppHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                addCommonHeaders(exchange);
                if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                    send(exchange, 204, "text/plain; charset=utf-8", "");
                    return;
                }

                String path = normalizePath(exchange.getRequestURI().getPath());
                String method = exchange.getRequestMethod().toUpperCase();

                if ("/health".equals(path)) {
                    sendJson(exchange, 200, "{\"status\":\"ok\",\"service\":\"autonomx-framework-test-webapp\"}");
                    return;
                }

                if ("/test/timeout".equals(path)) {
                    handleTimeout(exchange);
                    return;
                }

                if (USER_API.equals(path) || path.startsWith(USER_API + "/")) {
                    handleUserApi(exchange, method, path);
                    return;
                }

                if ("/admin/login".equals(path) && "POST".equals(method)) {
                    String contentType = header(exchange, "Content-Type").toLowerCase();
                    if (contentType.contains("application/json")) {
                        handleApiLogin(exchange);
                    } else {
                        handleUiLogin(exchange);
                    }
                    return;
                }

                if ("/admin/login".equals(path) || "/admin/auth/login".equals(path)) {
                    if ("GET".equals(method) || "HEAD".equals(method)) {
                        renderLogin(exchange, null);
                    } else {
                        methodNotAllowed(exchange);
                    }
                    return;
                }

                if ("/admin/logout".equals(path)) {
                    Headers headers = exchange.getResponseHeaders();
                    headers.add("Set-Cookie", "AUTONOMX_TEST_SESSION=; Path=/; Max-Age=0; SameSite=Lax");
                    redirect(exchange, "/admin");
                    return;
                }

                if ("/admin".equals(path) || "/admin/".equals(path)) {
                    if (isUiAuthenticated(exchange)) {
                        redirect(exchange, "/admin/dashboard");
                    } else {
                        renderLogin(exchange, null);
                    }
                    return;
                }

                if (path.startsWith("/admin")) {
                    handleAdminUi(exchange, method, path);
                    return;
                }

                if ("/".equals(path)) {
                    send(exchange, 200, "text/plain; charset=utf-8", "Autonomx local framework test webapp\n");
                    return;
                }

                sendJson(exchange, 404, "{\"statusCode\":404,\"error\":\"Not Found\",\"message\":\"Not Found\"}");
            } catch (Exception e) {
                e.printStackTrace();
                sendJson(exchange, 500, "{\"statusCode\":500,\"error\":\"Internal Server Error\",\"message\":\""
                        + jsonEscape(e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage()) + "\"}");
            } finally {
                exchange.close();
            }
        }
    }

    private static void handleApiLogin(HttpExchange exchange) throws IOException {
        String body = readBody(exchange);
        String email = jsonString(body, "email");
        String password = jsonString(body, "password");
        if (isAdminCredential(email, password)) {
            String userJson = "{"
                    + "\"id\":1,\"firstname\":\"auto \",\"lastname\":\"user\","
                    + "\"username\":\"" + ADMIN_USERNAME + "\",\"email\":\"" + ADMIN_EMAIL + "\","
                    + "\"registrationToken\":null,\"isActive\":true,\"blocked\":null,"
                    + "\"roles\":[{\"id\":1,\"name\":\"Super Admin\",\"code\":\"strapi-super-admin\","
                    + "\"description\":\"Super Admins can access and manage all features and settings.\"}]}";
            // Keep one canonical token/user copy. The historical JSONPath expressions
            // use recursive lookups (for example .token and .user.roles..id), so a
            // duplicate root + data payload turns extracted values into comma lists.
            String json = "{\"data\":{\"token\":\"" + ADMIN_TOKEN + "\",\"user\":" + userJson + "}}";
            sendJson(exchange, 200, json);
        } else {
            sendJson(exchange, 400,
                    "{\"data\":null,\"error\":{\"status\":400,\"name\":\"ValidationError\",\"message\":\"Invalid credentials\"}}");
        }
    }

    private static void handleTimeout(HttpExchange exchange) throws IOException {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        sendJson(exchange, 200, "{\"status\":\"delayed\"}");
    }

    private static void handleUserApi(HttpExchange exchange, String method, String path) throws IOException {
        int authStatus = apiAuthStatus(exchange);
        if (authStatus != 200) {
            if (authStatus == 403) {
                sendJson(exchange, 403, "{\"statusCode\":403,\"error\":\"Forbidden\",\"message\":\"Forbidden\"}");
            } else {
                sendJson(exchange, 401, "{\"statusCode\":401,\"error\":\"Unauthorized\"}");
            }
            return;
        }

        Integer id = extractTrailingId(path, USER_API);
        if (id == null) {
            if ("GET".equals(method)) {
                List<TestUser> users = new ArrayList<TestUser>(USERS.values());
                Collections.sort(users, new Comparator<TestUser>() {
                    @Override
                    public int compare(TestUser left, TestUser right) {
                        return Integer.compare(left.id, right.id);
                    }
                });
                StringBuilder json = new StringBuilder();
                json.append("{\"results\":[");
                for (int i = 0; i < users.size(); i++) {
                    if (i > 0) {
                        json.append(',');
                    }
                    json.append(users.get(i).toJson());
                }
                json.append("],\"pagination\":{\"page\":1,\"pageSize\":100,\"pageCount\":1,\"total\":")
                        .append(users.size()).append("}}");
                sendJson(exchange, 200, json.toString());
                return;
            }
            if ("POST".equals(method)) {
                String body = readBody(exchange);
                TestUser user = userFromJson(body, NEXT_USER_ID.incrementAndGet());
                USERS.put(user.id, user);
                sendJson(exchange, 201, user.toJson());
                return;
            }
            methodNotAllowed(exchange);
            return;
        }

        TestUser existing = USERS.get(id);
        if (existing == null) {
            sendJson(exchange, 404, "{\"statusCode\":404,\"error\":\"Not Found\",\"message\":\"User not found\"}");
            return;
        }

        if ("PUT".equals(method) || "PATCH".equals(method)) {
            String body = readBody(exchange);
            updateUserFromJson(existing, body);
            sendJson(exchange, 200, existing.toJson());
            return;
        }
        if ("DELETE".equals(method)) {
            USERS.remove(id);
            sendJson(exchange, 200, existing.toJson());
            return;
        }
        if ("GET".equals(method)) {
            sendJson(exchange, 200, existing.toJson());
            return;
        }
        methodNotAllowed(exchange);
    }

    private static void handleAdminUi(HttpExchange exchange, String method, String path) throws IOException {
        if (!isUiAuthenticated(exchange)) {
            redirect(exchange, "/admin");
            return;
        }

        if ("/admin/dashboard".equals(path)) {
            renderDashboard(exchange);
            return;
        }
        if ("/admin/forgot-password".equals(path)) {
            sendHtml(exchange, 200, page("Forgot password", shell("<h1>Forgot password</h1><a href=\"/admin\">Back to login</a>")));
            return;
        }
        if ("/admin/users".equals(path)) {
            if (!"GET".equals(method) && !"HEAD".equals(method)) {
                methodNotAllowed(exchange);
                return;
            }
            renderUsers(exchange);
            return;
        }
        if ("/admin/users/new".equals(path)) {
            renderUserForm(exchange, null, false);
            return;
        }
        if ("/admin/users/create".equals(path) && "POST".equals(method)) {
            Map<String, String> form = parseForm(readBody(exchange));
            TestUser user = userFromForm(form, NEXT_USER_ID.incrementAndGet());
            USERS.put(user.id, user);
            redirect(exchange, "/admin/dashboard");
            return;
        }

        Matcher editMatcher = Pattern.compile("^/admin/users/(\\d+)/edit$").matcher(path);
        if (editMatcher.matches()) {
            int id = Integer.parseInt(editMatcher.group(1));
            TestUser user = USERS.get(id);
            if (user == null) {
                sendHtml(exchange, 404, page("User not found", shell("<h1>User not found</h1>")));
                return;
            }
            if ("POST".equals(method)) {
                updateUserFromForm(user, parseForm(readBody(exchange)));
                renderUserForm(exchange, user, true);
            } else if ("GET".equals(method) || "HEAD".equals(method)) {
                renderUserForm(exchange, user, false);
            } else {
                methodNotAllowed(exchange);
            }
            return;
        }

        // Compatibility destinations used only as side-panel href locators.
        if (path.contains("ctm-configurations") || path.contains("content-type-builder") || path.contains("upload")
                || path.contains("plugins/users-permissions")) {
            renderDashboard(exchange);
            return;
        }

        sendHtml(exchange, 404, page("Not found", shell("<h1>Not found</h1>")));
    }

    private static void handleUiLogin(HttpExchange exchange) throws IOException {
        Map<String, String> form = parseForm(readBody(exchange));
        String email = form.get("email");
        String password = form.get("password");
        if (isAdminCredential(email, password)) {
            exchange.getResponseHeaders().add("Set-Cookie", SESSION_COOKIE + "; Path=/; HttpOnly; SameSite=Lax");
            redirect(exchange, "/admin/dashboard");
        } else {
            renderLogin(exchange, "Invalid email or password");
        }
    }

    private static void renderLogin(HttpExchange exchange, String error) throws IOException {
        StringBuilder body = new StringBuilder();
        body.append("<main class=\"login login-page\">")
                .append("<div class=\"login-card\">")
                .append("<div class=\"brand-lockup\"><span class=\"brand-mark\">A</span><span>Autonomx</span></div>")
                .append("<p class=\"eyebrow\">LOCAL ADMIN CONSOLE</p>")
                .append("<h1>Welcome back</h1>")
                .append("<p class=\"muted login-intro\">Sign in to manage your local test environment.</p>")
                .append("<form class=\"login-form\" method=\"post\" action=\"/admin/login\">")
                .append("<div class=\"field-group\"><label for=\"email\">Email address</label>")
                .append("<input id=\"email\" name=\"email\" type=\"email\" autocomplete=\"username\" placeholder=\"you@example.com\"></div>")
                .append("<div class=\"field-group\"><label for=\"password\">Password</label>")
                .append("<input id=\"password\" name=\"password\" type=\"password\" autocomplete=\"current-password\" placeholder=\"Enter your password\"></div>")
                .append("<button class=\"button button-primary button-full\" type=\"submit\">Sign in <span aria-hidden=\"true\">→</span></button>")
                .append("</form>")
                .append("<div class=\"login-links\"><a href=\"/admin/forgot-password\">Forgot password?</a><a class=\"main-site\" href=\"/\">Main site</a></div>")
                .append("<div class=\"Loading\" style=\"display:none\">Loading</div>");
        if (error != null) {
            body.append("<div id=\"error-email\" class=\"permissionserrorsContainer alert alert-error\">")
                    .append(htmlEscape(error)).append("</div>");
        }
        body.append("</div><p class=\"login-footer\">A lightweight local environment for reliable automation.</p></main>");
        sendHtml(exchange, 200, page("Login", body.toString()));
    }

    private static void renderDashboard(HttpExchange exchange) throws IOException {
        String content = "<section class=\"page-section dashboard-page\">"
                + "<div class=\"page-heading\"><div><p class=\"eyebrow\">OVERVIEW</p><h1>Dashboard</h1>"
                + "<p class=\"muted\">Your local framework test application is ready.</p></div>"
                + "<span class=\"status-pill status-success\"><span class=\"status-dot\"></span>System online</span></div>"
                + "<div class=\"stat-grid\">"
                + "<article class=\"stat-card\"><span class=\"stat-icon icon-blue\">⌁</span><div><p class=\"stat-label\">Environment</p><strong>Local</strong><p class=\"stat-caption\">127.0.0.1:18080</p></div></article>"
                + "<article class=\"stat-card\"><span class=\"stat-icon icon-purple\">◌</span><div><p class=\"stat-label\">Service</p><strong>Framework API</strong><p class=\"stat-caption\">Ready for testing</p></div></article>"
                + "<article class=\"stat-card\"><span class=\"stat-icon icon-green\">✓</span><div><p class=\"stat-label\">Health</p><strong>Healthy</strong><p class=\"stat-caption\">Last checked just now</p></div></article>"
                + "</div>"
                + "<div class=\"content-card welcome-card\"><div><p class=\"eyebrow\">QUICK START</p><h2>Build with confidence</h2>"
                + "<p class=\"muted\">Use the navigation to manage test users and exercise the same flows as the functional suite.</p></div>"
                + "<a class=\"button button-primary\" href=\"/admin/users\">View users <span aria-hidden=\"true\">→</span></a></div>"
                + "</section>";
        sendHtml(exchange, 200, page("Dashboard", shell(content)));
    }

    private static void renderUsers(HttpExchange exchange) throws IOException {
        Map<String, String> query = parseQuery(exchange.getRequestURI());
        String filterName = firstNonEmpty(query.get("filters.0.name"), query.get("filter.name"), "username");
        String filterValue = firstNonEmpty(query.get("filters.0.value"), query.get("filter.value"), "");

        List<TestUser> users = new ArrayList<TestUser>(USERS.values());
        Collections.sort(users, new Comparator<TestUser>() {
            @Override
            public int compare(TestUser left, TestUser right) {
                return Integer.compare(left.id, right.id);
            }
        });

        StringBuilder body = new StringBuilder();
        body.append("<section class=\"page-section users-page\">")
                .append("<div class=\"page-heading\"><div><p class=\"eyebrow\">DIRECTORY</p><h1>Users</h1>")
                .append("<p class=\"muted\">Manage accounts and permissions for your test environment.</p></div>")
                .append("<div class=\"page-actions\">")
                .append("<button class=\"button button-secondary\" type=\"button\" onclick=\"document.getElementById('filter-panel').style.display='block'\"><span aria-hidden=\"true\">≡</span> Filters</button>")
                .append("<button class=\"button button-primary\" color=\"Primary\" type=\"button\" onclick=\"window.location='/admin/users/new'\"><span aria-hidden=\"true\">+</span> Add new user</button>")
                .append("</div></div>")
                .append("<div id=\"filter-panel\" class=\"filter-panel\" style=\"").append(filterValue.isEmpty() ? "display:none" : "display:block").append("\">")
                .append("<form class=\"filter-form\" method=\"get\" action=\"/admin/users\">")
                .append("<div class=\"field-group\"><label for=\"filters.0.name\">Filter by</label><select id=\"filters.0.name\" name=\"filters.0.name\">")
                .append("<option value=\"username\"").append("username".equalsIgnoreCase(filterName) ? " selected" : "").append(">username</option>")
                .append("<option value=\"email\"").append("email".equalsIgnoreCase(filterName) ? " selected" : "").append(">email</option></select></div>")
                .append("<div class=\"field-group filter-value\"><label for=\"filters.0.value\">Contains</label><input id=\"filters.0.value\" name=\"filters.0.value\" value=\"").append(htmlEscape(filterValue)).append("\" placeholder=\"Search users\"></div>")
                .append("<button class=\"button button-primary\" type=\"submit\">Apply filter</button>")
                .append("</form></div>");
        if (!filterValue.isEmpty()) {
            body.append("<a class=\"remove__admin filter-chip\" href=\"/admin/users\"><span aria-hidden=\"true\">×</span> Remove filter</a>");
        } else {
            // Keep the legacy selector present without affecting the visible UI.
            body.append("<a class=\"remove__admin\" href=\"/admin/users\" style=\"display:none\">Remove filter</a>");
        }

        body.append("<div class=\"content-card table-card\"><div class=\"table-toolbar\"><div><h2>All users</h2><p class=\"muted\">Accounts created by the test suite appear here.</p></div><span class=\"record-count\">").append(users.size()).append(" records</span></div>")
                .append("<div class=\"table-scroll\"><table><thead><tr><th>ID</th><th>Username</th><th>Email</th><th>Confirmed</th><th>Blocked</th><th>Edit</th></tr></thead><tbody>");
        for (TestUser user : users) {
            if (!matchesFilter(user, filterName, filterValue)) {
                continue;
            }
            body.append("<tr class=\"TableRow\" onclick=\"window.location='/admin/users/").append(user.id).append("/edit'\">")
                    .append("<td>").append(user.id).append("</td>")
                    .append("<td><div class=\"user-cell\"><span class=\"avatar\">").append(htmlEscape(initial(user.username))).append("</span><span class=\"user-name\">").append(htmlEscape(user.username)).append("</span></div></td>")
                    .append("<td>").append(htmlEscape(user.email)).append("</td>")
                    .append("<td><span class=\"status-pill ").append(user.confirmed ? "status-success" : "status-muted").append("\">").append(user.confirmed ? "Confirmed" : "Pending").append("</span></td>")
                    .append("<td><span class=\"status-pill ").append(user.blocked ? "status-danger" : "status-success").append("\">").append(user.blocked ? "Blocked" : "Active").append("</span></td>")
                    .append("<td><a class=\"fa-pencil table-action\" href=\"/admin/users/").append(user.id).append("/edit\">Edit <span aria-hidden=\"true\">→</span></a></td></tr>");
        }
        body.append("</tbody></table></div></div></section>");
        sendHtml(exchange, 200, page("Users", shell(body.toString())));
    }

    private static void renderUserForm(HttpExchange exchange, TestUser user, boolean saved) throws IOException {
        boolean create = user == null;
        String username = create ? "" : user.username;
        String email = create ? "" : user.email;
        String action = create ? "/admin/users/create" : "/admin/users/" + user.id + "/edit";
        boolean confirmed = !create && user.confirmed;
        boolean blocked = !create && user.blocked;

        StringBuilder body = new StringBuilder();
        body.append("<section class=\"page-section form-page\">")
                .append("<div class=\"page-heading\"><div><p class=\"eyebrow\">DIRECTORY / USER</p><h1>")
                .append(create ? "Add user" : "Edit user").append("</h1>")
                .append("<p class=\"muted\">").append(create ? "Create an account for your local test environment." : "Update account details and access settings.").append("</p></div>")
                .append("<a class=\"button button-ghost\" href=\"/admin/users\">← Back to users</a></div>")
                .append("<div class=\"content-card form-card\">");
        if (saved) {
            body.append("<div title=\"Saved\" class=\"alert alert-success\"><span aria-hidden=\"true\">✓</span> Changes saved successfully.</div>");
        }
        body.append("<form class=\"user-form\" method=\"post\" action=\"").append(action).append("\">")
                .append("<div class=\"form-grid\">")
                .append("<div class=\"field-group\"><label for=\"username\">Username</label><input id=\"username\" name=\"username\" value=\"").append(htmlEscape(username)).append("\" placeholder=\"Enter a username\"></div>")
                .append("<div class=\"field-group\"><label for=\"email\">Email</label><input id=\"email\" name=\"email\" value=\"").append(htmlEscape(email)).append("\" placeholder=\"name@example.com\"></div>")
                .append("<div class=\"field-group\"><label for=\"password\">Password</label><input id=\"password\" name=\"password\" type=\"password\" placeholder=\"").append(create ? "Create a password" : "Leave blank to keep current").append("\"></div>")
                .append("<div class=\"field-group\"><label for=\"role\">Role</label><select id=\"role\" class=\"Select-control\" name=\"role\"><option>Authenticated</option></select></div>")
                .append("</div><div class=\"form-divider\"></div><p class=\"form-section-title\">Account status</p><div class=\"checkbox-grid\">")
                .append("<label class=\"checkbox-label\" for=\"confirmed\"><input id=\"confirmed\" name=\"confirmed\" type=\"checkbox\" value=\"true\"")
                .append(confirmed ? " checked" : "").append("><span><strong>Confirmed</strong><small>Allow the user to sign in.</small></span></label>")
                .append("<label class=\"checkbox-label\" for=\"blocked\"><input id=\"blocked\" name=\"blocked\" type=\"checkbox\" value=\"true\"")
                .append(blocked ? " checked" : "").append("><span><strong>Blocked</strong><small>Prevent access to this account.</small></span></label>")
                .append("</div><div class=\"form-actions\"><a class=\"button button-ghost\" href=\"/admin/users\">Cancel</a><button class=\"button button-primary\" type=\"submit\">Save changes <span aria-hidden=\"true\">→</span></button></div></form></div></section>");
        sendHtml(exchange, 200, page(create ? "Add user" : "Edit user", shell(body.toString())));
    }

    private static String shell(String content) {
        return "<div class=\"app-shell\">"
                + "<header class=\"topbar\"><div class=\"topbar-brand\"><a class=\"projectName\" href=\"/admin/dashboard\"><span class=\"brand-mark\">A</span><span>Autonomx</span></a></div>"
                + "<div class=\"topbar-actions\"><span class=\"environment-badge\">LOCAL</span><div class=\"account-wrap\"><button class=\"fa-caret-down account-button\" type=\"button\" onclick=\"var m=document.getElementById('user-menu');m.style.display=(m.style.display==='block'?'none':'block')\"><span class=\"account-avatar\">A</span><span>Account</span><span class=\"chevron\">⌄</span></button>"
                + "<div id=\"user-menu\" class=\"account-menu\" style=\"display:none\"><a role=\"menuitem\" href=\"/admin/logout\"><span class=\"fa-sign-out\" aria-hidden=\"true\"></span>Logout</a></div></div></div></header>"
                + "<aside class=\"sidebar\"><p class=\"nav-label\">WORKSPACE</p><nav aria-label=\"Workspace navigation\">"
                + "<a class=\"nav-link fa-circle\" href=\"/admin/users\"><span class=\"nav-icon\">♙</span><span>Users</span></a>"
                + "<a class=\"nav-link\" href=\"/admin/ctm-configurations\"><span class=\"nav-icon\">▦</span><span>Content Manager</span></a>"
                + "<a class=\"nav-link\" href=\"/admin/content-type-builder\"><span class=\"nav-icon\">◇</span><span>Content Type Builder</span></a>"
                + "<a class=\"nav-link\" href=\"/admin/upload\"><span class=\"nav-icon\">↑</span><span>Upload</span></a>"
                + "<a class=\"nav-link\" href=\"/admin/plugins/users-permissions\"><span class=\"nav-icon\">⚿</span><span>Roles &amp; Permissions</span></a>"
                + "</nav><div class=\"sidebar-footer\"><span class=\"status-dot\"></span><span>Test environment<br><small>Ready for automation</small></span></div></aside>"
                + "<main class=\"workspace\"><div class=\"workspace-inner\">" + content + "</div></main></div>";
    }

    private static String page(String title, String body) {
        String styles = "*{box-sizing:border-box}"
                + ":root{--ink:#172033;--muted:#6d7890;--line:#e7eaf0;--surface:#fff;--canvas:#f6f8fc;--navy:#101827;--navy-soft:#1b263a;--blue:#4263eb;--blue-dark:#3151d4;--green:#16845b;--green-bg:#e7f7ef;--red:#c2414b;--red-bg:#fff0f1;--shadow:0 12px 32px rgba(31,45,75,.08)}"
                + "html,body{min-height:100%}body{margin:0;background:var(--canvas);color:var(--ink);font-family:Inter,-apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica,Arial,sans-serif;font-size:14px;line-height:1.5}"
                + "a{color:var(--blue);text-decoration:none}a:hover{color:var(--blue-dark)}button,input,select{font:inherit}button{cursor:pointer}h1,h2,p{margin-top:0}h1{font-size:30px;letter-spacing:-.7px;line-height:1.15;margin-bottom:8px}h2{font-size:17px;line-height:1.3;margin-bottom:6px}.muted{color:var(--muted)}.eyebrow{color:#7c88a1;font-size:11px;font-weight:750;letter-spacing:1.3px;margin-bottom:10px}.status-dot{background:#35bb7c;border-radius:50%;display:inline-block;height:8px;width:8px}"
                + ".app-shell{display:grid;grid-template-areas:'sidebar topbar' 'sidebar workspace';grid-template-columns:248px minmax(0,1fr);grid-template-rows:72px minmax(calc(100vh - 72px),1fr);min-height:100vh}.topbar{align-items:center;background:var(--surface);border-bottom:1px solid var(--line);display:flex;grid-area:topbar;justify-content:space-between;padding:0 36px;position:relative;z-index:5}.projectName{align-items:center;color:var(--ink);display:flex;font-size:18px;font-weight:750;gap:10px;letter-spacing:-.2px}.projectName:hover{color:var(--ink)}.brand-mark{align-items:center;background:linear-gradient(135deg,#627cf3,#3b55d9);border-radius:10px;box-shadow:0 5px 12px rgba(66,99,235,.25);color:#fff;display:inline-flex;font-size:15px;font-weight:800;height:34px;justify-content:center;width:34px}.topbar-actions{align-items:center;display:flex;gap:22px}.environment-badge{background:#eef2ff;border:1px solid #dce4ff;border-radius:999px;color:#5268ca;font-size:10px;font-weight:800;letter-spacing:1px;padding:5px 9px}.account-wrap{position:relative}.account-button{align-items:center;background:transparent;border:0;color:var(--ink);display:flex;font-size:13px;font-weight:650;gap:9px;padding:6px 0}.account-avatar{align-items:center;background:#e9edff;border-radius:50%;color:#4b62d7;display:inline-flex;font-size:11px;font-weight:800;height:30px;justify-content:center;width:30px}.chevron{color:#8a94a8;font-size:16px;line-height:1}.account-menu{background:var(--surface);border:1px solid var(--line);border-radius:10px;box-shadow:var(--shadow);min-width:150px;padding:6px;position:absolute;right:0;top:44px;z-index:10}.account-menu a{align-items:center;border-radius:7px;color:var(--ink);display:flex;gap:9px;padding:9px 11px}.account-menu a:hover{background:#f3f5fb}.fa-sign-out{color:var(--muted);font-size:16px}.sidebar{background:var(--navy);color:#aeb9cc;grid-area:sidebar;padding:30px 16px 20px}.nav-label{color:#697895;font-size:10px;font-weight:800;letter-spacing:1.3px;margin:0 14px 13px}.sidebar nav{display:flex;flex-direction:column;gap:4px}.nav-link{align-items:center;border-radius:9px;color:#aeb9cc;display:flex;font-size:13px;font-weight:550;gap:12px;padding:11px 13px;transition:background .15s,color .15s}.nav-link:hover,.nav-link:focus{background:var(--navy-soft);color:#fff}.nav-link.fa-circle{background:rgba(79,106,221,.22);color:#fff}.nav-icon{align-items:center;color:#8190ae;display:inline-flex;font-size:17px;height:20px;justify-content:center;width:20px}.nav-link.fa-circle .nav-icon{color:#91a3ff}.sidebar-footer{align-items:flex-start;border-top:1px solid rgba(255,255,255,.09);display:flex;font-size:11px;gap:9px;line-height:1.45;margin:30px 10px 0;padding-top:18px}.sidebar-footer small{color:#697895}.workspace{grid-area:workspace;min-width:0;overflow:auto}.workspace-inner{margin:0 auto;max-width:1240px;padding:42px 48px 60px}.page-section{width:100%}.page-heading{align-items:flex-start;display:flex;gap:20px;justify-content:space-between;margin-bottom:30px}.page-heading h1{margin-bottom:6px}.page-heading .muted{margin-bottom:0}.page-actions{align-items:center;display:flex;gap:10px}.button{align-items:center;border:1px solid transparent;border-radius:8px;display:inline-flex;font-size:13px;font-weight:700;gap:7px;justify-content:center;line-height:1;padding:11px 15px;transition:background .15s,border-color .15s,box-shadow .15s,transform .15s}.button:hover{transform:translateY(-1px)}.button-primary{background:var(--blue);box-shadow:0 5px 12px rgba(66,99,235,.18);color:#fff}.button-primary:hover{background:var(--blue-dark);color:#fff}.button-secondary{background:var(--surface);border-color:#dfe4ee;color:#4e5b72}.button-secondary:hover,.button-ghost:hover{background:#f0f3f9;color:var(--ink)}.button-ghost{background:transparent;border-color:transparent;color:#5e6a80}.button-full{width:100%}.content-card,.stat-card{background:var(--surface);border:1px solid var(--line);border-radius:13px;box-shadow:var(--shadow)}.stat-grid{display:grid;gap:16px;grid-template-columns:repeat(3,minmax(0,1fr));margin-bottom:24px}.stat-card{align-items:flex-start;display:flex;gap:14px;padding:21px}.stat-icon{align-items:center;border-radius:10px;display:inline-flex;font-size:21px;height:43px;justify-content:center;width:43px}.icon-blue{background:#e9edff;color:#5369dc}.icon-purple{background:#f1eaff;color:#855fd1}.icon-green{background:#e4f7ed;color:#2a9c6d}.stat-label,.stat-caption{color:var(--muted);font-size:12px;margin:0}.stat-card strong{display:block;font-size:16px;margin:2px 0}.welcome-card{align-items:center;display:flex;justify-content:space-between;padding:28px 30px}.welcome-card p:last-child{margin-bottom:0}.status-pill{align-items:center;border-radius:999px;display:inline-flex;font-size:11px;font-weight:750;gap:6px;line-height:1;padding:7px 10px;white-space:nowrap}.status-success{background:var(--green-bg);color:var(--green)}.status-muted{background:#f0f2f6;color:#737d90}.status-danger{background:var(--red-bg);color:var(--red)}.filter-panel{background:#eef2ff;border:1px solid #dfe5ff;border-radius:11px;margin-bottom:12px;padding:16px 18px}.filter-form{align-items:flex-end;display:flex;gap:12px}.field-group{display:flex;flex:1;flex-direction:column;gap:7px}.field-group label{color:#4f5c73;font-size:12px;font-weight:700}.field-group input,.field-group select,.user-form input,.user-form select{background:var(--surface);border:1px solid #dce1eb;border-radius:7px;color:var(--ink);min-height:40px;outline:0;padding:9px 11px;transition:border-color .15s,box-shadow .15s}.field-group input:focus,.field-group select:focus,.user-form input:focus,.user-form select:focus{border-color:#7b8ef0;box-shadow:0 0 0 3px rgba(66,99,235,.12)}.filter-value{flex:2}.filter-chip{align-items:center;background:#fff;border:1px solid #dce3f2;border-radius:999px;display:inline-flex;font-size:12px;font-weight:650;gap:5px;margin:0 0 17px;padding:6px 10px}.table-card{overflow:hidden}.table-toolbar{align-items:center;border-bottom:1px solid var(--line);display:flex;justify-content:space-between;padding:20px 23px}.table-toolbar h2{margin-bottom:3px}.table-toolbar p{font-size:12px;margin-bottom:0}.record-count{color:var(--muted);font-size:12px}.table-scroll{overflow-x:auto}table{border-collapse:collapse;width:100%}th{background:#fafbfe;color:#78839a;font-size:10px;font-weight:800;letter-spacing:.8px;text-align:left;text-transform:uppercase}th,td{border-bottom:1px solid var(--line);padding:14px 20px;white-space:nowrap}td{color:#526077;font-size:13px}.TableRow{cursor:pointer;transition:background .15s}.TableRow:hover{background:#f8f9fd}.TableRow:last-child td{border-bottom:0}.user-cell{align-items:center;display:flex;gap:10px}.avatar{align-items:center;background:#e9edff;border-radius:50%;color:#5369dc;display:inline-flex;font-size:11px;font-weight:800;height:30px;justify-content:center;text-transform:uppercase;width:30px}.user-name{color:var(--ink);font-weight:700}.table-action{align-items:center;display:inline-flex;font-size:12px;font-weight:700;gap:5px}.form-card{max-width:850px;padding:28px 30px}.user-form{margin-top:4px}.form-grid{display:grid;gap:20px;grid-template-columns:repeat(2,minmax(0,1fr))}.form-divider{border-top:1px solid var(--line);margin:28px 0 22px}.form-section-title{font-size:13px;font-weight:750;margin-bottom:15px}.checkbox-grid{display:grid;gap:12px;grid-template-columns:repeat(2,minmax(0,1fr))}.checkbox-label{align-items:flex-start;background:#fafbfe;border:1px solid var(--line);border-radius:9px;cursor:pointer;display:flex;gap:11px;padding:14px}.checkbox-label input{accent-color:var(--blue);height:17px;margin:2px 0 0;width:17px}.checkbox-label strong,.checkbox-label small{display:block}.checkbox-label strong{font-size:13px}.checkbox-label small{color:var(--muted);font-size:11px;margin-top:3px}.form-actions{border-top:1px solid var(--line);display:flex;gap:10px;justify-content:flex-end;margin-top:28px;padding-top:22px}.alert{border-radius:8px;font-size:13px;margin-bottom:20px;padding:11px 13px}.alert-success{background:var(--green-bg);color:var(--green)}.alert-error{background:var(--red-bg);color:var(--red)}.login-page{align-items:center;background:radial-gradient(circle at 50% 0,#e8edff 0,#f6f8fc 42%,#f6f8fc 100%);display:flex;flex-direction:column;justify-content:center;min-height:100vh;padding:30px}.login-card{background:var(--surface);border:1px solid var(--line);border-radius:17px;box-shadow:0 22px 60px rgba(37,54,92,.13);max-width:430px;padding:40px;width:100%}.login-card .brand-lockup{align-items:center;color:var(--ink);display:flex;font-size:18px;font-weight:800;gap:10px;margin-bottom:34px}.login-card .brand-mark{height:38px;width:38px}.login-card h1{font-size:29px;margin-bottom:9px}.login-intro{margin-bottom:27px}.login-form{display:flex;flex-direction:column;gap:18px}.login-form .field-group{gap:8px}.login-form label{color:#3e4b64;font-size:12px;font-weight:700}.login-form input{background:#fbfcfe;border:1px solid #dce1eb;border-radius:8px;min-height:44px;padding:11px 12px}.login-links{display:flex;font-size:12px;justify-content:space-between;margin-top:20px}.login-footer{color:#8993a6;font-size:11px;margin-top:18px}.permissionserrorsContainer{margin-top:14px}.login-card .alert{margin-bottom:0}.login-page .login-footer{margin-bottom:0}@media(max-width:860px){.app-shell{grid-template-columns:78px minmax(0,1fr)}.sidebar{padding-left:10px;padding-right:10px}.nav-label,.nav-link span:not(.nav-icon),.sidebar-footer{display:none}.nav-link{justify-content:center;padding:12px}.workspace-inner{padding:32px 24px 48px}.topbar{padding:0 24px}.stat-grid{grid-template-columns:1fr}.environment-badge{display:none}}@media(max-width:600px){.app-shell{display:block}.topbar{height:66px}.sidebar{padding:8px;position:static}.sidebar nav{flex-direction:row;overflow-x:auto}.nav-link{flex:0 0 auto}.nav-label,.sidebar-footer{display:none}.workspace-inner{padding:26px 16px 40px}.page-heading{flex-direction:column;margin-bottom:22px}.page-actions{width:100%}.page-actions .button{flex:1}.filter-form,.form-grid,.checkbox-grid{display:grid;grid-template-columns:1fr}.filter-form .button{width:100%}.welcome-card{align-items:flex-start;flex-direction:column;gap:20px}.form-card{padding:20px}.topbar-actions{gap:10px}.login-page{padding:18px}.login-card{padding:28px 22px}}";
        return "<!doctype html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title>" + htmlEscape(title) + "</title>"
                + "<style>" + styles + "</style></head><body>" + body + "</body></html>";
    }

    private static boolean matchesFilter(TestUser user, String name, String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        String candidate;
        if ("email".equalsIgnoreCase(name)) {
            candidate = user.email;
        } else {
            candidate = user.username;
        }
        return candidate != null && candidate.toLowerCase().contains(value.toLowerCase());
    }

    private static TestUser userFromJson(String body, int id) {
        String username = firstNonEmpty(jsonString(body, "username"), "user" + id);
        String email = firstNonEmpty(jsonString(body, "email"), username + "@example.test");
        String password = firstNonEmpty(jsonString(body, "password"), "password");
        boolean confirmed = jsonBoolean(body, "confirmed", false);
        boolean blocked = jsonBoolean(body, "blocked", false);
        return new TestUser(id, username, email, password, confirmed, blocked);
    }

    private static TestUser userFromForm(Map<String, String> form, int id) {
        String username = firstNonEmpty(form.get("username"), "user" + id);
        String email = firstNonEmpty(form.get("email"), username + "@example.test");
        String password = firstNonEmpty(form.get("password"), "password");
        boolean confirmed = form.containsKey("confirmed");
        boolean blocked = form.containsKey("blocked");
        return new TestUser(id, username, email, password, confirmed, blocked);
    }

    private static void updateUserFromJson(TestUser user, String body) {
        String username = jsonString(body, "username");
        String email = jsonString(body, "email");
        String password = jsonString(body, "password");
        if (username != null) user.username = username;
        if (email != null) user.email = email;
        if (password != null) user.password = password;
        if (containsJsonKey(body, "confirmed")) user.confirmed = jsonBoolean(body, "confirmed", user.confirmed);
        if (containsJsonKey(body, "blocked")) user.blocked = jsonBoolean(body, "blocked", user.blocked);
    }

    private static void updateUserFromForm(TestUser user, Map<String, String> form) {
        if (form.containsKey("username")) user.username = form.get("username");
        if (form.containsKey("email")) user.email = form.get("email");
        if (form.containsKey("password") && form.get("password") != null && !form.get("password").isEmpty()) {
            user.password = form.get("password");
        }
        user.confirmed = form.containsKey("confirmed");
        user.blocked = form.containsKey("blocked");
    }

    private static int apiAuthStatus(HttpExchange exchange) {
        String auth = header(exchange, "Authorization");
        if (auth == null || auth.trim().isEmpty()) {
            return 403;
        }
        String normalized = auth.trim();
        if (normalized.equals("Bearer " + ADMIN_TOKEN) || normalized.equals(ADMIN_TOKEN)) {
            return 200;
        }
        return 401;
    }

    private static boolean isAdminCredential(String username, String password) {
        if (!ADMIN_PASSWORD.equals(password)) {
            return false;
        }
        return ADMIN_EMAIL.equalsIgnoreCase(username) || ADMIN_USERNAME.equalsIgnoreCase(username);
    }

    private static boolean isUiAuthenticated(HttpExchange exchange) {
        String cookie = header(exchange, "Cookie");
        return cookie != null && cookie.contains(SESSION_COOKIE);
    }

    private static String header(HttpExchange exchange, String name) {
        String value = exchange.getRequestHeaders().getFirst(name);
        return value == null ? "" : value;
    }

    private static void addCommonHeaders(HttpExchange exchange) {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Headers", "Authorization, Content-Type");
        headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
    }

    private static void sendJson(HttpExchange exchange, int status, String json) throws IOException {
        send(exchange, status, "application/json; charset=utf-8", json);
    }

    private static void sendHtml(HttpExchange exchange, int status, String html) throws IOException {
        send(exchange, status, "text/html; charset=utf-8", html);
    }

    private static void send(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        byte[] bytes = body == null ? new byte[0] : body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        if ("HEAD".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(status, -1);
            return;
        }
        exchange.sendResponseHeaders(status, bytes.length);
        OutputStream output = exchange.getResponseBody();
        output.write(bytes);
        output.flush();
    }

    private static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
    }

    private static void methodNotAllowed(HttpExchange exchange) throws IOException {
        sendJson(exchange, 405, "{\"statusCode\":405,\"error\":\"Method Not Allowed\"}");
    }

    private static String readBody(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;
        while ((read = input.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }
        return new String(output.toByteArray(), StandardCharsets.UTF_8);
    }

    private static Map<String, String> parseForm(String body) {
        Map<String, String> values = new LinkedHashMap<String, String>();
        if (body == null || body.isEmpty()) {
            return values;
        }
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            String key = urlDecode(keyValue[0]);
            String value = keyValue.length > 1 ? urlDecode(keyValue[1]) : "";
            values.put(key, value);
        }
        return values;
    }

    private static Map<String, String> parseQuery(URI uri) {
        return parseForm(uri.getRawQuery() == null ? "" : uri.getRawQuery());
    }

    private static String urlDecode(String value) {
        try {
            return URLDecoder.decode(value, "UTF-8");
        } catch (Exception e) {
            return value;
        }
    }

    private static Integer extractTrailingId(String path, String prefix) {
        if (path.equals(prefix)) {
            return null;
        }
        String value = path.substring(prefix.length());
        if (value.startsWith("/")) {
            value = value.substring(1);
        }
        if (value.isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static String normalizePath(String path) {
        if (path == null || path.isEmpty()) {
            return "/";
        }
        if (path.length() > 1 && path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }

    private static String jsonString(String json, String key) {
        if (json == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*\\\"((?:\\\\.|[^\\\"])*)\\\"");
        Matcher matcher = pattern.matcher(json);
        if (!matcher.find()) {
            return null;
        }
        return jsonUnescape(matcher.group(1));
    }

    private static boolean containsJsonKey(String json, String key) {
        if (json == null) {
            return false;
        }
        return Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:").matcher(json).find();
    }

    private static boolean jsonBoolean(String json, String key, boolean defaultValue) {
        if (json == null) {
            return defaultValue;
        }
        Pattern pattern = Pattern.compile("\\\"" + Pattern.quote(key) + "\\\"\\s*:\\s*(true|false)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(json);
        return matcher.find() ? Boolean.parseBoolean(matcher.group(1)) : defaultValue;
    }

    private static String jsonEscape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "\\r").replace("\n", "\\n");
    }

    private static String jsonUnescape(String value) {
        if (value == null) {
            return null;
        }
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static String htmlEscape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    private static String firstNonEmpty(String... values) {
        for (String value : values) {
            if (value != null && !value.isEmpty()) {
                return value;
            }
        }
        return "";
    }

    private static String initial(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "U";
        }
        return value.trim().substring(0, 1);
    }

    private static final class TestUser {
        private final int id;
        private volatile String username;
        private volatile String email;
        private volatile String password;
        private volatile boolean confirmed;
        private volatile boolean blocked;

        private TestUser(int id, String username, String email, String password, boolean confirmed, boolean blocked) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.confirmed = confirmed;
            this.blocked = blocked;
        }

        private String toJson() {
            return "{\"id\":" + id
                    + ",\"username\":\"" + jsonEscape(username) + "\""
                    + ",\"email\":\"" + jsonEscape(email) + "\""
                    + ",\"provider\":\"local\""
                    + ",\"confirmed\":" + confirmed
                    + ",\"blocked\":" + blocked
                    + ",\"role\":{\"id\":1,\"name\":\"Authenticated\",\"description\":\"Default role given to authenticated user.\",\"type\":\"authenticated\"}}";
        }
    }
}
