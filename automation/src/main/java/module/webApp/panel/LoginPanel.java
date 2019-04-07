package module.webApp.panel;
import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import data.webApp.User;
import module.common.data.CommonUser;
import moduleManager.module.webApp.PanelManager;

@Panel
public class LoginPanel {
	
	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public LoginPanel(PanelManager manager) {
		this.manager = manager;
	}

	// Locators
	//--------------------------------------------------------------------------------------------------------	
	public static class elements {
		// uses 2 locators. no limits to # of locators
		public static EnhancedBy USER_NAME_FIELD = Element.byCss("[placeholder='John Doe2']", "username field")
				.byCss("[placeholder='John Doe']");
		 
		public static EnhancedBy PASSWORD_FIELD = Element.byCss("#password", "password field");
		public static EnhancedBy LOGIN_SUBMIT = Element.byCss("[type='submit']", "submit button");
		public static EnhancedBy LOGOUT_BUTTON = Element.byCss("[href*='logout']", "logout button");
		public static EnhancedBy MAIN_SITE = Element.byCss(".main-site", "main site button");
		public static EnhancedBy ERROR_MESSAGE = Element.byCss("[class*='permissionserrorsContainer']", "input errors");
		public static EnhancedBy LOADING_INDICATOR = Element.byCss("[class*='Loading']", "loading indicator");
	}
	
	// Actions
	//--------------------------------------------------------------------------------------------------------	
	/**
	 * enter login info And click login button
	 * 
	 * @param user
	 */
	public void login(CommonUser user) {
		Helper.form.setField(elements.USER_NAME_FIELD, user.username);
		Helper.form.setField(elements.PASSWORD_FIELD, user.password);
		Helper.form.formSubmit(elements.LOGIN_SUBMIT, MainPanel.elements.ADMIN_LOGO, elements.LOADING_INDICATOR);
	}
	
	public void loginWithCsvData(User user) {
		setLoginFields(user);
		Helper.form.formSubmit(elements.LOGIN_SUBMIT, MainPanel.elements.ADMIN_LOGO, elements.LOADING_INDICATOR);
	}
	
	public void loginError(User user) {
		setLoginFields(user);
		Helper.form.formSubmit(elements.LOGIN_SUBMIT, elements.ERROR_MESSAGE);
	}
	
	public void relogin(CommonUser user) {
	    manager.main.logout();
		login(user);
	}

	public void setLoginFields(User user) {
		Helper.form.setField(elements.USER_NAME_FIELD, user.getUsername());
		Helper.form.setField(elements.PASSWORD_FIELD, user.getPassword());
	}
}