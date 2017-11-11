package test.java.android.sanityTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import core.helpers.Helper;
import core.logger.TestLog;
import core.runner.ParallelRunner;
import main.main_android.Panels.SearchPanel;
import main.main_android.objects.SearchObject;
import test.java.TestBase;

@RunWith(ParallelRunner.class)
public class SearchTest extends TestBase {
	
	@Before
	public void beforeMethod() throws Exception {
		setupWebDriver(app.focus_android.getDriver());
	}

	@Category({ SearchPanel.login.class})
	@Test
	public void verifySearchTest() {
		
		TestLog.When("I select bypass the initial screen");
		app.focus_android.launch.bypassFirstRunScreen();
		
		TestLog.Then("I search for '"+ SearchObject.DEFAULT_SEARCH_TERM +"'");
		SearchObject search = new SearchObject().withDefaultSearchTerm();
		app.focus_android.search.search(search);
		
		TestLog.Then("I verify search is complete");
		Helper.verifyElementIsDisplayed(SearchPanel.elements.ERASE_HISTORY);
	}
}