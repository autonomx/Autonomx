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
public class ServiceRunnerTestStepsTest extends TestBase {
	
	AtomicInteger testStepCount = new AtomicInteger(0);

	@BeforeClass
	public void beforeClass()  {
		ConfigVariable.apiParallelTestcasePath().setValue("../apiTestData/testCases/frameworkTests/testStep/");

		ConfigVariable.apiTestCaseFile().setValue("TestCases_UserValidation_step.csv");
	}
	
	// verifying test retry on api runner
	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void verifyApiRunner_TestStep(Object objects) throws Exception {
		
		
		TestLog.When("I verify api runner with specified csv file");
 
    	ServiceRunner.TestRunner(objects);
    	testStepCount.incrementAndGet(); 	
	}
	
	@AfterClass
	public void afterClass()  {	
		Helper.assertEquals(4, testStepCount.get());
	}	
}