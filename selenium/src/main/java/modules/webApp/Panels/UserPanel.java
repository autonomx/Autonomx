package modules.webApp.Panels;

import common.objects.UserObject;
import core.helpers.Element;
import core.helpers.Helper;
import core.uiCore.webElement.EnhancedBy;
import modules.webApp.StrapiPanel;

public class UserPanel {

	StrapiPanel manager;

	public UserPanel(StrapiPanel manager) {
		this.manager = manager;

	}

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

	/**
	 * enter user info and save changes
	 * 
	 * @param user
	 */
	public void addUser(UserObject user) {
		Helper.clickAndExpect(elements.NEW_USER_BUTTON, elements.SAVE_BUTTON);
		setUserFields(user);
		Helper.form.formSubmit(elements.SAVE_BUTTON, MainPanel.elements.ADMIN_LOGO);
		Helper.waitForListItemToLoad_Contains(elements.USER_ROWS, user.username().get());
	}
	
	public void editUser(UserObject user, UserObject editUser) {
		Helper.selectListItemContainsByName(elements.USER_ROWS, user.username().get());
		setUserFields(editUser);
		Helper.form.formSubmit(elements.SAVE_BUTTON, MainPanel.elements.ADMIN_LOGO);
		Helper.waitForListItemToLoad_Contains(elements.USER_ROWS, editUser.username().get());
	}

	public void setUserFields(UserObject user) {
		Helper.form.setField(elements.USER_NAME_FILED, user.username().get());
		Helper.form.setField(elements.EMAIL_FIELD, user.email().get());
		Helper.form.setField(elements.PASSWORD_FIELD, user.password().get());
		
		Helper.form.selectToggle(elements.CONFIRMED_ON, elements.CONFIRMED_OFF, user.confirmed().get());
		Helper.form.selectToggle(elements.BLOCKED_ON, elements.BLOCKED_OFF, user.blocked().get());
	}
}