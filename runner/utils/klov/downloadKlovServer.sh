#!/bin/bash -l
cd "$(dirname ${BASH_SOURCE[0]})"

git clone https://github.com/extent-framework/klov-server.git
mv ./klov-server/0.2.5/klov-0.2.5.jar ./klov-0.2.5.jar
rm -rf ./klov-server