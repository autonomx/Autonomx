package module.webApp.panel;

import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import module.common.data.CommonUser;
import moduleManager.module.webApp.PanelManager;

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
		public static EnhancedBy NEW_USER_BUTTON = Element.byCss("[label*='addAnEntry']", "add new user");
		
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
		public static EnhancedBy USER_ROWS = Element.byCss("tr[class*='TableRow']", "user rows");		
	}

	// Actions
	//--------------------------------------------------------------------------------------------------------	
	/**
	 * enter user info And save changes
	 * 
	 * @param user
	 */
	public void addUser(CommonUser user) {
		Helper.clickAndExpect(elements.NEW_USER_BUTTON, elements.SAVE_BUTTON);
		setUserFields(user);
		Helper.form.formSubmit(elements.SAVE_BUTTON, MainPanel.elements.ADMIN_LOGO);
		Helper.waitForListItemToLoad_Contains(elements.USER_ROWS, user.username);
	}
	
	public void editUser(CommonUser user, CommonUser editUser) {
		
		Helper.selectListItemContainsByName(elements.USER_ROWS, user.username);
		setUserFields(editUser);
		Helper.form.formSubmit(elements.SAVE_BUTTON, MainPanel.elements.ADMIN_LOGO);
		Helper.waitForListItemToLoad_Contains(elements.USER_ROWS, editUser.username);
	}

	public void setUserFields(CommonUser user) {
		Helper.form.clearAndSetField(elements.USER_NAME_FILED, user.username);
		Helper.form.clearAndSetField(elements.EMAIL_FIELD, user.email);
		Helper.form.clearAndSetField(elements.PASSWORD_FIELD, user.password);
		
		Helper.form.selectToggle(elements.CONFIRMED_ON, elements.CONFIRMED_OFF, user.confirmed);
		Helper.form.selectToggle(elements.BLOCKED_ON, elements.BLOCKED_OFF, user.blocked);
	}
}