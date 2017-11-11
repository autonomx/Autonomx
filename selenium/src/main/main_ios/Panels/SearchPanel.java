package main.main_ios.Panels;

import core.helpers.Element;
import core.helpers.Helper;
import core.webElement.EnhancedBy;
import main.main_ios.FocusIos;
import main.main_ios.objects.SearchObject;


	public class SearchPanel {

		// category
		public interface search {}
		
		FocusIos manager; 	
		public SearchPanel(FocusIos manager) {
			this.manager = manager;
		}
		
		public static class elements {
		
			public static EnhancedBy SEARCH_FIELD = Element.byMobileClass("XCUIElementTypeTextField", "search field");
			public static EnhancedBy ERASE_BUTTON = Element.byAccessibility("URLBar.deleteButton", "erase button");
			public static EnhancedBy CANCEL_BUTTON = Element.byAccessibility("Cancel", "cancel button");
			public static EnhancedBy PROGRESS_BAR = Element.byAccessibility("Progress", "progress bar");
		}
		
		
		/**
		 * enter search info and dismiss keyboard
		 * 
		 * @param user
		 */
		public void search(SearchObject search) { 
			Helper.setField(search.searchTerm, elements.SEARCH_FIELD);
			Helper.waitForElementToBeRemoved(elements.CANCEL_BUTTON);
		}
}