package main.main_web.Panels;

import core.helpers.Element;
import core.helpers.Helper;
import core.webElement.EnhancedBy;
import main.main_web.FocusWeb;
import main.main_web.objects.SearchObject;

public class SearchPanel {

	// category
	public interface search {}
	
	FocusWeb manager; 	
	public SearchPanel(FocusWeb manager) {
		this.manager = manager;
	}
	
	public static class elements {
	
		public static EnhancedBy SEARCH_FIELD = Element.byCss("[title='Search']", "search field");
		public static EnhancedBy VOICE_SEARCH = Element.byCss("[aria-label='Search by voice']", "voice search");
		
		public static EnhancedBy RESULT_STATS = Element.byCss("#resultStats", "result stats");
	}
	
	
	/**
	 * enter login info and select enter
	 * 
	 * @param user
	 */
	public void search(SearchObject search) {
		Helper.setFieldAndEnter(search.searchTerm, elements.SEARCH_FIELD);
		Helper.waitForElementToLoad(elements.RESULT_STATS);
	}
}