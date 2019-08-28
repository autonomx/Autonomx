package test.module.framework.tests;


import java.lang.reflect.Method;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.helpers.Helper;
import core.support.logger.TestLog;
import test.module.web.TestBase;

/*
 * prerequisite:
 * global.timeout.seconds should not be 90
 * appium.logging = false
 * appium.logging.level = info 
 */
public class ConfigTest extends TestBase {
	
	@BeforeSuite
	public void beforeSuite()  {
		ConfigVariable.setValue("beforeSuiteValue", true);
		ConfigVariable.setValue("beforeSuiteOverride", 15);
		ConfigVariable.globalTimeoutSeconds().setValue(90);
	}
	
	@BeforeClass
	public void beforeClass()  {
		ConfigVariable.setValue("beforeClassValue", true);
		ConfigVariable.appiumLogging().setValue(true);
		ConfigVariable.setValue("beforeSuiteOverride", 25);
	}

	@BeforeMethod()
	public void beforeMethod(Method method) {
		ConfigVariable.setValue("beforeMethodValue", true);
		ConfigVariable.appiumLogginLevel().setValue("debug");
	}
	
	@AfterClass
	public void afterClass()  {
		
		TestLog.When("I test test method config values are not passed to after class");
		String beforeMethodValue2 = ConfigVariable.appiumLogginLevel().toString();
		Helper.assertEquals("info", beforeMethodValue2);
		
		boolean beforeClassValue = ConfigVariable.getBooleanValue("beforeClassValue");
		Helper.assertTrue("before class config value not set", beforeClassValue);
		
		TestLog.Then("I test before suite values are passed to after class");
		boolean beforeSuiteValue = ConfigVariable.getBooleanValue("beforeSuiteValue");
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
		boolean beforeSuiteValue = ConfigVariable.getBooleanValue("beforeSuiteValue");
		Helper.assertTrue("before suite config value not set", beforeSuiteValue);

		int beforeSuiteValue2 = ConfigVariable.globalTimeoutSeconds().toInt();
		Helper.assertEquals(90, beforeSuiteValue2);
	}
	
	@Test
	public void configTestSetWithString() {
	
		TestLog.When("I test setting config value with string");
		ConfigVariable.globalRetryCount().setValue("3");
		int globalRetryCount = ConfigVariable.globalRetryCount().toInt();
		Helper.assertEquals(3, globalRetryCount);
	}
	
	@Test
	public void configTestSetWithInt() {
	
		TestLog.When("I test setting config value with int");
		ConfigVariable.globalRetryCount().setValue(2);
		int globalRetryCount = ConfigVariable.globalRetryCount().toInt();
		Helper.assertEquals(2, globalRetryCount);
	}
	
	@Test
	public void configTestWithBooleanTrue() {

		TestLog.When("I test getting boolean config value set to true");
		ConfigVariable.webElementHighlightEnable().setValue("true");
		boolean reportEnableDetailedReport = ConfigVariable.reportEnableDetailedReport().toBoolean();
		Helper.assertEquals(true, reportEnableDetailedReport);
	}
	
	@Test
	public void configTestBooleanWithFalse() {
		
		TestLog.When("I test getting boolean config value set to false");
		ConfigVariable.webElementHighlightEnable().setValue("true");
	    boolean reportEnableDetailedReport = ConfigVariable.reportEnableDetailedReport().toBoolean();
		Helper.assertEquals(true, reportEnableDetailedReport);	
	}
	
	@Test
	public void configTestSetConfigValue() {
		
		TestLog.When("I test setting config values");
		ConfigVariable.setValue("testKey", "3");
		ConfigVariable.setValue("testkey2", true);
		
		String value = ConfigVariable.getStringValue("testKey");
		Helper.assertEquals("3", value);	
		
		int intValue = ConfigVariable.getIntegerValue("testKey");
		Helper.assertEquals(3, intValue);	
		
		boolean booleanValue = ConfigVariable.getBooleanValue("testkey2");
		Helper.assertEquals(true, booleanValue);	
	}
	
	@Test(description = "verify new config values are passed down from suite, class to test methods level")
	public void verifyHierarchyAssignmentCreated() {
	
		TestLog.When("I test setting new config values at different scope levels");

		boolean beforeMethodValue = ConfigVariable.getBooleanValue("beforeMethodValue");
		boolean beforeClassValue = ConfigVariable.getBooleanValue("beforeClassValue");
		boolean beforeSuiteValue = ConfigVariable.getBooleanValue("beforeSuiteValue");
		
		int beforeSuiteOverride = ConfigVariable.getIntegerValue("beforeSuiteOverride");

		Helper.assertTrue("before method config value not set", beforeMethodValue);
		Helper.assertTrue("before class config value not set", beforeClassValue);
		Helper.assertTrue("before suite config value not set", beforeSuiteValue);
		Helper.assertEquals(25, beforeSuiteOverride);
	}
	
	@Test(description = "verify generated config values are passed down from suite, class to test methods level")
	public void verifyHierarchyAssignmentGenerated() {
	
		TestLog.When("I test updating config values at different scope levels");
		
		String beforeMethodValue2 = ConfigVariable.appiumLogginLevel().toString();
		boolean beforeClassValue2 = ConfigVariable.appiumLogging().toBoolean();
		int beforeSuiteValue2 = ConfigVariable.globalTimeoutSeconds().toInt();

		Helper.assertEquals("debug", beforeMethodValue2);
		Helper.assertTrue("before class config value should be true", beforeClassValue2);
		Helper.assertEquals(90, beforeSuiteValue2);
	}
}