#!/bin/bash -l
export LC_ALL=en_US.UTF-8

cd "$(dirname ${BASH_SOURCE[0]})"


function generateJsonScripts () {

	# get the list of file names in mac folder
	yourfilenames=`ls ../../mac/*.sh`

	# convert each file path to json format
	for eachfile in $yourfilenames
	do
		
	   fileName=$(basename $eachfile .sh)
	   fileName="$fileName" 
	   printf '{"script_path": "%s"}' $eachfile > ./conf/runners/$fileName.json
	   echo "generateJsonScripts: " $fileName
	done
}


# generate json files from test scripts for script server
bash generateScripts.sh

# run script server
cd utils/script-server

#convert testng xml suites to maven test scripts
generateJsonScripts

# install requirements
pip install -r requirements.txt --no-index

python -mwebbrowser http://localhost:5000
# launch the server
./launcher.py 

