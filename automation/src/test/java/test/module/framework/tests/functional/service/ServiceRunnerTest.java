package test.module.framework.tests.functional.service;


import java.lang.reflect.Method;

import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import core.apiCore.driver.ApiTestDriver;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
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
        
		ServiceRunner.TestRunner("suite1", "test1valid", "Y", "", "RESTfulAPI", "/auth/local", "application/json", "POST",
				"", "", "", requestBody, OutputParams, "200", "",
				"", "TestCases_UserValidation.csv", "1:1", "service", "");
		
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
        
		ServiceRunner.TestRunner("suite1", "invalid_responseCode", "Y", "", "RESTfulAPI", "/auth/local", "application/json", "POST",
				"", "", "", requestBody, OutputParams, "320", "",
				"", "TestCases_UserValidation.csv", "1:1", "service", "");
	}
	
	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void verifyApiRunner_dataProvider_valid(String TestSuite, String TestCaseID, String RunFlag, String Description,
			String InterfaceType, String UriPath, String ContentType, String Method, String Option,
			String RequestHeaders, String TemplateFile, String RequestBody, String OutputParams, String RespCodeExp,
			String ExpectedResponse, String TcComments,
			String tcName, String tcIndex, String testType, Object serviceSteps) throws Exception {
		
		String testname = AbstractDriverTestNG.testName.get();
		String testClass = ApiTestDriver.getTestClass(tcName);
		Helper.assertEquals(testClass + "-" + TestCaseID, testname);

		ServiceRunner.TestRunner(TestSuite, TestCaseID, RunFlag, Description, InterfaceType, UriPath, ContentType, Method,
				Option, RequestHeaders, TemplateFile, RequestBody, OutputParams, RespCodeExp, ExpectedResponse,
				TcComments, tcName, tcIndex, testType, serviceSteps);
	}
	
	 @Test()
 	public void setServiceTestName_valid() {
		 
		 TestLog.And("I verify service test name with valid values");		 
		 Object[] testData = new Object[]{"","user","","","","","","","","","","","","","","","TestCases_valididateUser.csv","0","service",""};
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
	 
	@Test()
	public void evaluateRequestbody_with_backlash()  {	
		String requestBody = "{\n" + 
				"    \"partyBaseUri\": \"<@partyBaseUri>\",\n" + 
				"    \"locationBaseUri\": \"<@locationBaseUri>\",\n" + 
				"    \"csv\": \"Adrian Young,Adrian.Young@abb.com,\\\"-123.113918, 49.261100\\\",\\\"-123.113918, 49.261100\\\",Vancouver City Hall\\nAudrey May,Audrey.May@abb.com,\\\"-123.130507, 49.299778\\\",\\\"-123.130507, 49.299778\\\",Vancouver Aquarium\\nAustin Hughes,Austin.Hughes@abb.com,\\\"-123.179996, 49.194356\\\",\\\"-123.179996, 49.194356\\\",Vancouver Intl Airport\\nBenjamin Wilson,Benjamin.Wilson@abb.com,\\\"-123.118136, 49.282487\\\",\\\"-123.118136, 49.282487\\\",Vancouver City Center\"\n" + 
				"}";
		requestBody = ServiceObject.normalize(requestBody);
		ServiceObject serviceObject = new ServiceObject().withRequestBody(requestBody);
		
		Helper.assertEquals(requestBody, serviceObject.getRequestBody());	
	}
	
	// test1 will fail, test2 depends on test1. test2 will be skipped
	@Test(expectedExceptions = SkipException.class)
	public void verifyApiRunner_depends_on_test() throws Exception {
		
		TestLog.When("I verify api runner test");
        String requestBody = "{\n" + 
        		"\"identifier\": \"<@adminUserName>\",\n" + 
        		"\"password\": \"<@adminUserPassword>\"\n" + 
        		"}";
        String OutputParams = "user.role.id:<$roles>; jwt:<$accessTokenAdmin>;\n" + 
        		"user.id:<$userId>";
        
        try {
		ServiceRunner.TestRunner("suite1", "test1valid", "Y", "", "RESTfulAPI", "/auth/local", "application/json", "POST",
				"", "", "", requestBody, OutputParams, "600", "",
				"", "TestCases_DependsOn.csv", "1:1", "service", "");
        }catch(AssertionError e) {
        	e.getMessage();
        }
        
        // simulate an error
        TestObject.getTestInfo().caughtThrowable = new Throwable();
        
		// if service test, parent test objects keeps track of the child test objects
		ApiTestDriver.parentTrackChildTests();
        
		ServiceRunner.TestRunner("suite1", "test1valid", "Y", "", "RESTfulAPI", "/auth/local", "application/json", "POST",
				"DEPENDS_ON_TEST:test1valid", "", "", requestBody, OutputParams, "200", "",
				"", "TestCases_DependsOn.csv", "1:1", "service", "");
	}
	
}