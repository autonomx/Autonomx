package test.module.framework.tests;


import java.util.HashMap;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import data.webApp.User;
import module.framework.panel.UserPanel;
import module.webApp.panel.SidePanel.Panels;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ListHelperTests extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.webApp.getWebDriver());
	}
	
	@Test
	public void verifyTableMap() {
		User user = Data.webApp.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.webApp.login.loginWithCsvData(user);
		
		TestLog.And("I select user panel");
		app.webApp.side.selectPanel(Panels.USERS);
		
		TestLog.And("I map the user table");
		HashMap<String, List<String>> table = Helper.list.getTableMap(UserPanel.elements.USER_COLUMN_HEADERS, UserPanel.elements.USER_ROWS,UserPanel.elements.USER_ROW_CELLS);

		TestLog.Then("I verify the data is data is mapped");
		// last column data is empty, is not added to map
		int columnCount = Helper.getListCount(UserPanel.elements.USER_COLUMN_HEADERS) - 1;
		Helper.assertEquals(columnCount, table.size());	
		
		Helper.assertEquals(table.get("Id").get(0), "1");
		Helper.assertEquals(table.get("Username").get(0), "autoAdmin1");
		Helper.assertEquals(table.get("Email").get(0), "autouser313@gmail.com");
	}
}