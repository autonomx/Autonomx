package test.java.ios.panelTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import core.helpers.Helper;
import core.runner.ParallelRunner;
import main.main_ios.Panels.LaunchPanel;
import main.main_ios.Panels.SearchPanel;
import test.java.TestBase;

@RunWith(ParallelRunner.class)
public class SearchPanelTest extends TestBase {

	@Before
	public void beforeMethod() throws Exception {
		setupWebDriver(app.focus_ios.getDriver());
	}

	/**
	 * the purpose of panel tests is validate the presence of element in case of change in the future
	 */
	@Test
	public void VerifySearchPanel() {
		Helper.verifyElementIsDisplayed(LaunchPanel.elements.FIRST_RUN_BUTTON);
		app.focus_ios.launch.bypassFirstRunScreen();
		Helper.verifyElementIsDisplayed(SearchPanel.elements.SEARCH_FIELD);
	}
}