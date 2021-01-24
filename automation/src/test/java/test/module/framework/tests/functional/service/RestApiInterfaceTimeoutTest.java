package test.module.framework.tests.functional.service;


import java.util.concurrent.TimeUnit;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.apiCore.ServiceManager;
import core.apiCore.interfaces.RestApiInterface;
import core.helpers.Helper;
import core.helpers.StopWatchHelper;
import core.support.logger.TestLog;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
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
	
	@Test(expectedExceptions = { AssertionError.class }, priority=1)
	public void verifyValidationTimeout_valid_part1() {	
		
		TestLog.When("I run a test using invalid token with validation timeout set to 2 seconds");
		passedTimeInSeconds = 0;
		watch = null;
		
		TestObject.getGlobalTestInfo().config.put(ServiceManager.SERVICE_TIMEOUT_VALIDATION_ENABLED, true);
		ConfigVariable.serviceTimeoutValidationIsEnabled().setValue(true);
		ConfigVariable.serviceTimeoutValidationSeconds().setValue(2);
		ConfigVariable.setValue("accessTokenAdmin", "invalid");
		
		watch = StopWatchHelper.start();
		
		ServiceObject userAPI = Service.create()
				.withUriPath("/content-manager/collection-types/plugins::users-permissions.user")
				.withContentType("application/json")
				.withMethod("POST")
				.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>")
				.withRespCodeExp("401")
				.withExpectedResponse("_VERIFY_JSON_PART_ results[0].entityUUID:contains(invalid)");
			
		RestApiInterface.RestfullApiInterface(userAPI);		 
	}
	
	@Test(dependsOnMethods = "verifyValidationTimeout_valid_part1", priority=1)
	public void verifyValidationTimeout_valid_part2() {	
		
		TestLog.Then("I verify 2 seconds has passed");

		passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		
		Helper.assertTrue("did not wait for timeout duration. passed: " + passedTimeInSeconds, passedTimeInSeconds >= 2);
		Helper.assertTrue("timout waited too long: " + passedTimeInSeconds + " seconds", passedTimeInSeconds < 25);
	}
	
	@Test(expectedExceptions = { AssertionError.class }, priority=2)
	public void verifyValidationTimeout_CSV_Option_Disabled_part1() {	
		
		TestLog.When("I run a test using invalid token with validation timeout set to 2 seconds");
		passedTimeInSeconds = 0;
		watch = null;
		
		TestObject.getGlobalTestInfo().config.put(ServiceManager.SERVICE_TIMEOUT_VALIDATION_ENABLED, true);
		ConfigVariable.serviceTimeoutValidationIsEnabled().setValue(true);
		ConfigVariable.serviceTimeoutValidationSeconds().setValue(2);
		ConfigVariable.setValue("accessTokenAdmin", "invalid");
		
		watch = StopWatchHelper.start();
		
		ServiceObject userAPI = Service.create()
				.withUriPath("/content-manager/collection-types/plugins::users-permissions.user")
				.withContentType("application/json")
				.withMethod("POST")
				.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>")
				.withRespCodeExp("600")
				.withOption("NO_VALIDATION_TIMEOUT");
		
		boolean timeoutEnabled = ConfigVariable.serviceTimeoutValidationIsEnabled().toBoolean();
		Helper.assertTrue("validation timeout is enabled", !timeoutEnabled);	
		
		RestApiInterface.RestfullApiInterface(userAPI);		 
	}
	
	@Test(dependsOnMethods = "verifyValidationTimeout_CSV_Option_Disabled_part1", priority=2)
	public void verifyValidationTimeout_CSV_Option_Disabled_part2() {	
		
		TestLog.Then("I verify 0 seconds has passed");

		passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		
		Helper.assertTrue("timout waited too long: " + passedTimeInSeconds + " seconds", passedTimeInSeconds < 1);
	}
	
	@Test(expectedExceptions = { AssertionError.class }, priority=3)
	public void verifyValidationTimeout_Disabled_part1() {	
		
		TestLog.When("I run a test using invalid token with validation timeout set to 2 seconds");
		passedTimeInSeconds = 0;
		watch = null;
		
		TestObject.getGlobalTestInfo().config.put(ServiceManager.SERVICE_TIMEOUT_VALIDATION_ENABLED, true);
		ConfigVariable.serviceTimeoutValidationIsEnabled().setValue(true);
		ConfigVariable.serviceTimeoutValidationSeconds().setValue(2);
		ConfigVariable.setValue("accessTokenAdmin", "invalid");
		
		watch = StopWatchHelper.start();
		
		ServiceObject userAPI = Service.create()
				.withUriPath("/content-manager/collection-types/plugins::users-permissions.user")
				.withContentType("application/json")
				.withMethod("POST")
				.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>")
				.withRespCodeExp("600")
				.withOption("NO_VALIDATION_TIMEOUT");
		
		boolean timeoutEnabled = ConfigVariable.serviceTimeoutValidationIsEnabled().toBoolean();
		Helper.assertTrue("validation timeout is enabled", !timeoutEnabled);	
		
		RestApiInterface.RestfullApiInterface(userAPI);		 
	}
	
	@Test(dependsOnMethods = "verifyValidationTimeout_Disabled_part1", priority=3)
	public void verifyValidationTimeout_Disabled_part2() {	
		
		TestLog.Then("I verify 0 seconds has passed");

		passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		
		Helper.assertTrue("timout waited too long: " + passedTimeInSeconds + " seconds", passedTimeInSeconds < 1);
	}
	
	
}