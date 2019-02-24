package module.rest;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import common.objects.UserObject;
import core.support.logger.TestLog;
import module.TestBase;

public class VerifyUsersTest extends TestBase {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {

		TestLog.When("I login with admin user");
		UserObject user = UserObject.user().withAdminLogin();
		app.serviceUiIntegration.login.login(user);

		TestLog.And("I delete all users with prefix zzz_");
		app.serviceUiIntegration.user.deleteAllUsers("zzz_");

		TestLog.When("I start test");
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {

		TestLog.When("I login with admin user");
		UserObject user = UserObject.user().withAdminLogin();
		app.serviceUiIntegration.login.login(user);

		TestLog.And("I delete all users with prefix zzz_");
		app.serviceUiIntegration.user.deleteAllUsers("zzz_");

		TestLog.When("I end test");
	}

	@Test
	public void verifyCreateUser() {

		TestLog.When("I login with admin user");
		UserObject user = UserObject.user().withAdminLogin();
		app.serviceUiIntegration.login.login(user);

	    user = UserObject.user().withDefaultUser();
		TestLog.And("I create user '" + user.username().get() + "'");
		app.serviceUiIntegration.user.createUser(user);

		TestLog.Then("I delete the user '" + user.username().get() + "'");
		app.serviceUiIntegration.user.deleteUser();
	}
}