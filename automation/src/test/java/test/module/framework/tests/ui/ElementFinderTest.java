package test.module.framework.tests.ui;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import data.framework.User;
import module.framework.panel.LoginPanel;
import module.framework.panel.MainPanel;
import module.framework.panel.MainPanel.elements;
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
	public void verifyElementInList() {
		
		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
		
		TestLog.And("I select user panel");
		app.framework.side.selectPanel(Panels.USERS);
				
		TestLog.Then("I verify element in list");
		int index = Helper.getElementIndexInList(elements.USER_ROWS,elements.EDIT_BUTTON);
		
		Helper.assertEquals(0, index);
		
		TestLog.And("I verify select element in list works");
		app.framework.main.selectEditUser(user);
		Helper.verifyElementIsDisplayed(UserPanel.elements.SAVE_BUTTON);
	}
	
	@Test
	public void verifyElementIsNotDisplayed() {
		
		boolean isError = Helper.isPresent(LoginPanel.elements.ERROR_MESSAGE);
		Helper.assertTrue("error message should not be displayed", !isError);			
	}
	
	@Test
	public void verifyElementIsVisible() {
		
		User user = Data.framework.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.framework.login.loginWithCsvData(user);
		
		TestLog.Then("I resize the page");
		Helper.setPageSize(800, 800);
		
		TestLog.And("I verify the visibility of admin logo");
		boolean isVisible = Helper.isVisibleInViewport(MainPanel.elements.ADMIN_LOGO);
		Helper.assertTrue("logo should be visible", isVisible);
		
		TestLog.And("I verify the submit button is present");
		boolean isPresent = Helper.isPresent(MainPanel.elements.SUBMIT_BUTTON);
		Helper.assertTrue("submit button should be present", isPresent);
	
		TestLog.Then("I verify the visibility of submit button");
		isVisible = Helper.isVisibleInViewport(MainPanel.elements.SUBMIT_BUTTON);
		Helper.assertTrue("logo should not be visible", !isVisible);
	}
	
	@Test
	public void verifyWaitForElement() {
		TestLog.When("I set wait time of 3 seconds, and the element is not found, test should not fail");
		Helper.waitForElementToLoad(MainPanel.elements.ADMIN_LOGO, 3, 1);
	}
	
	
}