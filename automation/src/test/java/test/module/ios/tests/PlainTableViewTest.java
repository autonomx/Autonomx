package test.module.ios.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import module.iosApp.data.PlainTableViewObject;
import module.iosApp.panel.MainPanel;
import module.iosApp.panel.MainPanel.options;
import test.module.TestBase;

public class PlainTableViewTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.iosApp.getIosMobileDriver());
	}

	@Test
	public void verifyPlainTableViewForm() {
		PlainTableViewObject form = Data.iosApp.plaintableviewobject().withDefaultValues();

		TestLog.When("I select plain table view object form");
		app.iosApp.main.selectPanel(options.PLAIN_TABLE_VIEW_STYLE);
		
		TestLog.Then("I fill in the form");		
		app.iosApp.plaintableview.fillForm(form);
		
		TestLog.Then("I return to the main panel");
		Helper.verifyElementIsDisplayed(MainPanel.elements.PLAIN_TABLE_VIEW_STYLE);
	}
}
