package test.module.framework.tests.ui;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import data.framework.User;
import module.framework.panel.LoginPanel;
import module.framework.panel.MainPanel;
import module.framework.panel.SidePanel.Panels;
import module.framework.panel.UserPanel;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ElementFinderTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.framework.getWebDriver());
	}
	
	@Test
	@Ignore //TODO: need to add user before test
	public void verifyElementInList() {
		
		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
		
		TestLog.And("I select user panel");
		app.framework.side.selectPanel(Panels.USERS);
				
		TestLog.Then("I verify element in list");
		int index = Helper.getElementIndexInList(MainPanel.USER_ROWS,MainPanel.EDIT_BUTTON);
		
		Helper.assertEquals(0, index);
		
		TestLog.And("I verify select element in list works");
		app.framework.main.selectEditUser(user);
		Helper.verifyElementIsDisplayed(UserPanel.SAVE_BUTTON);
	}
	
	@Test
	public void verifyElementIsNotDisplayed() {
		
		boolean isError = Helper.isPresent(LoginPanel.ERROR_MESSAGE);
		Helper.assertTrue("error message should not be displayed", !isError);			
	}
	
	@Test
	@Ignore // need to update for updated site
	public void verifyElementIsVisible() {
		
		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
		
		TestLog.Then("I resize the page");
		Helper.setPageSize(800, 800);
		
		TestLog.And("I verify the visibility of admin logo");
		boolean isVisible = Helper.isVisibleInViewport(MainPanel.ADMIN_LOGO);
		Helper.assertTrue("logo should be visible", isVisible);
		
		TestLog.And("I verify the submit button is present");
		boolean isPresent = Helper.isPresent(MainPanel.SUBMIT_BUTTON);
		Helper.assertTrue("submit button should be present", isPresent);
	
		TestLog.Then("I verify the visibility of submit button");
		isVisible = Helper.isVisibleInViewport(MainPanel.SUBMIT_BUTTON);
		Helper.assertTrue("logo should not be visible", !isVisible);
	}
	
	@Test
	public void verifyWaitForElement() {
		TestLog.When("I set wait time of 3 seconds, and the element is not found, test should not fail");
		Helper.waitForElementToLoad(MainPanel.ADMIN_LOGO, 3, 1);
	}
	
	
}