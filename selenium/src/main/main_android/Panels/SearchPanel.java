package main.main_android.Panels;


import core.helpers.Element;
import core.helpers.Helper;
import core.webElement.EnhancedBy;
import main.main_android.FocusAndroid;
import main.main_android.objects.SearchObject;

public class SearchPanel {

	// categories
	public interface login {}
	
	FocusAndroid manager;

	public SearchPanel(FocusAndroid manager) {
		this.manager = manager;
	}
	
	public static class elements {
		private static String app_package_name = "org.mozilla.focus:id/";
		
	    public static EnhancedBy SEARCH_FIELD = Element.byId(app_package_name + "url_edit", "search field");
	    public static EnhancedBy MORE_OPTIONS = Element.byId(app_package_name + "menu", "menu options");
	    public static EnhancedBy ERASE_HISTORY = Element.byId(app_package_name + "erase", "erase browser history button");
	}

	/**
	 * enter search info and dismiss keyboard
	 * 
	 * @param user
	 */
	public void search(SearchObject search) { 
		Helper.setFieldAndEnter(search.searchTerm, elements.SEARCH_FIELD);
		Helper.waitForElementToLoad(elements.ERASE_HISTORY);
	}
}