package test.module.framework.tests.functional.service;


import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.apiCore.ServiceManager;
import core.apiCore.TestDataProvider;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.TestObject;
import serviceManager.ServiceRunner;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ServiceBeforeClassSuiteTests extends TestBase {
	
	@BeforeClass
	public void beforeClass()  {
		Config.putValue("api.base.path", "../apiTestData/testCases/frameworkTests/testBase/");
		Config.putValue("api.base.after.testfile", "TestCases_RunAfter.csv");
		Config.putValue("api.base.before.testfile", "TestCases_RunBefore.csv");
	}
	
	@BeforeMethod()
	public void beforeMethod(Method method) {
		TestDataProvider.csvFileIndex.set(0);
	}
	
	@Test(priority=1)
	public void verifyTestbaseTest() throws Exception {
		
		
		Helper.assertEquals("../apiTestData/testCases/frameworkTests/testBase/", Config.getValue("api.base.path"));
		
		TestLog.When("I verify api runner test");
        String requestBody = "{\n" + 
        		"\"identifier\": \"<@adminUserName>\",\n" + 
        		"\"password\": \"<@adminUserPassword>\"\n" + 
        		"}";
        String OutputParams = "user.role.id:<$roles>; jwt:<$accessTokenAdmin>;\n" + 
        		"user.id:<$userId>";
        
        Object[] objects = {"suite1", "test-verifyTestbaseTest", "Y", "", "RESTfulAPI", "/auth/local", "application/json", "POST",
				"", "", "", requestBody, OutputParams, "200", "_VERIFY.JSON.PART_" + 
						"user.username:1: notEqualTo(<@rolesBeforeClass>);",
				"", "TestCases_UserValidation.csv", "0:1", "service", ""};
        
		ServiceRunner.TestRunner(objects);
		
		TestLog.Then("I verify before class values are accessible in test");
		String roles = TestObject.getTestInfo("UserValidation-test-verifyTestbaseTest").config.get("rolesBeforeClass").toString();
		Helper.assertEquals(roles, "1");
		
		// after class
		roles = Config.getValue("rolesBeforeClass");
		Helper.assertEquals(roles, "1");
		
		roles = Config.getValue("roles");
		Helper.assertEquals(roles, "1");
		
		TestObject.testInfo.remove("UserValidation-BeforeTestFile-RunBefore-getAdminTokenBeforeClass");
		TestObject.testInfo.remove("UserValidation-AfterTestFile-RunAfter-getAdminTokenAfterClass");

	}
	
	@Test(priority=2, description ="overriding before and after class to none, thus skipping before and after class")
	public void verifyTestbaseTest_override_set_none() throws Exception {
		
		
		Helper.assertEquals("../apiTestData/testCases/frameworkTests/testBase/", Config.getValue("api.base.path"));
		
		TestLog.When("I verify api runner test");
        String requestBody = "{\n" + 
        		"\"identifier\": \"<@adminUserName>\",\n" + 
        		"\"password\": \"<@adminUserPassword>\"\n" + 
        		"}";
        String OutputParams = "user.role.id:<$roles>; jwt:<$accessTokenAdmin>;\n" + 
        		"user.id:<$userId>";
        
        Object[] objects = {"suite1", "testBaseOverride", "Y", "", ServiceManager.EXTERNAL_INTERFACE, "", "", "beforeCsvFile:none;\n" + 
				"afterCsvFile:none;",
				"", "", "", "", "", "", "",
				"", "TestCases_UserValidation.csv", "0:2", "service", ""};
        
		ServiceRunner.TestRunner(objects);
		
		Helper.assertEquals("", Config.getValue(ServiceManager.TEST_BASE_BEFORE_CLASS));
		Helper.assertEquals("", Config.getValue(ServiceManager.TEST_BASE_AFTER_CLASS));

		Object[] objects2 = {"suite1", "verifyTestbaseTest", "Y", "", "RESTfulAPI", "/auth/local", "application/json", "POST",
				"", "", "", requestBody, OutputParams, "200", "_VERIFY.JSON.PART_" + 
						"user.username:1: notEqualTo(<@rolesBeforeClass>);",
				"", "TestCases_UserValidation.csv", "1:2", "service", ""};
		
		ServiceRunner.TestRunner(objects2);
		
		TestLog.Then("I verify before class and after class did not run");
		TestObject beforeClass = TestObject.testInfo.get("UserValidation-BeforeTestFile-RunBefore-getAdminTokenBeforeClass");
		TestObject afterClass = TestObject.testInfo.get("UserValidation-AfterTestFile-RunAfter-getAdminTokenAfterClass");

		Helper.assertTrue("before class was not suppose to run", beforeClass == null);
		Helper.assertTrue("after class was not suppose to run", afterClass == null);
	}
	
	@Test(priority=2, description ="overriding before and after class and setting test case id list")
	public void verifyTestbaseTest_override_set_testCaseId() throws Exception {
		
		// setting test base location
		Helper.assertEquals("../apiTestData/testCases/frameworkTests/testBase/", Config.getValue("api.base.path"));
		
		TestLog.When("I verify api runner test");
        String requestBody = "{\n" + 
        		"\"identifier\": \"<@adminUserName>\",\n" + 
        		"\"password\": \"<@adminUserPassword>\"\n" + 
        		"}";
        String OutputParams = "user.role.id:<$roles>; jwt:<$accessTokenAdmin>;\n" + 
        		"user.id:<$userId>";
        
        Object[] objects = {"suite1", "testBaseOverride", "Y", "", ServiceManager.EXTERNAL_INTERFACE, "", "","beforeCsvFile:TestCases_RunBefore.csv:createUserBeforeClass; afterCsvFile:TestCases_RunAfter.csv:getAdminTokenAfterClass;",
				"", "", "", "", "", "", "",
				"", "TestCases_UserValidation.csv", "0:2", "service", ""};
        
		ServiceRunner.TestRunner(objects);
		
		Helper.assertEquals("TestCases_RunBefore.csv:createUserBeforeClass", Config.getValue(ServiceManager.TEST_BASE_BEFORE_CLASS));
		Helper.assertEquals("TestCases_RunAfter.csv:getAdminTokenAfterClass", Config.getValue(ServiceManager.TEST_BASE_AFTER_CLASS));

		Object[] objects2 = {"suite1", "verifyTestbaseTest", "Y", "", "RESTfulAPI", "/auth/local", "application/json", "POST",
				"", "", "", requestBody, OutputParams, "200", "_VERIFY.JSON.PART_" + 
						"user.username:1: notEqualTo(<@rolesBeforeClass>);",
				"", "TestCases_UserValidation.csv", "1:2", "service", ""};
		
		ServiceRunner.TestRunner(objects2);
		
		TestLog.Then("I verify before class and after class did not run");
		TestObject beforeClass_getAdminTokenBeforeClass = TestObject.testInfo.get("UserValidation-BeforeTestFile-RunBefore-getAdminTokenBeforeClass");
		TestObject beforeClass_createUserBeforeClass = TestObject.testInfo.get("UserValidation-BeforeTestFile-RunBefore-createUserBeforeClass");

		TestObject afterClass = TestObject.testInfo.get("UserValidation-AfterTestFile-RunAfter-getAdminTokenAfterClass");

		// ensure's correct tests ran
		Helper.assertTrue("before class was not suppose to run", beforeClass_getAdminTokenBeforeClass == null);
		Helper.assertTrue("before class was suppose to run", beforeClass_createUserBeforeClass != null);

		Helper.assertTrue("after class was suppose to run", afterClass != null);
	}
}