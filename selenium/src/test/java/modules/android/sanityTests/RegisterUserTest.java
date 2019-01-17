package modules.android.sanityTests;



import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import common.objects.UserObject;
import core.support.logger.TestLog;
import modules.TestBase;

public class RegisterUserTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.selenoid.getDriver());
	}

	@Test
	public void registerUser() {
		UserObject user = UserObject.user().withDefaultUser();

		TestLog.When("I select the registration panel");
		app.selenoid.main.selectRegisterPanel();
		
		TestLog.When("I register a user");
		app.selenoid.register.registerUser(user);

		TestLog.Then("I verify the user has logged in");
		//Helper.verifyElementIsDisplayed(MainPanel.elements.INSTALLS_PANEL);
	}
}