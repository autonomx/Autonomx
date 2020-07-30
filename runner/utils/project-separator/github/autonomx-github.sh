#!/bin/bash -l
export LC_ALL=en_US.UTF-8

VERSION=$1

cd "$(dirname ${BASH_SOURCE[0]})"

# pull the latest autonomx
rm -rf ./Autonomx
git clone https://github.com/autonomx/Autonomx.git

# move current autonomx client to temp folder
mv ./Autonomx/.git ./tmp/
rm -rf ./Autonomx/


# copy client project
echo copy autonomx project
rsync -r ../autonomx-client/Autonomx ./

#### setup web
cd ./Autonomx/

# remove current pom
rm -rf ./automation/pom.xml

# move and replace pom with maven central pom 
mv ./automation/maven-central/pom.xml ./automation/pom.xml

# remove unecessary files
rm -rf ./bitbucket-pipelines.yml
rm -rf ./automation/maven-central

# remove the copied git forlder and move the .git folder back a
rm -rf ./.git
mv ../tmp/ ./.git/

# change version to latest autnonomx-core
cd ./automation/
mvn versions:use-latest-versions -Dincludes=io.autonomx:autonomx-core
rm -rf ./pom.xml.versionsBackup











