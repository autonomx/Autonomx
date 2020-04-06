package test.module.framework.tests.functional.service;


import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.apiCore.TestDataProvider;
import core.helpers.Helper;
import core.support.logger.TestLog;
import serviceManager.ServiceRunner;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 * Test to verify running single test case
 */
public class ServiceRunnerTestCaseTest extends TestBase {
	
	AtomicInteger testCount = new AtomicInteger(0);

	@BeforeClass
	public void beforeClass()  {
		ConfigVariable.apiTestCaseFile().setValue("TestCases_UserValidation.csv");
		ConfigVariable.apiTestCase().setValue("createUserNoToken");
	}
	
	// verifying test retry on api runner
	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void verifyApiRunner_TestCaseFile(Object objects) throws Exception {
		
		
		TestLog.When("I verify api runner with specified csv file");
 
    	ServiceRunner.TestRunner(objects);
    	testCount.incrementAndGet(); 	
	}
	
	@AfterClass
	public void afterClass()  {	
		Helper.assertEquals(1, testCount.get());
	}	
}