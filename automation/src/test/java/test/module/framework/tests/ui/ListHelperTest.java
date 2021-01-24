package test.module.framework.tests.ui;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import data.framework.User;
import module.framework.panel.SidePanel.Panels;
import module.framework.panel.UserPanel;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ListHelperTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.framework.getWebDriver());
	}
	
	@Test
	public void verifyTableMap2() {
		
	}
		
	@Test
	@Ignore // test needs to be updated for new site
	public void verifyTableMap() {
		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
		
		TestLog.And("I select user panel");
		app.framework.side.selectPanel(Panels.USERS);
		
		TestLog.And("I map the user table");
		HashMap<String, List<String>> table = Helper.list.getTableMap(UserPanel.USER_COLUMN_HEADERS, UserPanel.USER_ROWS,UserPanel.USER_ROW_CELLS);

		TestLog.Then("I verify the data is data is mapped");
		// last column data is empty, is not added to map
		int columnCount = Helper.getListCount(UserPanel.USER_COLUMN_HEADERS) - 1;
		Helper.assertEquals(columnCount, table.size());	
		
		for (Map.Entry<String, List<String>> entry : table.entrySet()) {
		    System.out.println(entry.getKey() + ":" + entry.getValue().toString());
		}
		
		Helper.assertEquals(table.get("Username").get(0), "autoAdmin1");
		Helper.assertEquals(table.get("Email").get(0), "autouser313@gmail.com");
	}
}