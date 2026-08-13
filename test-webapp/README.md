# Autonomx framework test webapp

This dependency-free Java server replaces the deleted hosted demo application used by the Autonomx framework functional and UI tests.

It emulates the test-visible contract of the old Strapi-style application:

- `POST /admin/login` for admin token login
- `GET|POST /content-manager/collection-types/plugins::users-permissions.user`
- `GET|PUT|PATCH|DELETE /content-manager/collection-types/plugins::users-permissions.user/{id}`
- `/admin` login UI and `/admin/auth/login` compatibility route
- local user create/edit/filter/logout pages using the legacy selectors expected by the page objects
- `GET /health` for a readiness check

The default server address is `http://127.0.0.1:18080`.

The server stores test users in memory, so state resets when the process restarts. The admin credentials intentionally match the existing test data: `autouser313@gmail.com` / `autoPass1`. API login also accepts `autoAdmin1` with the same password.

## Start on Windows

```bat
cd test-webapp
start.bat
```

## Start on macOS/Linux

```bash
cd test-webapp
./start.sh
```

Keep the server running, then execute the existing framework functional suite from another terminal:

```bash
cd automation
java -jar ./.maven/maven.jar clean compile test -DsuiteXmlFile=suites/frameworkFunctionalTests.xml
```

You can check readiness at `http://127.0.0.1:18080/health` before starting the suite.
