package module.webApp.panels;

import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import moduleManager.module.webApp.PanelManager;

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

		// using wildcard to find element
		public static EnhancedBy ADMIN_LOGO = Element.byCss("[class*='adminprojectName__admin']", "admin logo");
		public static EnhancedBy USERINFO_DROPDOWN = Element.byCss(".fa-caret-down", "userinfo dropdown");
		public static EnhancedBy SIGNOUT_OPTION = Element.byCss(".fa-sign-out", "signout option");
	}
	
	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void logout() {
		Helper.selectDropDown(elements.USERINFO_DROPDOWN, elements.SIGNOUT_OPTION);
		Helper.waitForElementToLoad(LoginPanel.elements.USER_NAME_FIELD);
	}

}