package test.module.service;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import test.module.ModuleBase;

/**
 * Add additional before/after method/class/suite for the test project
 */
public class TestBase extends ModuleBase {

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