#!/bin/bash -l
export LC_ALL=en_US.UTF-8

VERSION=$1

cd "$(dirname ${BASH_SOURCE[0]})"
# return to outside of autnomx directory
cd ../../../../

# remove web project 
rm -rf ./autonomx-complete

# create sub projects
echo copy complete project
rsync -r ./autonomx/ ./autonomx-complete/

#### setup web
cd ./autonomx-complete/

# remove temp folders
rm -rf ./automation/target/classes
rm -rf ./automation/target/maven-status
rm -rf ./automation/target/test-classes
rm -rf ./automation/test-output

# remove git folders
rm -rf ./.git

# remove project separator
rm -rf ./runner/utils/project-separator

# remove runner 
echo set runner 
bash runner/generateScripts.sh

# add to zip
cd ../
zip autonomx-complete-$VERSION.zip ./autonomx-complete

# remove non zip project
rm -rf ./autonomx-complete











