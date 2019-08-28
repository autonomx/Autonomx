package test.module.framework.tests;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import data.framework.User;
import module.framework.panel.MainPanel.elements;
import module.framework.panel.UserPanel;
import module.webApp.panel.SidePanel.Panels;
import test.module.framework.TestBase;

/**
 * this test class demonstrates 3 data types: data object, csv data and data provider
 * @author ehsan matean
 *
 */
public class ElementFinderTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.webApp.getWebDriver());
	}
	
	@Test
	public void verifyElementInList() {
		
		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
		
		TestLog.And("I select user panel");
		app.webApp.side.selectPanel(Panels.USERS);
				
		TestLog.Then("I verify element in list");
		int index = Helper.getElementIndexInList(elements.USER_ROWS,elements.EDIT_BUTTON);
		
		Helper.assertEquals(0, index);
		
		TestLog.And("I verify select element in list works");
		app.framework.main.selectEditUser(user);
		Helper.verifyElementIsDisplayed(UserPanel.elements.SAVE_BUTTON);
	}
}