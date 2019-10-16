package module.framework.panel;

/**
 * Rebuild or clean project after adding new panel or removing to generate associated files
 */
import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import data.framework.User;
import moduleManager.module.framework.PanelManager;

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
	public static class elements {
	
		// using wildcard to find element
		public static EnhancedBy ADMIN_LOGO = Element.byCss("[class*='adminprojectName__admin']", "admin logo");
		public static EnhancedBy USERINFO_DROPDOWN = Element.byCss(".fa-caret-down", "userinfo dropdown");
		public static EnhancedBy SIGNOUT_OPTION = Element.byCss(".fa-sign-out", "signout option");
		public static EnhancedBy EDIT_BUTTON = Element.byCss(".fa-pencil", "signout option");

		public static EnhancedBy SUBMIT_BUTTON = Element.byCss("[type='submit']", "submit button");

		
		// Users panel
		public static EnhancedBy USER_ROWS = Element.byCss("tr[class*='TableRow']", "user rows");	
	}
	
	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void logout() {
		Helper.selectDropDown(elements.USERINFO_DROPDOWN, elements.SIGNOUT_OPTION);
		Helper.waitForElementToLoad(LoginPanel.elements.USER_NAME_FIELD);
	}
	
	public void selectEditUser(User user) {
		Helper.selectElementContainedInList(elements.USER_ROWS, user.getUsername(), elements.EDIT_BUTTON);
	}
}