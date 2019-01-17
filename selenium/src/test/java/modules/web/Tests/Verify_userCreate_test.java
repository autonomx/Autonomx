package modules.web.Tests;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import common.objects.UserObject;
import core.helpers.Helper;
import core.support.logger.TestLog;
import modules.TestBase;
import modules.webApp.Panels.SidePanelNavigation.Panels;
import modules.webApp.Panels.UserPanel;

public class Verify_userCreate_test extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.strapi.getDriver());
	}

	@Test
	public void create_admin_user() {
		UserObject user = UserObject.user().withAdminLogin();

		TestLog.When("I login with admin user");
		app.strapi.login.login(user);
		
		TestLog.And("I select user panel");
		app.strapi.side.selectPanel(Panels.USERS);

		TestLog.Then("I add an admin user");
		user = UserObject.user().withDefaultUser();
		app.strapi.user.addUser(user);
		
		TestLog.And("I verify the user is displayed in the user list");
		Helper.verifyContainsIsInList(UserPanel.elements.USER_ROWS, user.username().get());
	}
	
	@Test
	public void edit_admin_user() {
		UserObject user = UserObject.user().withAdminLogin();

		TestLog.When("I login with admin user");
		app.strapi.login.login(user);
		
		TestLog.And("I select user panel");
		app.strapi.side.selectPanel(Panels.USERS);

		TestLog.Then("I add an admin user");
		user = UserObject.user().withDefaultUser();
		app.strapi.user.addUser(user);
		
		TestLog.And("I verify the user is displayed in the user list");
		Helper.verifyContainsIsInList(UserPanel.elements.USER_ROWS, user.username().get());
		
		TestLog.Then("I edit the user");
		UserObject editUser = UserObject.user().withEdittUser();
		app.strapi.user.editUser(user, editUser);
	}
}