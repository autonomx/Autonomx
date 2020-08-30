package test.module.framework.tests.functional.service;


import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import core.apiCore.helpers.CsvReader;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 * Test to verify running single test case
 */
public class ServiceRunnerTestRangeTest extends TestBase {
	
	AtomicInteger testCount = new AtomicInteger(0);

	@BeforeClass
	public void beforeClass()  {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/");

		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv:createUser-createUserNoToken, createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "");
	}
	
	// verifying test retry on api runner
	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void verifyApiRunner_TestCaseFile(Object objects) throws Exception {
		
		
		TestLog.When("I verify api runner with specified csv file");
 
    	testCount.incrementAndGet(); 	
	}
	
	@AfterClass
	public void afterClass()  {	
		Helper.assertEquals(3, testCount.get());
	}	
}