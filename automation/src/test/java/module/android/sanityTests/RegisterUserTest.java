package module.android.sanityTests;



import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import common.objects.UserObject;
import core.support.logger.TestLog;
import module.TestBase;

public class RegisterUserTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.androidApp.getAndroidMobileDriver());
	}

	@Test
	public void registerUser() {
		UserObject user = UserObject.user().withDefaultUser();

		TestLog.When("I select the registration panel");
		app.androidApp.main.selectRegisterPanel();
		
		TestLog.When("I register a user");
		app.androidApp.registration.registerUser(user);

		TestLog.Then("I verify the user has logged in");
		//Helper.verifyElementIsDisplayed(MainPanel.elements.INSTALLS_PANEL);
	}
}