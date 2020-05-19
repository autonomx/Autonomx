package module.iosApp.panel;

/**
 * Rebuild or clean project after adding new panel to generate associated files
 */
import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import moduleManager.module.iosApp.PanelManager;

@Panel

public class MainPanel {

	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public MainPanel(PanelManager manager) {
		this.manager = manager;
	}

	public enum options {
		PLAIN_TABLE_VIEW_STYLE, LIST_SECTIONS
	}

	// Locators
	//--------------------------------------------------------------------------------------------------------	
	public static EnhancedBy PLAIN_TABLE_VIEW_STYLE = Element.byAccessibility("Plain Table View Style", "plain table view style");
	public static EnhancedBy LIST_SECTIONS = Element.byAccessibility("List Sections", "list sections");
	public static EnhancedBy EUREKA = Element.byAccessibility("Eureka", "eureka logo");


	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void selectPanel(options panel) {
		switch (panel) {
		case PLAIN_TABLE_VIEW_STYLE:
			Helper.mobile.mobile_scrollToElement(PLAIN_TABLE_VIEW_STYLE);
			Helper.click.clickAndExpect(PLAIN_TABLE_VIEW_STYLE, PlainTableViewPanel.NAME);
			break;
		case LIST_SECTIONS:
			Helper.click.clickAndExpect(LIST_SECTIONS, ListSections.BACK);
			break;
		default:
			throw new IllegalStateException("Unsupported panels " + panel);
		}
	}
}