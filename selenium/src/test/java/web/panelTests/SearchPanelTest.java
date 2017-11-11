package test.java.web.panelTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import core.helpers.Helper;
import core.runner.ParallelRunner;
import main.main_web.Panels.SearchPanel;
import test.java.TestBase;

@RunWith(ParallelRunner.class)
public class SearchPanelTest extends TestBase {


	@Before
	public void beforeMethod() throws Exception {
		setupWebDriver(app.focus_web.getDriver());
	}

	/**
	 * verifies the presence of elements in case the elements locators changes
	 */
	@Test
	public void VerifySearchPanel() {
		Helper.verifyElementIsDisplayed(SearchPanel.elements.SEARCH_FIELD);
		Helper.verifyElementIsDisplayed(SearchPanel.elements.VOICE_SEARCH);
	}

}