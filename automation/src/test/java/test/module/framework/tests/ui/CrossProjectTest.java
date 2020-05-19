package test.module.framework.tests.ui;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.autonomxData;
import data.webApp.User;
import module.webApp.panel.LoginPanel;
import module.webApp.panel.MainPanel;
import moduleManager.autonomxManager;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class CrossProjectTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.framework.getWebDriver());
	}
	
	@Test(description = "uses module manager and data class created through project.name")
	public void verifyCrossProjectManager() {
		
		autonomxManager sch = new autonomxManager();
		User user = autonomxData.webApp.user().admin();
		sch.webApp.login.loginWithCsvData(user);
					
		TestLog.Then("I verify admin logo is displayed");
		Helper.verifyElementIsDisplayed(MainPanel.ADMIN_LOGO);
		
		TestLog.When("I logout");
		app.webApp.main.logout();

		TestLog.Then("I should see the login panel");
		Helper.verifyElementIsDisplayed(LoginPanel.LOGIN_SUBMIT);				
	}
}