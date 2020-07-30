#!/usr/bin/env sh
export LC_ALL=en_US.UTF-8

cd "$(dirname ${BASH_SOURCE[0]})"

# clone script server
if [ ! -d apache-maven* ];  then
	rm -rf ./*/
	curl -SL https://archive.apache.org/dist/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.zip > maven_download.zip && \
	unzip maven_download.zip                                  && \
	rm maven_download.zip
fi

# rename to maven
mv apache-maven* maven