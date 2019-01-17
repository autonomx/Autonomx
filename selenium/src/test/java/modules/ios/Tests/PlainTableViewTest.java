package modules.ios.Tests;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import modules.TestBase;
import modules.main_ios.Panels.MainPanel;
import modules.main_ios.Panels.MainPanel.options;
import modules.main_ios.objects.PlainTableViewObject;

public class PlainTableViewTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.eureka.getDriver());
	}

	@Test
	public void verifyPlainTableViewForm() {
		PlainTableViewObject form = PlainTableViewObject.form().withDefaultValues();

		TestLog.When("I select plain table view object form");
		app.eureka.main.selectPanel(options.PLAIN_TABLE_VIEW_STYLE);
		
		TestLog.Then("I fill in the form");		
		app.eureka.plain.fillForm(form);

		TestLog.Then("I return to the main panel");
		Helper.verifyElementIsDisplayed(MainPanel.elements.PLAIN_TABLE_VIEW_STYLE);
	}
}