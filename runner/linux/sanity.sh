#!/bin/bash -l
cd "${0%/*}"
cd ../../selenium
mvn test -P sanityTests
xdg-open ./extent.html
