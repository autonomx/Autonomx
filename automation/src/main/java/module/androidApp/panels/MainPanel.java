package module.androidApp.panels;


import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import moduleManager.module.androidApp.PanelManager;

@Panel
public class MainPanel {

	// ensure the proper panel manager is imported, containing project "module name".
	//eg. moduleManager.module.<module name>.PanelManager
	PanelManager manager;
	public MainPanel(PanelManager manager) {
		this.manager = manager;
	}
	
	// Locators
	//--------------------------------------------------------------------------------------------------------	
	public static class elements {
		public static EnhancedBy REGISTER_PANEL = Element.byAccessibility("startUserRegistrationCD", "registration button");
		public static EnhancedBy POPUP_BUTTON = Element.byAccessibility("showPopupWindowButtonCD", "popup button");

	}
	
	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void selectRegisterPanel() {
		Helper.clickAndExpect(elements.REGISTER_PANEL, RegistrationPanel.elements.USERNAME_FIELD);
	}
}