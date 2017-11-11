package main.main_ios.Panels;

import core.helpers.Element;
import core.helpers.Helper;
import core.webElement.EnhancedBy;
import main.main_ios.FocusIos;

public class LaunchPanel {
	
	FocusIos manager; 	
	public LaunchPanel(FocusIos manager) {
		this.manager = manager;
	}
	
	
	public static class elements {
		
	    public static EnhancedBy FIRST_RUN_BUTTON = Element.byAccessibility("FirstRunViewController.button", "first run button");
	}
	
	/**
	 * selects first run button and waits for search field to load
	 */
	public void bypassFirstRunScreen() {
		Helper.clickAndExpect(elements.FIRST_RUN_BUTTON, SearchPanel.elements.SEARCH_FIELD);
	}
}