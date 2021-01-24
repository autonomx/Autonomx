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
		public static EnhancedBy NEW_USER_BUTTON = Element.byCss("button[color='Primary']", "add new user");
		
		// add/edit user panel
		public static EnhancedBy USER_NAME_FILED = Element.byCss("#username", "username field");
		public static EnhancedBy EMAIL_FIELD = Element.byCss("#email", "email field");
		public static EnhancedBy PASSWORD_FIELD = Element.byCss("#password", "password field");		
		public static EnhancedBy ROLE_DROPDOWN = Element.byCss(".Select-control", "role panel");
		public static EnhancedBy ROLE_OPTIONS = Element.byCss(".Select-control", "role options");
		public static EnhancedBy CONFIRMED_ON = Element.byId("confirmed", "on confirmed");
		public static EnhancedBy CONFIRMED_OFF = Element.byCss("#confirmed", "off confirmed");	
		public static EnhancedBy BLOCKED_ON = Element.byCss("#blocked", "on blocked");
		public static EnhancedBy BLOCKED_OFF = Element.byCss("#blocked", "off blocked");
		public static EnhancedBy SAVE_BUTTON = Element.byCss("[type='submit']", "save button");
		public static EnhancedBy SAVE_SUCCESS = Element.byCss("[title='Saved']", "save sucess indicator");

		// Users panel
		public static EnhancedBy USER_ROWS = Element.byCss("tr", "user rows");	
		public static EnhancedBy USER_COLUMN_HEADERS = Element.byCss("th", "user columns");		

		// filter 
		public static EnhancedBy FILTER_BUTTON = Element.byXpath("//*[contains(text(), 'Filters')]", "filter button");		
		public static EnhancedBy FILTER_TYPE_DROPDOWN = Element.byCss("[id*='.name']", "filter type");		
		public static EnhancedBy FILTER_TYPE_DROPDOWN_OPTION = Element.byCss("[id*='.name'] option", "filter type options");		
		public static EnhancedBy FILTER_TEXT_FIELD = Element.byCss("[id*='.value']", "filter text field");		
		public static EnhancedBy FILTER_SUBMIT_BUTTON = Element.byCss("[type='submit']", "filter submit button");		
		public static EnhancedBy FILTER_REMOVE_BUTTON = Element.byCss("[class*='remove__admin']", "filter remove button");		

	// Actions
	//--------------------------------------------------------------------------------------------------------	
	/**
	 * enter user info And save changes
	 * 
	 * @param user
	 */
	public void addUser(CommonUser user) {
		Helper.clickAndExpect(NEW_USER_BUTTON, SAVE_BUTTON);
		setUserFields(user);
		Helper.form.formSubmit(SAVE_BUTTON, MainPanel.ADMIN_LOGO);
	}
	
	public void editUser(CommonUser user, CommonUser editUser) {
		
		Helper.selectListItemContainsByName(USER_ROWS, user.username);
		setUserFields(editUser);
		Helper.form.formSubmit(SAVE_BUTTON, SAVE_SUCCESS);
	}

	public void setUserFields(CommonUser user) {
		Helper.form.clearAndSetField(USER_NAME_FILED, user.username);
		Helper.form.clearAndSetField(EMAIL_FIELD, user.email);
		Helper.form.clearAndSetField(PASSWORD_FIELD, user.password);

		Helper.form.selectToggle(CONFIRMED_ON, CONFIRMED_OFF, user.confirmed);
		Helper.form.selectToggle(BLOCKED_ON, BLOCKED_OFF, user.blocked);
	}
	
	public void filterUsers(CommonUser user) {
		Helper.clickAndExpect(FILTER_BUTTON, FILTER_TYPE_DROPDOWN);
		Helper.selectDropDown(FILTER_TYPE_DROPDOWN, FILTER_TYPE_DROPDOWN_OPTION, "username");
		Helper.setField(FILTER_TEXT_FIELD, user.username);
		Helper.formSubmit(FILTER_SUBMIT_BUTTON, USER_ROWS);
	}
	
	public void removeFilter() {
		Helper.clickAndExpect(FILTER_REMOVE_BUTTON, USER_ROWS);
	}
}