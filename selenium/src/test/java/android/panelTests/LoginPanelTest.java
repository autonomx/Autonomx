package test.java.android.panelTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import core.helpers.Helper;
import core.runner.ParallelRunner;
import main.main_android.Panels.LaunchPanel;
import main.main_android.Panels.SearchPanel;
import test.java.TestBase;

@RunWith(ParallelRunner.class)
public class LoginPanelTest extends TestBase {

	@Before
	public void beforeMethod() throws Exception {
		setupWebDriver(app.focus_android.getDriver());
	}

	/**
	 * the purpose of panel tests is validate the presence of element in case of change in the future
	 */
	@Test
	public void VerifySearchPanel() {
		Helper.verifyElementIsDisplayed(LaunchPanel.elements.NEXT_BUTTON);
		app.focus_android.launch.bypassFirstRunScreen();
		Helper.verifyElementIsDisplayed(SearchPanel.elements.SEARCH_FIELD);
		Helper.verifyElementIsDisplayed(SearchPanel.elements.MORE_OPTIONS);
	}
}