#!/bin/bash -l
export LC_ALL=en_US.UTF-8

VERSION=$1

cd "$(dirname ${BASH_SOURCE[0]})"

# pull the latest autonomx
rm -rf ./autonomx-core
git clone https://github.com/autonomx/AutonomxCore.git

# move current autonomx client to temp folder
mv ./AutonomxCore/.git ./tmp/
rm -rf ./AutonomxCore/


# copy client project
echo copy Autonomx-core project
rsync -r ../autonomx-core/ ./autonomx-core

#### setup core
cd ./autonomx-core/


# remove unecessary files
rm -rf ./automation/maven-central

# remove the copied git forlder and move the .git folder back a
rm -rf ./.git
mv ../tmp/ ./.git/
rm -rf ../tmp/











