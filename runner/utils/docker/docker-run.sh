#!/usr/bin/env sh
export LC_ALL=en_US.UTF-8

# copy automation folder to docker directory
mkdir -p automation/automation
mkdir -p automation/apiTestData

cp -R ../../../automation automation
cp -R ../../../apiTestData automation


cd "$(dirname ${BASH_SOURCE[0]})"
docker-compose build