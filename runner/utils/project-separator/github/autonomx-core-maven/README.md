# Github

replace autonomx-core pom file with pom-maven-central.xml

Run to deploy project to maven central

Tutorial:
https://stackoverflow.com/questions/28846802/how-to-manually-publish-jar-to-maven-central

https://central.sonatype.org/pages/apache-maven.html

// deploy to maven central 
mvn clean deploy
mvn nexus-staging:release
mvn nexus-staging:drop