#!/bin/bash -l
export LC_ALL=en_US.UTF-8

cd "$(dirname ${BASH_SOURCE[0]})"

# install requirements
pip install -r requirements.txt --no-index
