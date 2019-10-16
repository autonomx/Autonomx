package test.module.framework.tests.functional.service;


import java.util.concurrent.TimeUnit;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.apiCore.interfaces.RestApiInterface;
import core.helpers.Helper;
import core.helpers.StopWatchHelper;
import core.support.logger.TestLog;
import core.support.objects.ServiceObject;
import serviceManager.Service;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class RestApiInterfaceTimeoutTest extends TestBase {
	
	long passedTimeInSeconds = 0;
	StopWatchHelper watch = null;
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {

	}
	
	@Test(expectedExceptions = { AssertionError.class })
	public void verifyValidationTimeout() {	
		
		TestLog.When("I run a test using invalid token with validation timeout set to 2 seconds");

		ConfigVariable.apiTimeoutValidationIsEnabled().setValue(true);
		ConfigVariable.apiTimeoutValidationSeconds().setValue(2);
		ConfigVariable.setValue("accessTokenAdmin", "invalid");
		
		watch = StopWatchHelper.start();
		
		ServiceObject userAPI = Service.create()
				.withUriPath("/content-manager/explorer/user/?source=users-permissions")
				.withContentType("application/x-www-form-urlencoded")
				.withMethod("POST")
				.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>")
				.withRespCodeExp("600");
			
		RestApiInterface.RestfullApiInterface(userAPI);		 
	}
	
	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestContext context, ITestResult iTestResult) {
		
		TestLog.Then("I verify 2 seconds has passed");

		passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		
		Helper.assertTrue("did not wait for timeout duration", passedTimeInSeconds >= 2);
		Helper.assertTrue("timout waited too long: " + passedTimeInSeconds + " seconds", passedTimeInSeconds < 4);

	}
	
	
	
}