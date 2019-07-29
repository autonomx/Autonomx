package test.module.android.tests;



import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import module.androidApp.panel.MainPanel;
import module.common.data.CommonUser;
import test.module.android.TestBase;

public class RegisterUserTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.androidApp.getAndroidMobileDriver());
	}

	@Test
	public void registerUser() {
		
		CommonUser user = Data.common.commonuser().withDefaultUser(); 
		
		// dismiss old android version alert
		Helper.dimissAlert();
		
		TestLog.When("I select the registration panel");
		app.androidApp.main.selectRegisterPanel();
		
		TestLog.When("I register a user");
		app.androidApp.registration.registerUser(user);

		TestLog.Then("I verify registration is complete");
		Helper.verifyElementIsDisplayed(MainPanel.elements.REGISTER_PANEL);
	}
}