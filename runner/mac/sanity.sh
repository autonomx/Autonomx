#!/bin/bash -l
cd "${0%/*}"
cd ../../selenium
mvn test -P sanityTests
open ./extent.html
