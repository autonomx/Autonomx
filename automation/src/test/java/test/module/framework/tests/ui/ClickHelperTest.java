package test.module.framework.tests.ui;


import org.openqa.selenium.Point;
import org.testng.annotations.BeforeMethod;
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
public class ClickHelperTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.framework.getWebDriver());
	}
	
	@Test
	public void verifyClickPoint() {
		
		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
		
		TestLog.And("I select user panel");
		app.framework.side.selectPanel(Panels.USERS);
				
		TestLog.Then("I click a point");
		Point position = Helper.getElementPosition(UserPanel.elements.NEW_USER_BUTTON, 0);
		//Helper.clickPointsAndExpect(1039, 92, UserPanel.elements.SAVE_BUTTON);

		Helper.clickPointsAndExpect(position.getX(), position.getY(), UserPanel.elements.SAVE_BUTTON);
	}
}