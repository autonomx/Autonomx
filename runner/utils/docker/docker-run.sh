#!/usr/bin/env sh
export LC_ALL=en_US.UTF-8

cd "$(dirname ${BASH_SOURCE[0]})"
docker-compose build