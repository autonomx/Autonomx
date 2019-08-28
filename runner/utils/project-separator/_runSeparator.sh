#!/bin/bash -l
export LC_ALL=en_US.UTF-8

Version=1.0.3

cd "$(dirname ${BASH_SOURCE[0]})"

bash ./webProject.sh $Version
bash ./androidProject.sh $Version
bash ./iosProject.sh $Version
bash ./winProject.sh $Version
bash ./serviceProject.sh $Version
bash ./webServiceAndIntegration.sh $Version
bash ./autonomxComplete.sh $Version