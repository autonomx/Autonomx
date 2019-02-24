package module.ios.Tests;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import module.TestBase;
import module.iosApp.objects.PlainTableViewObject;
import module.iosApp.panels.MainPanel;
import module.iosApp.panels.MainPanel.options;

public class PlainTableViewTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.iosApp.getIosMobileDriver());
	}

	@Test
	public void verifyPlainTableViewForm() {
		PlainTableViewObject form = PlainTableViewObject.form().withDefaultValues();

		TestLog.When("I select plain table view object form");
		app.iosApp.main.selectPanel(options.PLAIN_TABLE_VIEW_STYLE);
		
		TestLog.Then("I fill in the form");		
		app.iosApp.plaintableview.fillForm(form);

		TestLog.Then("I return to the main panel");
		Helper.verifyElementIsDisplayed(MainPanel.elements.PLAIN_TABLE_VIEW_STYLE);
	}
}