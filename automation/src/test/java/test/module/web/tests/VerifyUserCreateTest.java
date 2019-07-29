package test.module.web.tests;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import data.webApp.User;
import module.common.data.CommonUser;
import module.webApp.panel.SidePanel.Panels;
import module.webApp.panel.UserPanel;
import test.module.web.TestBase;

public class VerifyUserCreateTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.webApp.getWebDriver());
	}

	@Test
	public void createAdminUser() {
		User loginUser = Data.webApp.user().admin();

		TestLog.When("I login with admin user");
		app.webApp.login.login(loginUser);
		
		TestLog.And("I select user panel");
		app.webApp.side.selectPanel(Panels.USERS);

		TestLog.Then("I add an admin user");
		CommonUser user = Data.common.commonuser().withAdminLogin();
		user = Data.common.commonuser().withDefaultUser();
		app.webApp.user.addUser(user);
		
		TestLog.And("I verify the user is displayed in the user list");
		Helper.verifyContainsIsInList(UserPanel.elements.USER_ROWS, user.username);
	}
	
	@Test
	public void editAdminUser() {
		User loginUser = Data.webApp.user().admin();

		TestLog.When("I login with admin user");
		app.webApp.login.login(loginUser);
		
		TestLog.And("I select user panel");
		app.webApp.side.selectPanel(Panels.USERS);

		TestLog.Then("I add an admin user");
		// can use csv based user or java class based user object
		CommonUser user = Data.common.commonuser().withAdminLogin();
		user = Data.common.commonuser().withDefaultUser();
		app.webApp.user.addUser(user);
		
		TestLog.And("I verify the user is displayed in the user list");
		Helper.verifyContainsIsInList(UserPanel.elements.USER_ROWS, user.username);
		
		TestLog.Then("I edit the user");
		CommonUser editUser = Data.common.commonuser().withEdittUser();
		app.webApp.user.editUser(user, editUser);
	}
}