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
        body.append("<main class=\"login\"><h1>Autonomx Test Admin</h1>")
                .append("<form method=\"post\" action=\"/admin/login\">")
                .append("<label for=\"email\">Email</label>")
                .append("<input id=\"email\" name=\"email\" type=\"email\" autocomplete=\"username\">")
                .append("<label for=\"password\">Password</label>")
                .append("<input id=\"password\" name=\"password\" type=\"password\" autocomplete=\"current-password\">")
                .append("<button type=\"submit\">Login</button>")
                .append("</form>")
                .append("<a href=\"/admin/forgot-password\">Forgot password?</a>")
                .append("<a class=\"main-site\" href=\"/\">Main site</a>")
                .append("<div class=\"Loading\" style=\"display:none\">Loading</div>");
        if (error != null) {
            body.append("<div id=\"error-email\" class=\"permissionserrorsContainer\">")
                    .append(htmlEscape(error)).append("</div>");
        }
        body.append("</main>");
        sendHtml(exchange, 200, page("Login", body.toString()));
    }

    private static void renderDashboard(HttpExchange exchange) throws IOException {
        String content = "<section><h1>Dashboard</h1><p>Local framework test application is ready.</p></section>";
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
        body.append("<section><h1>Users</h1>")
                .append("<button color=\"Primary\" type=\"button\" onclick=\"window.location='/admin/users/new'\">Add new user</button>")
                .append("<button type=\"button\" onclick=\"document.getElementById('filter-panel').style.display='block'\">Filters</button>")
                .append("<div id=\"filter-panel\" style=\"").append(filterValue.isEmpty() ? "display:none" : "display:block").append("\">")
                .append("<form method=\"get\" action=\"/admin/users\">")
                .append("<select id=\"filters.0.name\" name=\"filters.0.name\"><option value=\"username\">username</option><option value=\"email\">email</option></select>")
                .append("<input id=\"filters.0.value\" name=\"filters.0.value\" value=\"").append(htmlEscape(filterValue)).append("\">")
                .append("<button type=\"submit\">Apply</button>")
                .append("</form></div>");
        if (!filterValue.isEmpty()) {
            body.append("<a class=\"remove__admin\" href=\"/admin/users\">Remove filter</a>");
        } else {
            // Keep the legacy selector present without affecting the visible UI.
            body.append("<a class=\"remove__admin\" href=\"/admin/users\" style=\"display:none\">Remove filter</a>");
        }

        body.append("<table><thead><tr><th>ID</th><th>Username</th><th>Email</th><th>Confirmed</th><th>Blocked</th><th>Edit</th></tr></thead><tbody>");
        for (TestUser user : users) {
            if (!matchesFilter(user, filterName, filterValue)) {
                continue;
            }
            body.append("<tr class=\"TableRow\" onclick=\"window.location='/admin/users/").append(user.id).append("/edit'\">")
                    .append("<td>").append(user.id).append("</td>")
                    .append("<td>").append(htmlEscape(user.username)).append("</td>")
                    .append("<td>").append(htmlEscape(user.email)).append("</td>")
                    .append("<td>").append(user.confirmed).append("</td>")
                    .append("<td>").append(user.blocked).append("</td>")
                    .append("<td><a class=\"fa-pencil\" href=\"/admin/users/").append(user.id).append("/edit\">Edit</a></td></tr>");
        }
        body.append("</tbody></table></section>");
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
        body.append("<section><h1>").append(create ? "Add user" : "Edit user").append("</h1>");
        if (saved) {
            body.append("<div title=\"Saved\">Saved</div>");
        }
        body.append("<form method=\"post\" action=\"").append(action).append("\">")
                .append("<label for=\"username\">Username</label><input id=\"username\" name=\"username\" value=\"").append(htmlEscape(username)).append("\">")
                .append("<label for=\"email\">Email</label><input id=\"email\" name=\"email\" value=\"").append(htmlEscape(email)).append("\">")
                .append("<label for=\"password\">Password</label><input id=\"password\" name=\"password\" type=\"password\">")
                .append("<label>Role</label><select class=\"Select-control\" name=\"role\"><option>Authenticated</option></select>")
                .append("<label for=\"confirmed\">Confirmed</label><input id=\"confirmed\" name=\"confirmed\" type=\"checkbox\" value=\"true\"")
                .append(confirmed ? " checked" : "").append(">")
                .append("<label for=\"blocked\">Blocked</label><input id=\"blocked\" name=\"blocked\" type=\"checkbox\" value=\"true\"")
                .append(blocked ? " checked" : "").append(">")
                .append("<button type=\"submit\">Save</button></form></section>");
        sendHtml(exchange, 200, page(create ? "Add user" : "Edit user", shell(body.toString())));
    }

    private static String shell(String content) {
        return "<header><a class=\"projectName\" href=\"/admin/dashboard\">Autonomx</a>"
                + "<button class=\"fa-caret-down\" type=\"button\" onclick=\"var m=document.getElementById('user-menu');m.style.display=(m.style.display==='block'?'none':'block')\">Account</button>"
                + "<div id=\"user-menu\" style=\"display:none\"><a role=\"menuitem\" href=\"/admin/logout\"><span class=\"fa-sign-out\"></span>Logout</a></div></header>"
                + "<nav><a class=\"fa-circle\" href=\"/admin/users\">Users</a> "
                + "<a href=\"/admin/ctm-configurations\">Content Manager</a> "
                + "<a href=\"/admin/content-type-builder\">Content Type Builder</a> "
                + "<a href=\"/admin/upload\">Upload</a> "
                + "<a href=\"/admin/plugins/users-permissions\">Roles &amp; Permissions</a></nav>"
                + "<main>" + content + "</main>";
    }

    private static String page(String title, String body) {
        return "<!doctype html><html><head><meta charset=\"utf-8\"><title>" + htmlEscape(title) + "</title>"
                + "<style>body{font-family:Arial,sans-serif;margin:24px}header,nav{margin-bottom:18px}nav a{margin-right:12px}.projectName{font-size:22px;font-weight:bold;margin-right:20px}label{display:block;margin-top:10px}input,select,button{margin:4px 0;padding:6px}table{border-collapse:collapse;margin-top:16px}th,td{border:1px solid #ccc;padding:8px}.TableRow{cursor:pointer}.permissionserrorsContainer,#error-email{color:#b00020;margin-top:12px}</style>"
                + "</head><body>" + body + "</body></html>";
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
