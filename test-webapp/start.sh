#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p .build
javac -encoding UTF-8 -d .build FrameworkTestWebApp.java
exec java -cp .build FrameworkTestWebApp "${AUTONOMX_TEST_WEBAPP_PORT:-18080}"
