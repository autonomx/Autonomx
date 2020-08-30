#!/usr/bin/env sh
export LC_ALL=en_US.UTF-8

# set current directory
cd "$(dirname ${BASH_SOURCE[0]})"

# get the list of file names in mac folder
yourfilenames=`ls ../automation/suites/*.xml`

# remove existing scripts
rm ./win/*
rm ./mac/*
rm ./linux/*

# creates run script for each suite

# win
for eachfile in $yourfilenames
do
	
   fileName=$(basename $eachfile .xml)
   printf ' cd %%~dp0\n cd ../../automation\n if not exist .maven/maven.jar ( \n  call %%~dp0../../setup.bat \n ) \n java -jar .maven/maven.jar clean compile test -DsuiteXmlFile=suites/%s.xml \n cmd /k' $fileName > ./win/$fileName.bat
   echo "generate win batch file: " $fileName
done

# mac
for eachfile in $yourfilenames
do
	
   fileName=$(basename $eachfile .xml)
   printf '#!/bin/bash\n cd "$(dirname ${BASH_SOURCE[0]})"\n cd ../../automation \n if [ ! -f .maven/maven.jar ]; then \n   bash ../setup.sh \n fi \n java -jar ./.maven/maven.jar clean compile test -DsuiteXmlFile=suites/%s.xml \n $SHELL' $fileName > ./mac/$fileName.sh
   chmod 777 ./mac/$fileName.sh
   echo "generate mac bash file: " $fileName
done

# linux
for eachfile in $yourfilenames
do
	
   fileName=$(basename $eachfile .xml)
   printf '#!/bin/bash\n cd "$(dirname ${BASH_SOURCE[0]})"\n cd ../../automation \n if [ ! -f .maven/maven.jar ]; then \n   bash ../setup.sh \n fi \n java -jar ./.maven/maven.jar clean compile test -DsuiteXmlFile=suites/%s.xml \n $SHELL' $fileName > ./mac/$fileName.sh
   chmod 777 ./mac/$fileName.sh
   echo "generate linux bash file: " $fileName
done

