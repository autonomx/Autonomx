package test.module.serviceIntegration.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.support.logger.TestLog;
import data.Data;
import module.common.data.CommonUser;
import test.module.TestBase;

public class VerifyUsersTest extends TestBase {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {

		TestLog.When("I login with admin user");
		CommonUser user = Data.common.commonuser().withAdminLogin();
		app.serviceUiIntegration.login.login(user);

		TestLog.And("I delete all users with prefix zzz_");
		app.serviceUiIntegration.user.deleteAllUsers("zzz_");

		TestLog.When("I start test");
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {

		TestLog.When("I login with admin user");
		CommonUser user = Data.common.commonuser().withAdminLogin();
		app.serviceUiIntegration.login.login(user);

		TestLog.And("I delete all users with prefix zzz_");
		app.serviceUiIntegration.user.deleteAllUsers("zzz_");

		TestLog.Then("I end test");
	}

	@Test
	public void verifyCreateUser() {

		TestLog.When("I login with admin user");
		CommonUser user = Data.common.commonuser().withAdminLogin();
		app.serviceUiIntegration.login.login(user);

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		app.serviceUiIntegration.user.createUser(user);

		TestLog.Then("I delete the user '" + user.username + "'");
		app.serviceUiIntegration.user.deleteUser();
	}
}