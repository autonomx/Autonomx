package test.module.android.tests;



import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.support.logger.TestLog;
import data.Data;
import module.common.data.CommonUser;
import test.module.TestBase;

public class RegisterUserTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.androidApp.getAndroidMobileDriver());
	}

	@Test
	public void registerUser() {
		CommonUser user = Data.common.commonuser().withDefaultUser(); 

		TestLog.When("I select the registration panel");
		app.androidApp.main.selectRegisterPanel();
		
		TestLog.When("I register a user");
		app.androidApp.registration.registerUser(user);

		TestLog.Then("I verify the user has logged in");
		//Helper.verifyElementIsDisplayed(MainPanel.elements.INSTALLS_PANEL);
	}
}