package test.module.framework.tests.functional;



import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import data.webApp.User;
import module.webApp.panel.LoginPanel;
import module.webApp.panel.MainPanel;
import test.module.web.TestBase;
/**
 * this test class demonstrates 3 data types: data object, csv data and data provider
 * @author ehsan matean
 *
 */

public class rerunFailedTests extends TestBase {
	
	public static int val = 1;

	@BeforeMethod
	public void beforeMethod() throws Exception {
		ConfigVariable.globalUiRerunFailedAfterSuite().setValue(true);
		ConfigVariable.globalRetryCount().setValue(0);
		setupWebDriver(app.webApp.getHybridDriver());
	}
	
	// NOTE: test needs to fail on first run to run in test suite
	// hence, it cant be added to test suite
	@Test()
	public void verifyAdminUserWithCsvData() {
		
		User user = Data.webApp.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		
		if(val==1) {
			val++;
			Helper.assertFalse("failed on purpose");
		}
	
		
		app.webApp.login.loginWithCsvData(user);
		
		TestLog.Then("I verify admin logo is displayed");
		Helper.verifyElementIsDisplayed(MainPanel.ADMIN_LOGO);
				
		TestLog.When("I logout");
		app.webApp.main.logout();

		TestLog.Then("I should see the login panel");
		Helper.verifyElementIsDisplayed(LoginPanel.LOGIN_SUBMIT);
		throw new RuntimeException();
	}
}