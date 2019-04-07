package test.module.web.tests;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import module.common.data.CommonUser;
import module.webApp.panel.SidePanel.Panels;
import module.webApp.panel.UserPanel;
import test.module.TestBase;

public class VerifyUserCreateTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.webApp.getWebDriver());
	}

	@Test
	public void createAdminUser() {
		CommonUser user = Data.common.commonuser().withAdminLogin();

		TestLog.When("I login with admin user");
		app.webApp.login.login(user);
		
		TestLog.And("I select user panel");
		app.webApp.side.selectPanel(Panels.USERS);

		TestLog.Then("I add an admin user");
		user = Data.common.commonuser().withDefaultUser();
		app.webApp.user.addUser(user);
		
		TestLog.And("I verify the user is displayed in the user list");
		Helper.verifyContainsIsInList(UserPanel.elements.USER_ROWS, user.username);
	}
	
	@Test
	public void editAdminUser() {
		CommonUser user = Data.common.commonuser().withAdminLogin();

		TestLog.When("I login with admin user");
		app.webApp.login.login(user);
		
		TestLog.And("I select user panel");
		app.webApp.side.selectPanel(Panels.USERS);

		TestLog.Then("I add an admin user");
		user = Data.common.commonuser().withDefaultUser();
		app.webApp.user.addUser(user);
		
		TestLog.And("I verify the user is displayed in the user list");
		Helper.verifyContainsIsInList(UserPanel.elements.USER_ROWS, user.username);
		
		TestLog.Then("I edit the user");
		CommonUser editUser = Data.common.commonuser().withEdittUser();
		app.webApp.user.editUser(user, editUser);
	}
}