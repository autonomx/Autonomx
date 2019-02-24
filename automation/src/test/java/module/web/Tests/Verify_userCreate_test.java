package module.web.Tests;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import common.objects.UserObject;
import core.helpers.Helper;
import core.support.logger.TestLog;
import module.webApp.panels.SidePanel.Panels;
import module.TestBase;
import module.webApp.panels.UserPanel;

public class Verify_userCreate_test extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.webApp.getWebDriver());
	}

	@Test
	public void create_admin_user() {
		UserObject user = UserObject.user().withAdminLogin();

		TestLog.When("I login with admin user");
		app.webApp.login.login(user);
		
		TestLog.And("I select user panel");
		app.webApp.side.selectPanel(Panels.USERS);

		TestLog.Then("I add an admin user");
		user = UserObject.user().withDefaultUser();
		app.webApp.user.addUser(user);
		
		TestLog.And("I verify the user is displayed in the user list");
		Helper.verifyContainsIsInList(UserPanel.elements.USER_ROWS, user.username().get());
	}
	
	@Test
	public void edit_admin_user() {
		UserObject user = UserObject.user().withAdminLogin();

		TestLog.When("I login with admin user");
		app.webApp.login.login(user);
		
		TestLog.And("I select user panel");
		app.webApp.side.selectPanel(Panels.USERS);

		TestLog.Then("I add an admin user");
		user = UserObject.user().withDefaultUser();
		app.webApp.user.addUser(user);
		
		TestLog.And("I verify the user is displayed in the user list");
		Helper.verifyContainsIsInList(UserPanel.elements.USER_ROWS, user.username().get());
		
		TestLog.Then("I edit the user");
		UserObject editUser = UserObject.user().withEdittUser();
		app.webApp.user.editUser(user, editUser);
	}
}