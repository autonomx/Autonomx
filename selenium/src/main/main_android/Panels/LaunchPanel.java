package main.main_android.Panels;

import core.helpers.Element;
import core.helpers.Helper;
import core.webElement.EnhancedBy;
import main.main_android.FocusAndroid;
public class LaunchPanel {
	
	FocusAndroid manager; 	
	public LaunchPanel(FocusAndroid manager) {
		this.manager = manager;
	}
	
	
	public static class elements {
		
		private static String app_package_name = "org.mozilla.focus:id/";
		
	    public static EnhancedBy NEXT_BUTTON = Element.byId(app_package_name + "next", "next button");
	    public static EnhancedBy FINISH_BUTTON = Element.byId(app_package_name + "finish", "finish button");
	}
	
	/**
	 * selects first run button and waits for search field to load
	 */
	public void bypassFirstRunScreen() {
		Helper.clickAndExpect(elements.NEXT_BUTTON, elements.NEXT_BUTTON);
		Helper.clickAndExpect(elements.NEXT_BUTTON, elements.NEXT_BUTTON);
		Helper.clickAndExpect(elements.NEXT_BUTTON, elements.FINISH_BUTTON);
		Helper.clickAndExpect(elements.FINISH_BUTTON, SearchPanel.elements.SEARCH_FIELD);
	}
}