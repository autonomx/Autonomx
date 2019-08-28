package test.module.framework.tests;

import java.lang.reflect.Method;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.helpers.Helper;
import core.support.logger.TestLog;
import core.support.objects.TestObject;
import core.uiCore.drivers.AbstractDriver;
import test.module.framework.TestBase;

/**
 * 
 * @author ehsan matean
 *
 */
public class singleSignInTest extends TestBase {

	@BeforeClass
	public void beforeClass() {
		ConfigVariable.globalIsSingleSignIn().setValue(true);
		ConfigVariable.globalParallelTestCount().setValue(1);

	}
	
	@BeforeMethod()
	public void beforeMethod(Method method) throws Exception {

	}

	@Test(description = "verify if drivers are passed to next test when single sign in is enabled")
	public void verifyDriverQuiteTestPart1() throws Exception {
		
		TestLog.When("I start 2 webdrivers and store them in webDriverList");
		setupWebDriver(app.webApp.getWebDriver());
		setupWebDriver(app.webApp.getWebDriver());

		TestLog.When("I test 2 drivers are available");
		List<WebDriver> drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(2, drivers.size());
		
		TestLog.Then("I test quite 1 and should have 1 driver left");
		Helper.quitCurrentDriver();
		drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(1, drivers.size());
		
		TestLog.Then("I start another driver and have 2 available");
		setupWebDriver(app.webApp.getWebDriver());
		drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(2, drivers.size());
		
		WebDriver activeDriver = AbstractDriver.getWebDriver();
		Helper.assertTrue("active driver is null", activeDriver != null);
	}

	@Test(dependsOnMethods = "verifyDriverQuiteTestPart1")
	public void verifySingleSignInDriverQuiteTestPart2() throws Exception {
		
		setupWebDriver(app.webApp.getWebDriver());
		
		TestLog.When("I test drivers are passed to the next test");
		List<WebDriver> drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(1, drivers.size());

		TestLog.Then("I quit all drivers");
		Helper.quitAllCurrentTestDrivers();
		drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(0, drivers.size());
	}
}