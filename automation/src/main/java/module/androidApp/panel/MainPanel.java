package module.androidApp.panel;

/**
 * Rebuild or clean project after adding new panel or removing to generate associated files
 */
import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import moduleManager.module.androidApp.PanelManager;

@Panel
public class MainPanel {

	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public MainPanel(PanelManager manager) {
		this.manager = manager;
	}
	
	// Locators
	//--------------------------------------------------------------------------------------------------------	
		public static EnhancedBy REGISTER_PANEL = Element.byAccessibility("startUserRegistrationCD", "registration button");
		public static EnhancedBy POPUP_BUTTON = Element.byAccessibility("showPopupWindowButtonCD", "popup button");
	
	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void selectRegisterPanel() {
		Helper.clickAndExpect(REGISTER_PANEL, RegistrationPanel.USERNAME_FIELD);
	}
}