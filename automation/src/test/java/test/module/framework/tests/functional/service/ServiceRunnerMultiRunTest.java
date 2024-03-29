package test.module.framework.tests.functional.service;


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
public class ServiceRunnerMultiRunTest extends TestBase {
	public static ThreadLocal<Integer> testCount = new ThreadLocal<Integer>(); // key for testObject

	@BeforeClass
	public void beforeClass()  {
		testCount.set(0);

		ConfigVariable.apiParallelTestcasePath().setValue("../apiTestData/testCases/frameworkTests/multirun/");

		ConfigVariable.apiTestCaseFile().setValue("TestCases_UserValidation_multirun.csv");
	}
	
	// verifying test retry on api runner
	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void verifyApiRunner_TestCaseFile(Object objects) throws Exception {
		
		
		TestLog.When("I verify api runner with specified csv file");
 
    	ServiceRunner.TestRunner(objects);
    	testCount.set(testCount.get() + 1); 	
	}
	
	@AfterClass
	public void afterClass()  {	
		Helper.assertEquals(12, testCount.get());
	}	
}