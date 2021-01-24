package module.framework.panel;

import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import data.framework.User;
import data.framework.UserInvalid;
import module.webApp.panel.MainPanel;
import moduleManager.module.framework.PanelManager;

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
	public static EnhancedBy PASSWORD_LABEL = Element.byCss("[for='password']", "username label");
	public static EnhancedBy USERNAME_LABEL = Element.byCss("[for='email']", "password label");
	public static EnhancedBy INVALID_INPUT_MESSAGE = Element.byCss("#error-email", "invalid email");
	public static EnhancedBy FORGOT_PASSWORD = Element.byCss("[href *='forgot-password']", "forgot password");

		
	
	public static EnhancedBy EMAIL_FIELD = Element.byCss("#email", "email field");
	 
	public static EnhancedBy PASSWORD_FIELD = Element.byCss("#password", "password field");
	public static EnhancedBy LOGIN_SUBMIT = Element.byCss("[type='submit']", "submit button");
	public static EnhancedBy LOGOUT_BUTTON = Element.byCss("[href*='logout']", "logout button");
	public static EnhancedBy MAIN_SITE = Element.byCss(".main-site", "main site button");
	public static EnhancedBy ERROR_MESSAGE = Element.byCss("#error-email", "input errors");
	public static EnhancedBy LOADING_INDICATOR = Element.byCss("[class*='Loading']", "loading indicator");	

	// Actions
	//--------------------------------------------------------------------------------------------------------	
public void login(User user) {
		
		Helper.loginbuilder
		.builder()
		.withUsername(EMAIL_FIELD, user.getUsername())
		.withPassword(PASSWORD_FIELD, user.getPassword())
		.withFormSubmit(LOGIN_SUBMIT, MainPanel.ADMIN_LOGO)
		.build();
	}

	public void loginWithCsvData(User user) {
		login(user);
	}
	
	public void loginError(UserInvalid user) {
	
		Helper.loginbuilder
		.builder()
		.withUsername(EMAIL_FIELD, user.getUsername())
		.withPassword(PASSWORD_FIELD, user.getPassword())
		.withFormSubmit(LOGIN_SUBMIT, ERROR_MESSAGE)
		.build();
	}
	
	public void relogin(User user) {
	    manager.main.logout();
		login(user);
	}
}