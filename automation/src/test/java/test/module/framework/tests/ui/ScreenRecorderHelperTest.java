package test.module.framework.tests.ui;


import java.io.File;
import java.util.ArrayList;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.ExtentManager;
import core.support.logger.TestLog;
import data.Data;
import data.framework.User;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ScreenRecorderHelperTest extends TestBase {

	@BeforeClass
	public void beforeClass() throws Exception {
		Config.setGlobalValue("recorder.enableRecording", true);
		Config.setGlobalValue("recorder.onFailedTestsOnly", false);
		
		ConfigVariable.recorderEnableRecording().setValue(true);
		ConfigVariable.recorderOnFailedTestsOnly().setValue(false);
		setupWebDriver(app.framework.getWebDriver());
	}
	
	@Test
	public void verifyClickPoint() {
		
		
		
		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
		
	}
	
	@AfterClass
	public void afterClass()  {
		
		ArrayList<File> videos = Helper.getFileList( ExtentManager.getReportRootFullPath(), true);
		Helper.assertTrue("no video files created",!videos.isEmpty());
		
		boolean isFound = false;
		for(File file:videos){
			if(file.getName().contains("verifyClickPoint"))
				isFound = true;
		}
		Helper.assertTrue("correct video not found", isFound);
	}
}