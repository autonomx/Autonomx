package module.webApp.panel;

/**
 * Rebuild or clean project after adding new panel or removing to generate associated files
 */
import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import moduleManager.module.webApp.PanelManager;

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

	// using wildcard to find element
	public static EnhancedBy ADMIN_LOGO = Element.byCss("[class*='adminprojectName__admin']", "admin logo");
	public static EnhancedBy USERINFO_DROPDOWN = Element.byCss(".fa-caret-down", "userinfo dropdown");
	public static EnhancedBy SIGNOUT_OPTION = Element.byCss(".fa-sign-out", "signout option");
		
	
	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void logout() {
		Helper.selectDropDown(USERINFO_DROPDOWN, SIGNOUT_OPTION);
		Helper.waitForElementToLoad(LoginPanel.USER_NAME_FIELD);
	}
}