package test.module.framework.tests.functional;


import javax.security.auth.login.LoginException;

import org.openqa.selenium.WebDriverException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.helpers.Helper;
import core.support.logger.TestLog;
import core.support.objects.TestObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class RetryTest extends TestBase {

	@BeforeClass
	public void beforeClass()  {

	}
	
	@BeforeMethod
	public void beforeMethod() throws Exception {
		
	}
	
	@Test(description = "verify test retry")
	public void verifyRetryFunction() {
		
		// scope of retry value is test level. RetryTest class will use the same scope to get the retry count
		ConfigVariable.globalRetryCount().setValue(1);

		int runCount = TestObject.getTestInfo().runCount;
		if(runCount == 1) {
			TestLog.When("I fail on first retry");
			Helper.assertFalse("failed on first run");
			
		}else if(runCount == 2) {
			TestLog.Then("I pass on second retry");
			int retryCountUpdated = ConfigVariable.globalRetryCount().toInt();
			Helper.assertEquals(1, retryCountUpdated);
			Helper.assertTrue("should pass", true);
		}
	}
	
	@Test(description = "verify test retry")
	public void verifyRetryFunction_screenRecorder_onFailOnly() {
			
		// scope of retry value is test level. RetryTest class will use the same scope to get the retry count
		ConfigVariable.globalRetryCount().setValue(1);
		ConfigVariable.recorderEnableRecording().setValue(true);
		ConfigVariable.recorderOnFailedTestsOnly().setValue(true);
		
		int runCount = TestObject.getTestInfo().runCount;
		if(runCount == 1) {
			TestLog.When("I fail on first retry");
			Helper.assertTrue("screen recorder initiated", !TestObject.getTestInfo().isScreenRecorderInitiated);
			
			Helper.assertFalse("failed on first run");
			
		}else if(runCount == 2) {
			TestLog.Then("I pass on second retry");
			int retryCountUpdated = ConfigVariable.globalRetryCount().toInt();
			Helper.assertEquals(1, retryCountUpdated);
			Helper.assertTrue("should pass", true);
			Helper.assertTrue("screen recorder initiated", TestObject.getTestInfo().isScreenRecorderInitiated);
		}
	}
	
	@Test(description = "verify test retry")
	public void verifyRetryFunction_screenRecorder() {
			
		// scope of retry value is test level. RetryTest class will use the same scope to get the retry count
		ConfigVariable.globalRetryCount().setValue(1);
		ConfigVariable.recorderEnableRecording().setValue(true);
		ConfigVariable.recorderOnFailedTestsOnly().setValue(false);
		
		int runCount = TestObject.getTestInfo().runCount;
		if(runCount == 1) {
			TestLog.When("I fail on first retry");
			Helper.assertTrue("screen recorder initiated", TestObject.getTestInfo().isScreenRecorderInitiated);
			
			Helper.assertFalse("failed on first run");
			
		}else if(runCount == 2) {
			TestLog.Then("I pass on second retry");
			int retryCountUpdated = ConfigVariable.globalRetryCount().toInt();
			Helper.assertEquals(1, retryCountUpdated);
			Helper.assertTrue("should pass", true);
			Helper.assertTrue("screen recorder initiated", TestObject.getTestInfo().isScreenRecorderInitiated);
		}
	}
	
	@Test(description = "verify test retry")
	public void verifyRetryFunction_screenRecorder_retry_zero() {
			
		// scope of retry value is test level. RetryTest class will use the same scope to get the retry count
		ConfigVariable.globalRetryCount().setValue(0);
		ConfigVariable.recorderEnableRecording().setValue(true);
		ConfigVariable.recorderOnFailedTestsOnly().setValue(true);
		
		int runCount = TestObject.getTestInfo().runCount;
		if(runCount == 1) {
			TestLog.When("I fail on first retry");
			Helper.assertTrue("screen recorder initiated", !TestObject.getTestInfo().isScreenRecorderInitiated);
			
			Helper.assertFalse("failed on first run");
			
		}else if(runCount == 2) {
			TestLog.Then("I pass on second retry");
			int retryCountUpdated = ConfigVariable.globalRetryCount().toInt();
			Helper.assertEquals(0, retryCountUpdated);
			Helper.assertTrue("should pass", true);
			Helper.assertTrue("screen recorder initiated", TestObject.getTestInfo().isScreenRecorderInitiated);
		}
	}
	
	@Test(description = "verify test retry")
	public void verifyRetryFunction_screenRecorder_disabled() {
			
		// scope of retry value is test level. RetryTest class will use the same scope to get the retry count
		ConfigVariable.globalRetryCount().setValue(1);
		ConfigVariable.recorderEnableRecording().setValue(false);
		ConfigVariable.recorderOnFailedTestsOnly().setValue(false);
		
		int runCount = TestObject.getTestInfo().runCount;
		if(runCount == 1) {
			TestLog.When("I fail on first retry");
			Helper.assertTrue("screen recorder initiated", !TestObject.getTestInfo().isScreenRecorderInitiated);
			
			Helper.assertFalse("failed on first run");
			
		}else if(runCount == 2) {
			TestLog.Then("I pass on second retry");
			int retryCountUpdated = ConfigVariable.globalRetryCount().toInt();
			Helper.assertEquals(1, retryCountUpdated);
			Helper.assertTrue("should pass", true);
			Helper.assertTrue("screen recorder initiated", !TestObject.getTestInfo().isScreenRecorderInitiated);
		}
	}
	
	// note: config value is not passed in test rerun
	@Test(description = "verify extra retry when certain exceptions are thrown")
	public void verifyExceptionRetryHandling() throws LoginException {
		ConfigVariable.globalRetryCount().setValue(1);	

		int runCount = TestObject.getTestInfo().runCount;
		
		if(runCount == 1) {
			TestLog.When("I throw webdriver exception, then retry count should increase");
			throw new WebDriverException();
		
		}else if(runCount == 2) {
			TestLog.Then("I throw login exception on second retry");
			int retryCountUpdated = ConfigVariable.globalRetryCount().toInt();
			
			// config value is not passed on rerun
			Helper.assertEquals(1, retryCountUpdated);
			throw new LoginException();

		}else if(runCount == 3) {
			TestLog.Then("I pass on third retry");
			int retryCountUpdated = ConfigVariable.globalRetryCount().toInt();
			
			// config value is not passed on rerun
			Helper.assertEquals(1, retryCountUpdated);
			Helper.assertTrue("should pass", true);
		}
	}
	
	@Test(expectedExceptions = RuntimeException.class, description = "verify fail on retry")
	public void verifyFailAfterRetry() {
		ConfigVariable.globalRetryCount().setValue(1);

		int runCount = TestObject.getTestInfo().runCount;
		if(runCount == 1) {
			TestLog.When("I fail on first retry");
			Helper.assertFalse("failed on first run");
			
		}else if(runCount == 2) {
			TestLog.Then("I fail on second retry");
			throw new RuntimeException();
		}
	}
	
	@Test(expectedExceptions = RuntimeException.class, description = "verify fail without retry")
	public void verifyFailNoRetry() {
		ConfigVariable.globalRetryCount().setValue(0);

		int runCount = TestObject.getTestInfo().runCount;
		if(runCount == 1) {
			TestLog.When("I fail on first run");
			throw new RuntimeException();
			
	    // will not get to this point
		}else if(runCount == 2) {
			TestLog.Then("I fail on second retry");
			Helper.assertFalse("should fail on run exception");
		}
	}
}