package test.module.framework.tests;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.helpers.Helper;
import core.support.annotation.helper.utils.MavenCommandRunner;
import test.module.framework.TestBase;

public class MavenRunnerTest extends TestBase {

	@Test(priority=1)
	public void verifyMavenDownload() throws Exception {
		MavenCommandRunner.MAVEN_PATH = "";
		
		File mavenDestination = new File(MavenCommandRunner.MAVEN_DOWNLOAD_DESTINATION);
		FileUtils.deleteDirectory(mavenDestination);
		
		MavenCommandRunner.downloadMavenIfNotExist();

        File mavenFile = MavenCommandRunner.verifyAndGetMavenHomePath();
		Helper.assertTrue("maven destination not found", mavenFile.exists());
		
		File mavenHome = new File(mavenFile.getAbsolutePath() + "/bin");
		Helper.assertTrue("maven destination not found at " + mavenHome.getAbsolutePath(), mavenHome.exists());
	}
	
	@Test(dependsOnMethods = "verifyMavenDownload", priority=2)
	public void verifyMavenPathFromConfig() throws Exception {
		MavenCommandRunner.MAVEN_PATH = "";
	
		// download maven to utils folder
		MavenCommandRunner.downloadMavenIfNotExist();
        File mavenFile = MavenCommandRunner.verifyAndGetMavenHomePath();
		MavenCommandRunner.MAVEN_PATH = "";

		// set config path
		ConfigVariable.mavenHome().setValue(mavenFile.getAbsolutePath());
		MavenCommandRunner.setMavenPathFromConfig();

		Helper.assertEquals(mavenFile.getAbsolutePath(), MavenCommandRunner.MAVEN_PATH);
	}
	@Test()
	public void verifyMavenPathFromCommandLine() throws Exception {
		MavenCommandRunner.MAVEN_PATH = "";
		MavenCommandRunner.setMavenPath();
		Helper.assertTrue("maven path not found: " + MavenCommandRunner.MAVEN_PATH, MavenCommandRunner.MAVEN_PATH.contains("maven"));

		File mavenFolder = new File(MavenCommandRunner.MAVEN_PATH);
		Helper.assertTrue("maven folder not found: " + MavenCommandRunner.MAVEN_PATH, mavenFolder.exists());
	}
}
