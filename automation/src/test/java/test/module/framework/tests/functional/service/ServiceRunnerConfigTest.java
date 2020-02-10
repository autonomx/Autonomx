package test.module.framework.tests.functional.service;


import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
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
public class ServiceRunnerConfigTest extends TestBase {
	
	@BeforeClass
	public void beforeClass()  {
		ConfigVariable.setValue("ServiceRunnerConfigTest-beforeClassValue", true);
		ConfigVariable.appiumLogging().setValue(true);
		ConfigVariable.setValue("ServiceRunnerConfigTest-beforeSuiteOverride", 25);
		
		ConfigVariable.setValue("accessTokenAdmin", "invalid");
		
		TestLog.Then("I test before suite values are passed to after suite");
		boolean beforeSuiteValue = ConfigVariable.getBooleanValue("ServiceRunnerConfigTest-beforeSuiteValue");
		Helper.assertTrue("before suite config value not set", beforeSuiteValue);
	}
	
	// admin user is set to "invalid" from before suite, tests are expected to fail
	@Test(expectedExceptions = { AssertionError.class }, dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void verifyApiRunner_dataProvider_valid(String TestSuite, String TestCaseID, String RunFlag, String Description,
			String InterfaceType, String UriPath, String ContentType, String Method, String Option,
			String RequestHeaders, String TemplateFile, String RequestBody, String OutputParams, String RespCodeExp,
			String ExpectedResponse, String TcComments,
			String tcName, String tcIndex, String testType, Object serviceSteps) throws Exception {
		
		String username = ConfigVariable.getStringValue("adminUserName");
		Helper.assertEquals("accessTokenAdmin", username);
		
		if(RequestHeaders.equals("NO_TOKEN"))Helper.assertFalse("failed cause no token will fail this test, hence needs to be overwritten");

		ServiceRunner.TestRunner(TestSuite, TestCaseID, RunFlag, Description, InterfaceType, UriPath, ContentType, Method,
				Option, RequestHeaders, TemplateFile, RequestBody, OutputParams, RespCodeExp, ExpectedResponse,
				TcComments, tcName, tcIndex, testType, serviceSteps);
	}
	
	@AfterClass
	public void afterClass()  {	
		
		boolean beforeClassValue = ConfigVariable.getBooleanValue("ServiceRunnerConfigTest-beforeClassValue");
		Helper.assertTrue("before class config value not set", beforeClassValue);
		
		TestLog.Then("I test before suite values are passed to after class");
		boolean beforeSuiteValue = ConfigVariable.getBooleanValue("ServiceRunnerConfigTest-beforeSuiteValue");
		Helper.assertTrue("before suite config value not set", beforeSuiteValue);

		int beforeSuiteValue2 = ConfigVariable.globalTimeoutSeconds().toInt();
		Helper.assertEquals(90, beforeSuiteValue2);
	}
	
	@AfterSuite
	public void afterSuite()  {
		
		TestLog.When("I test before class config values are not passed to after suite");
		boolean beforeClassValue = ConfigVariable.appiumLogging().toBoolean();
		Helper.assertTrue("before class config value should be false", !beforeClassValue);
		
		TestLog.When("I test test method config values are not passed to after suite");
		String beforeMethodValue2 = ConfigVariable.appiumLogginLevel().toString();
		Helper.assertEquals("info", beforeMethodValue2);
		
		TestLog.Then("I test before suite values are passed to after suite");
		boolean beforeSuiteValue = ConfigVariable.getBooleanValue("ServiceRunnerConfigTest-beforeSuiteValue");
		Helper.assertTrue("before suite config value not set", beforeSuiteValue);

		int beforeSuiteValue2 = ConfigVariable.globalTimeoutSeconds().toInt();
		Helper.assertEquals(90, beforeSuiteValue2);
	}
	
	
}