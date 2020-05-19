package test.module.android;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import moduleManager.module.ModuleBase;

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
	}

	@AfterClass(alwaysRun = true)
	public void afterClass(ITestContext context) {
	}
	
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() {
	}

	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
	}
}