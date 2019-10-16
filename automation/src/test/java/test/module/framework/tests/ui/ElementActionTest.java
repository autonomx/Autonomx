package test.module.framework.tests.ui;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import module.framework.panel.LoginPanel;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ElementActionTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.webApp.getWebDriver());
	}
	
	@Test
	public void moveMouseToPositionByPointsTest() {
		int submitButtonWidth = Helper.findMiddleOfElement(LoginPanel.elements.LOGIN_SUBMIT)[0];
		int submitButtonHeight = Helper.findMiddleOfElement(LoginPanel.elements.LOGIN_SUBMIT)[1];

		TestLog.When("I test finding element by location");	
		Helper.click.clickPointsAndExpect(submitButtonWidth, submitButtonHeight, LoginPanel.elements.INVALID_INPUT_MESSAGE);
		Helper.verifyElementIsDisplayed(LoginPanel.elements.INVALID_INPUT_MESSAGE);
		
		TestLog.Then("I verify element can be found by location once the mouse has been displaced");	
		Helper.refreshPage();
		Helper.click.clickPointsAndExpect(submitButtonWidth, submitButtonHeight, LoginPanel.elements.INVALID_INPUT_MESSAGE);
		Helper.verifyElementIsDisplayed(LoginPanel.elements.INVALID_INPUT_MESSAGE);	
	}
	
	@Test
	public void doubleClickActionTest() {
		int submitButtonWidth = Helper.findMiddleOfElement(LoginPanel.elements.LOGIN_SUBMIT)[0];
		int submitButtonHeight = Helper.findMiddleOfElement(LoginPanel.elements.LOGIN_SUBMIT)[1];

		TestLog.When("I test finding element by location");	
		Helper.clickAction.doubleClickPoints(submitButtonWidth, submitButtonHeight);
		Helper.verifyElementIsDisplayed(LoginPanel.elements.INVALID_INPUT_MESSAGE);
	}
}