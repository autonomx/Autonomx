#!/usr/bin/env sh
export LC_ALL=en_US.UTF-8

cd "$(dirname ${BASH_SOURCE[0]})"

# clone script server
if [ ! -d "./utils/script-server/web" ]; then
	rm ./utils/script-server
	mkdir ./utils/script-server                       && \
	cd ./utils/script-server                         && \
	curl -SL https://github.com/bugy/script-server/releases/download/1.14.0/script-server.zip > script-server.zip && \
	unzip script-server.zip                                  && \
	rm script-server.zip
fi


# remove existing scrips
rm -rf ./utils/script-server/conf/runners/*

function generateJsonScripts () {

	# set run scripts based on OS
    if [[ "$OSTYPE" == "msys" ]]; then
    		yourfilenames=`ls ../../win/*.bat`
    else		
		# get the list of file names in mac folder
		yourfilenames=`ls ../../mac/*.sh`
	fi

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
bash ./generateScripts.sh

# run script server
cd ./utils/script-server

#convert testng xml suites to maven test scripts
generateJsonScripts

# replace python3 with python
sed -i -e 's/python3/python/g' ./launcher.py

# install requirements
pip install -r requirements.txt

python -mwebbrowser http://localhost:5000
# launch the server
./launcher.py 