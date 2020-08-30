cd automation

del /F /Q .maven
mkdir .maven
curl -A "maven jar" -L "https://github.com/autonomx/mavenRunner/releases/latest/download/maven.jar" -o .maven/maven.jar

java -jar ./.maven/maven.jar clean install -U -DskipTests 