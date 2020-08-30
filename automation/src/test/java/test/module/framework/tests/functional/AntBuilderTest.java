package test.module.framework.tests.functional;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.testng.annotations.Test;

import core.helpers.Helper;
import marker.SourceChangeDetector;
import test.module.framework.TestBase;

public class AntBuilderTest extends TestBase {

	@Test(priority=1)
	public void verifyMavenDownload() throws Exception {
		   File marker = new File("target/generated-sources/annotations/marker/marker.java");
		   File mavenDir = new File(".maven");
		   Helper.deleteFile(mavenDir.getAbsolutePath());
		   Helper.assertTrue("maven did not get deleted", !mavenDir.exists());

		   runAnt();
		   
		   Helper.assertTrue("maven did not download", mavenDir.exists());
		   File mavenJar = new File(".maven/maven.jar");
		   
		   Helper.assertTrue("maven did not download", mavenJar.exists());
		   
		   Helper.assertTrue("marker does not exist", marker.exists());
	}
	
	@Test(priority=2)
	public void verifyDetectChange() throws Exception {
		   
		   Helper.deleteFile("src/main/java/tempDir");
		   File tempClassDir = new File("target/classes/tempDir");

		   File tempFile = new File("src/main/java/tempDir/temp"+Helper.generateRandomString(3)+".java");
		   File marker = new File("target/generated-sources/annotations/marker/marker.java");
		   File maven_status = new File("target/maven-status");

		   // create temp file 
		   Helper.createFileFromPath(tempFile.getAbsolutePath());
		   
		   // delete class file if exist
		   Helper.deleteFile(tempClassDir.getAbsolutePath());
		   
		   Helper.assertTrue("source detection failed", SourceChangeDetector.hasSourceChanged());
		   
		   
		   Helper.assertTrue("marker does not exist", !marker.exists());
		   Helper.assertTrue("maven_status does not exist", !maven_status.exists());

		   Helper.deleteFile("src/main/java/tempDir");
	}
	
	public void runAnt() {
		   File buildFile = new File("ant.xml");
		   Project p = new Project();
		   p.setUserProperty("ant.file", buildFile.getAbsolutePath());
		   p.init();
		   ProjectHelper helper = ProjectHelper.getProjectHelper();
		   p.addReference("ant.projectHelper", helper);
		   helper.parse(p, buildFile);
		   try {
			   p.executeTarget(p.getDefaultTarget());
		   }catch(Exception e) {
			   e.printStackTrace();
			   Helper.waitForSeconds(3);
		   }
	}
	
	
}
