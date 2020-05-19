package test.module.framework.tests.ui;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.helpers.Helper;
import core.support.logger.TestLog;
import core.support.objects.TestObject;
import core.uiCore.drivers.AbstractDriver;
import data.Data;
import data.framework.User;
import data.framework.UserInvalid;
import module.framework.panel.MainPanel;
import test.module.framework.TestBase;

/**
 * 
 * @author ehsan matean
 *
 */
public class SingleSignInTest extends TestBase {

	@BeforeClass
	public void beforeClass() {
		ConfigVariable.globalIsSingleSignIn().setValue(true);
	}
	

	@Test(description = "verify if drivers are passed to next test when single sign in is enabled", priority=1)
	public void verifyDriverQuiteTestPart1() throws Exception {
		
		TestLog.When("I start 2 webdrivers and store them in webDriverList");
		setupWebDriver(app.framework.getWebDriver());
		setupWebDriver(app.framework.getWebDriver());

		TestLog.When("I test 2 drivers are available");
		List<WebDriver> drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(2, drivers.size());
		
		TestLog.Then("I test quite 1 and should have 1 driver left");
		Helper.quitCurrentDriver();
		drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(1, drivers.size());
		
		TestLog.Then("I start another driver and have 2 available");
		setupWebDriver(app.framework.getWebDriver());
		drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(2, drivers.size());
		
		WebDriver activeDriver = AbstractDriver.getWebDriver();
		Helper.assertTrue("active driver is null", activeDriver != null);
	}

	@Test(dependsOnMethods = "verifyDriverQuiteTestPart1", priority=1)
	public void verifyDriverQuiteTestPart2() throws Exception {
		
		setupWebDriver(app.framework.getWebDriver());
		
		TestLog.When("I test drivers are passed to the next test");
		List<WebDriver> drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(1, drivers.size());

		TestLog.Then("I quit all drivers");
		Helper.quitAllCurrentTestDrivers();
		drivers = TestObject.getTestInfo().webDriverList;
		Helper.assertEquals(0, drivers.size());
	}
	
	@Test(description = "verify if different user will result in quitting driver and loging as new user", priority=2)
	public void verifyDifferentUserPart1() throws Exception {
		setupWebDriver(app.framework.getWebDriver());

		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
				
		TestLog.Then("I verify admin logo is displayed");
		Helper.verifyElementIsDisplayed(MainPanel.ADMIN_LOGO);
	}
	
	@Test(dependsOnMethods = "verifyDifferentUserPart1", priority=2)
	public void verifyDifferentUserPart2() throws Exception {
		setupWebDriver(app.framework.getWebDriver());

		UserInvalid user = Data.framework.userinvalid().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginError(user);
	}
	
	@Test(description = "verify if same user will result in using same browser", priority=3)
	public void verifySameUserPart1() throws Exception {
		setupWebDriver(app.framework.getWebDriver());

		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
				
		TestLog.Then("I verify admin logo is displayed");
		Helper.verifyElementIsDisplayed(MainPanel.ADMIN_LOGO);
		Helper.verifyElementIsDisplayed(MainPanel.ADMIN_LOGO);

	}
	
	@Test(dependsOnMethods = "verifySameUserPart1", priority=3)
	public void verifySameUserPart2() throws Exception {
		setupWebDriver(app.framework.getWebDriver());
		
		Helper.refreshPage();
		
		TestLog.Then("I verify admin logo is displayed");
		Helper.verifyElementIsDisplayed(MainPanel.ADMIN_LOGO);

		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
		
		TestLog.Then("I verify admin logo is displayed");
		Helper.verifyElementIsDisplayed(MainPanel.ADMIN_LOGO);
	}
}