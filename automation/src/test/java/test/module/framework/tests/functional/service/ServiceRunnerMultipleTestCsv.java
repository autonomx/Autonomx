package test.module.framework.tests.functional.service;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.configReader.PropertiesReader;
import core.support.logger.TestLog;
import serviceManager.ServiceRunner;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ServiceRunnerMultipleTestCsv extends TestBase {
	
	AtomicInteger testCount = new AtomicInteger(0);
	
	String csvTestPath = PropertiesReader.getLocalRootPath()
			+ Config.getValue(TestDataProvider.TEST_DATA_PARALLEL_PATH) +  "TestCases_UserValidation.csv";

	String csvTestPath_destination = PropertiesReader.getLocalRootPath()
			+ Config.getValue(TestDataProvider.TEST_DATA_PARALLEL_PATH) + "TestCases_UserValidation2.csv";


	@BeforeClass
	public void beforeClass() throws IOException  {		
		TestLog.When("I create additional csv test file");
		TestDataProvider.csvFileIndex.set(0);
		Files.copy(new File(csvTestPath).toPath(),new File(csvTestPath_destination).toPath(), StandardCopyOption.REPLACE_EXISTING);
	}
	
	// verifying test retry on api runner
	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 2)
	public void verifyApiRunner_TestCaseFile(String TestSuite, String TestCaseID, String RunFlag, String Description,
			String InterfaceType, String UriPath, String ContentType, String Method, String Option,
			String RequestHeaders, String TemplateFile, String RequestBody, String OutputParams, String RespCodeExp,
			String ExpectedResponse, String TcComments,
			String tcName, String tcIndex, String testType) throws Exception {
		
		
		TestLog.When("I verify api runner with specified csv file");
 
    	ServiceRunner.TestRunner(TestSuite, TestCaseID, RunFlag, Description, InterfaceType, UriPath, ContentType, Method,
    				Option, RequestHeaders, TemplateFile, RequestBody, OutputParams, RespCodeExp, ExpectedResponse,
    				TcComments, tcName, tcIndex, testType);
    	testCount.incrementAndGet(); 	
	}
	
	@AfterClass
	public void afterClass() throws IOException  {	
		TestLog.Then("I verify tests from the csv files have passed successfuly ");

		Helper.assertEquals(10, testCount.get());
		Files.delete(new File(csvTestPath_destination).toPath());
	}	
}