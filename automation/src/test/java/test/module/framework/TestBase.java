package test.module.framework;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import configManager.ConfigVariable;
import core.apiCore.TestDataProvider;
import core.support.logger.TestLog;
import data.Data;
import module.common.data.CommonUser;
import test.module.ModuleBase;

/**
 * Add additional before/after method/class/suite for the test project
 */
public class TestBase extends ModuleBase {
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestContext context, ITestResult iTestResult) {
	}


	@BeforeClass(alwaysRun = true)
	public void beforeClass(ITestContext context) {
		// setting the csv index value to 0, since multiple service runners are executing in this test suite
		// normal case, only 1 service runner is executed per test suite
		TestDataProvider.csvFileIndex.set(0);
		
	}

	@AfterClass(alwaysRun = true)
	public void afterClass(ITestContext context) {
	}
	
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
		ConfigVariable.apiParallelTestcasePath().setValue("../apiTestData/testCases/frameworkTests/");
	   //ConfigVariable.webBrowserType().setValue("CHROME_HEADLESS");
		
		TestLog.When("I login with admin user");
		CommonUser user = Data.common.commonuser().withAdminLogin();
		app.serviceUiIntegration.login.login(user);
		
		// for apiRunnerConfigTest
		ConfigVariable.setValue("ServiceRunnerConfigTest-beforeSuiteValue", true);
		ConfigVariable.setValue("ServiceRunnerConfigTest-beforeSuiteOverride", 15);
		ConfigVariable.globalTimeoutSeconds().setValue(90);
		
		// CofigTest
		ConfigVariable.setValue("beforeSuiteValue", true);
		ConfigVariable.setValue("beforeSuiteOverride", 15);
		ConfigVariable.globalTimeoutSeconds().setValue(90);
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
	}
}