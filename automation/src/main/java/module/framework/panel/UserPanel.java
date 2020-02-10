package module.framework.panel;

import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import data.framework.User;
import moduleManager.module.framework.PanelManager;

@Panel
public class UserPanel{

	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public UserPanel(PanelManager manager) {
		this.manager = manager;
	}
	
	// Locators
	//--------------------------------------------------------------------------------------------------------	
	public static class elements {
		public static EnhancedBy NEW_USER_BUTTON = Element.byCss("[label*='addAnEntry'] span", "add new user");
		
		// add/edit user panel
		public static EnhancedBy USER_NAME_FILED = Element.byCss("#username", "username field");
		public static EnhancedBy EMAIL_FIELD = Element.byCss("#email", "email field");
		public static EnhancedBy PASSWORD_FIELD = Element.byCss("#password", "password field");		
		public static EnhancedBy ROLE_DROPDOWN = Element.byCss(".Select-control", "role panel");
		public static EnhancedBy ROLE_OPTIONS = Element.byCss(".Select-control", "role options");
		public static EnhancedBy CONFIRMED_ON = Element.byCss("#__ON__confirmed", "on confirmed");
		public static EnhancedBy CONFIRMED_OFF = Element.byCss("#__OFF__confirmed", "off confirmed");	
		public static EnhancedBy BLOCKED_ON = Element.byCss("#__ON__blocked", "on blocked");
		public static EnhancedBy BLOCKED_OFF = Element.byCss("#__OFF__blocked", "off blocked");
		public static EnhancedBy SAVE_BUTTON = Element.byCss("[type='submit']", "save button");
		
		// Users panel
		public static EnhancedBy USER_COLUMN_HEADERS = Element.byCss("th", "user columns");	
		public static EnhancedBy USER_ROWS = Element.byCss("tr[class*='TableRow']", "user rows");	
		public static EnhancedBy USER_ROW_CELLS = Element.byCss("td", "user row cells");	

	}

	// Actions
	//--------------------------------------------------------------------------------------------------------	
	/**
	 * enter user info And save changes
	 * 
	 * @param user
	 */
	public void addUser(User user) {
		Helper.clickAndExpect(elements.NEW_USER_BUTTON, elements.SAVE_BUTTON);
		setUserFields(user);
		Helper.form.formSubmit(elements.SAVE_BUTTON, MainPanel.elements.ADMIN_LOGO);
		Helper.waitForListItemToLoad_Contains(MainPanel.elements.USER_ROWS, user.getUsername());
	}
	
	public void editUser(User user, User editUser) {
		
		Helper.selectListItemContainsByName(MainPanel.elements.USER_ROWS, user.getUsername());
		setUserFields(editUser);
		Helper.form.formSubmit(elements.SAVE_BUTTON, MainPanel.elements.ADMIN_LOGO);
		Helper.waitForListItemToLoad_Contains(MainPanel.elements.USER_ROWS, editUser.getUsername());
	}

	public void setUserFields(User user) {
		Helper.form.clearAndSetField(elements.USER_NAME_FILED, user.getUsername());
		Helper.form.clearAndSetField(elements.EMAIL_FIELD, user.getEmail());
		Helper.form.clearAndSetField(elements.PASSWORD_FIELD, user.getPassword());
		
		Helper.form.selectToggle(elements.CONFIRMED_ON, elements.CONFIRMED_OFF, Boolean.valueOf(user.getConfirmed()));
		Helper.form.selectToggle(elements.BLOCKED_ON, elements.BLOCKED_OFF, Boolean.valueOf(user.getBlocked()));
	}
}