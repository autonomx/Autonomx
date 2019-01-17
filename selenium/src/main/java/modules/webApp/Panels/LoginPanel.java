package modules.webApp.Panels;

import common.objects.UserObject;
import core.helpers.Element;
import core.helpers.Helper;
import core.uiCore.webElement.EnhancedBy;
import modules.webApp.StrapiPanel;

public class LoginPanel {

	StrapiPanel manager;

	public LoginPanel(StrapiPanel manager) {
		this.manager = manager;

	}

	public static class elements {
		public static EnhancedBy USER_NAME_FIELD = Element.byCss("[placeholder='John Doe']", "username field");
		public static EnhancedBy PASSWORD_FIELD = Element.byCss("#password", "password field");
		public static EnhancedBy LOGIN_SUBMIT = Element.byCss("[type='submit']", "submit button");
		public static EnhancedBy LOGOUT_BUTTON = Element.byCss("[href*='logout']", "logout button");
		public static EnhancedBy MAIN_SITE = Element.byCss(".main-site", "main site button");
		public static EnhancedBy ERROR_MESSAGE = Element.byCss("[class*='InputErrors']", "input errors");
		
		public static EnhancedBy LOADING_INDICATOR = Element.byCss("[class*='Loading']", "loading indicator");

	}

	/**
	 * enter login info and click login button
	 * 
	 * @param user
	 */
	public void login(UserObject user) {
		setLoginFields(user);
		Helper.form.formSubmit(elements.LOGIN_SUBMIT, MainPanel.elements.ADMIN_LOGO, elements.LOADING_INDICATOR);

	}

	public void loginError(UserObject user) {
		setLoginFields(user);
		Helper.form.formSubmit(elements.LOGIN_SUBMIT, elements.ERROR_MESSAGE);
	}

	public void relogin(UserObject user) {
		manager.main.logout();
		login(user);
	}

	public void setLoginFields(UserObject user) {
		Helper.form.setField(elements.USER_NAME_FIELD, user.username().get());
		Helper.form.setField(elements.PASSWORD_FIELD, user.password().get());
	}
}