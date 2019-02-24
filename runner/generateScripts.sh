#!/bin/bash
export LC_ALL=en_US.UTF-8

# set current directory
cd "$(dirname ${BASH_SOURCE[0]})"

# get the list of file names in mac folder
yourfilenames=`ls ../automation/suites/*.xml`

# creates run script for each suite

# win
for eachfile in $yourfilenames
do
	
   fileName=$(basename $eachfile .xml)
   printf 'cd ../../automation\n ../runner/utils/mvn/bin/mvn clean compiler:compile compiler:testCompile surefire:test -Dsurefire.suiteXmlFiles=suites/%s.xml' $fileName > ./win/$fileName.bat
   echo "generate win batch file: " $fileName
done

# mac
for eachfile in $yourfilenames
do
	
   fileName=$(basename $eachfile .xml)
   printf '#!/bin/bash\n cd "$(dirname ${BASH_SOURCE[0]})"\n cd ../../automation\n ../runner/utils/mvn/bin/mvn clean compiler:compile compiler:testCompile surefire:test -Dsurefire.suiteXmlFiles=suites/%s.xml' $fileName > ./mac/$fileName.sh
   chmod 777 ./mac/$fileName.sh
   echo "generate mac bash file: " $fileName
done

# linux
for eachfile in $yourfilenames
do
	
   fileName=$(basename $eachfile .xml)
   printf '#!/bin/bash\n cd "$(dirname ${BASH_SOURCE[0]})"\n cd ../../automation\n ../runner/utils/mvn/bin/mvn clean compiler:compile compiler:testCompile surefire:test -Dsurefire.suiteXmlFiles=suites/%s.xml' $fileName > ./linux/$fileName.sh
   chmod 777 ./mac/$fileName.sh
   echo "generate linux bash file: " $fileName
done

