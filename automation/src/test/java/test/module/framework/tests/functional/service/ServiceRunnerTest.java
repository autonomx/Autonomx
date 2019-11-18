package test.module.framework.tests.functional.service;


import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import core.apiCore.driver.ApiTestDriver;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.uiCore.drivers.AbstractDriverTestNG;
import serviceManager.ServiceRunner;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ServiceRunnerTest extends TestBase {
	
	
	@BeforeMethod()
	public void beforeMethod(Method method) {
		TestDataProvider.csvFileIndex.set(0);
	}
	
	@Test()
	public void verifyApiRunner_valid() throws Exception {
		
		TestLog.When("I verify api runner test");
        String requestBody = "{\n" + 
        		"\"identifier\": \"<@adminUserName>\",\n" + 
        		"\"password\": \"<@adminUserPassword>\"\n" + 
        		"}";
        String OutputParams = "user.role.id:<$roles>; jwt:<$accessTokenAdmin>;\n" + 
        		"user.id:<$userId>";
        
		ServiceRunner.TestRunner("suite1", "test1", "Y", "", "RESTfulAPI", "/auth/local", "application/json", "POST",
				"", "", "", requestBody, OutputParams, "200", "",
				"", "TestCases_UserValidation.csv", "1:1", "service");
		
		String roles = Config.getValue("roles");
		Helper.assertEquals(roles, "1");
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void verifyApiRunner_invalid_responseCode() throws Exception {
		
		TestLog.When("I verify api runner test");
        String requestBody = "{\n" + 
        		"\"identifier\": \"<@adminUserName>\",\n" + 
        		"\"password\": \"<@adminUserPassword>\"\n" + 
        		"}";
        String OutputParams = "user.role.id:<$roles>; jwt:<$accessTokenAdmin>;\n" + 
        		"user.id:<$userId>";
        
		ServiceRunner.TestRunner("suite1", "test1", "Y", "", "RESTfulAPI", "/auth/local", "application/json", "POST",
				"", "", "", requestBody, OutputParams, "300", "",
				"", "TestCases_UserValidation.csv", "1:1", "service");
	}
	
	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void verifyApiRunner_dataProvider_valid(String TestSuite, String TestCaseID, String RunFlag, String Description,
			String InterfaceType, String UriPath, String ContentType, String Method, String Option,
			String RequestHeaders, String TemplateFile, String RequestBody, String OutputParams, String RespCodeExp,
			String ExpectedResponse, String TcComments,
			String tcName, String tcIndex, String testType) throws Exception {
		
		String testname = AbstractDriverTestNG.testName.get();
		String testClass = ApiTestDriver.getTestClass(tcName);
		Helper.assertEquals(testClass + "-" + TestCaseID, testname);

		ServiceRunner.TestRunner(TestSuite, TestCaseID, RunFlag, Description, InterfaceType, UriPath, ContentType, Method,
				Option, RequestHeaders, TemplateFile, RequestBody, OutputParams, RespCodeExp, ExpectedResponse,
				TcComments, tcName, tcIndex, testType);
	}
	
	 @Test()
 	public void setServiceTestName_valid() {
		 
		 TestLog.And("I verify service test name with valid values");		 
		 Object[] testData = new Object[]{"","user","","","","","","","","","","","","","","","TestCases_valididateUser.csv","0","service"};
		 ApiTestDriver.setServiceTestName(testData);
		 String testname = AbstractDriverTestNG.testName.get();
		 Helper.assertEquals("valididateUser-user", testname); 	
     }
	 
	 @Test()
 	public void setServiceTestName_invalid() {
		 
		 TestLog.When("I verify service test name if empty test data");
		 Object[] testData = new Object[0];
		 ApiTestDriver.setServiceTestName(testData);
		 String testname = AbstractDriverTestNG.testName.get();
		 Helper.assertEquals("ServiceRunnerTest-setServiceTestName_invalid", testname);
		 
		 TestLog.And("I verify service test name with empty test data of size 18");		 
		 testData = new Object[18];
		 ApiTestDriver.setServiceTestName(testData);
		 testname = AbstractDriverTestNG.testName.get();
		 Helper.assertEquals("ServiceRunnerTest-setServiceTestName_invalid", testname);
		 
		 TestLog.And("I verify service test name with empty test data of size 19");		 
		 testData = new Object[19];
		 ApiTestDriver.setServiceTestName(testData);
		 testname = AbstractDriverTestNG.testName.get();
		 Helper.assertEquals("ServiceRunnerTest-setServiceTestName_invalid", testname);
     }
}