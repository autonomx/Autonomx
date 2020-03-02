package test.module.framework.tests.functional;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import configManager.ConfigVariable;
import core.apiCore.interfaces.Authentication;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.TestObject;
import test.module.framework.TestBase;

/*
 * prerequisite:
 * global.timeout.seconds should not be 90
 * appium.logging = false
 * appium.logging.level = info 
 */
@SuppressWarnings("unchecked")
public class ConfigTest extends TestBase {
	
	@BeforeClass
	public void beforeClass()  {
		ConfigVariable.setValue("beforeClassValue", true);
		ConfigVariable.appiumLogging().setValue(true);
		ConfigVariable.setValue("beforeSuiteOverride", 25);
		
		boolean beforeSuiteValue = ConfigVariable.getBooleanValue("beforeSuiteValue");
		Helper.assertTrue("before suite config value not set", beforeSuiteValue);
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
	public void configInvalid() {
		String invalidValue = Config.getValue("invalid");
		Helper.assertEquals("", invalidValue);
		
		int invalidIntValue = Config.getIntValue("invalid");
		Helper.assertEquals(-1, invalidIntValue);
		
		boolean invalidBooleanValue = Config.getBooleanValue("invalid");
		Helper.assertEquals(false, invalidBooleanValue);
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
	
	@Test
	public void verifyConfigObject() {
		List<String> credentials = new ArrayList<String>();
		credentials.add("username1");
		credentials.add("password1");
		
		// store basic request in config
		Config.putValue(Authentication.BASIC_AUTHORIZATION, credentials);
		ArrayList<String> basicRequest = (ArrayList<String>) Config.getObjectValue(Authentication.BASIC_AUTHORIZATION);
	
		Helper.assertEquals("username1", basicRequest.get(0));
		Helper.assertEquals("password1", basicRequest.get(1));
	}
	
	@Test
	public void checkForDuplicateKeys_valid() {
		Multimap<String, String> multimap = ArrayListMultimap.create();

		multimap.put("1", "A");
		multimap.put("1", "B");
		multimap.put("1", "C");
		multimap.put("1", "A");
		multimap.put("2", "A");
		multimap.put("2", "B");
		multimap.put("2", "C");
		multimap.put("2", "A");
		
		TestObject.getTestInfo().configKeys.putAll(multimap);
		List<String> duplicates = Config.checkForDuplicateKeys();
		Helper.assertEquals(2, duplicates.size());
	}	
}