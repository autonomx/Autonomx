package module.web.Tests;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import common.objects.UserObject;
import core.helpers.Helper;
import core.support.logger.TestLog;
import module.TestBase;
import module.webApp.panels.LoginPanel;
import module.webApp.panels.MainPanel;


public class Verify_Login_Test extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.webApp.getWebDriver());
	}
	
	@Test
	public void validate_user_Login() {
		UserObject user = UserObject.user().withAdminLogin();
		TestLog.When("I login with admin user");
		app.webApp.login.login(user);

		TestLog.Then("I verify admin logo is displayed");
		Helper.verifyElementIsDisplayed(MainPanel.elements.ADMIN_LOGO);

		TestLog.When("I logout");
		app.webApp.main.logout();

		TestLog.Then("I should see the login panel");
		Helper.verifyElementIsDisplayed(LoginPanel.elements.LOGIN_SUBMIT);
	}
}