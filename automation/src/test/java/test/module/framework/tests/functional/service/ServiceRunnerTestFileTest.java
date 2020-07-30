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
 *
 */
public class ServiceRunnerTestFileTest extends TestBase {
	
	AtomicInteger testCount = new AtomicInteger(0);

	@BeforeClass
	public void beforeClass()  {
		ConfigVariable.apiTestCaseFile().setValue("TestCases_UserValidation.csv");
		TestDataProvider.csvFileIndex.set(0);

	}
	
	// verifying test retry on api runner
	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void verifyApiRunner_TestCaseFile(Object objects) throws Exception {
		
		
		TestLog.When("I verify api runner with specified csv file");
 
		// we ignore validation errors, just care about test count
		// validation error occurs cause previous test: serviceRunnerTestCaseTest uses the same test case name to create user
		try {
			ServiceRunner.TestRunner(objects);
		}catch(AssertionError e ) {
			e.getMessage();
		}
    	
    	testCount.incrementAndGet(); 	
	}
	
	@AfterClass
	public void afterClass()  {	
		Helper.assertEquals(5, testCount.get());
	}	
}