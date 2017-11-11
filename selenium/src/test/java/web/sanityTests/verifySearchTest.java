package test.java.web.sanityTests;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import core.helpers.Helper;
import core.logger.TestLog;
import core.runner.ParallelRunner;
import main.main_web.Panels.SearchPanel;
import main.main_web.objects.SearchObject;
import test.java.TestBase;

@RunWith(ParallelRunner.class)
public class verifySearchTest extends TestBase {

	@Before
	public void beforeMethod() throws Exception {
		setupWebDriver(app.focus_web.getDriver());
	}

	@Category({ SearchPanel.search.class})
    @Test
	public void verifyDefaultSearchTest() {
		
		TestLog.When("I search term '" + SearchObject.DEFAULT_SEARCH_TERM + "'");
		SearchObject search = new SearchObject().withDefaultSearchTerm();
		app.focus_web.search.search(search);
		
		TestLog.Then("I verify search results have returned");
		Helper.verifyElementIsDisplayed(SearchPanel.elements.RESULT_STATS);
	}
	
	@Category({ SearchPanel.search.class})
    @Test
	public void verifyValidSearchTest() {
		
		TestLog.When("I search term 'testing'");
		SearchObject search = new SearchObject().withSearchTerm("testing");
		app.focus_web.search.search(search);
		
		TestLog.Then("I verify search results have returned");
		Helper.verifyElementIsDisplayed(SearchPanel.elements.RESULT_STATS);
	}
	
	@Category({ SearchPanel.search.class})
    @Test
	public void verifyMoreSearchTest() {
		
		TestLog.When("I search term 'framework'");
		SearchObject search = new SearchObject().withSearchTerm("framework");
		app.focus_web.search.search(search);
		
		TestLog.Then("I verify search results have returned");
		Helper.verifyElementIsDisplayed(SearchPanel.elements.RESULT_STATS);
	}
	
	@Category({ SearchPanel.search.class})
    @Test
	public void verifyAdditionalSearchTest() {
		
		TestLog.When("I search term 'qa test automation'");
		SearchObject search = new SearchObject().withSearchTerm("qa test automation");
		app.focus_web.search.search(search);
		
		TestLog.Then("I verify search results have returned");
		Helper.verifyElementIsDisplayed(SearchPanel.elements.RESULT_STATS);
	}
}