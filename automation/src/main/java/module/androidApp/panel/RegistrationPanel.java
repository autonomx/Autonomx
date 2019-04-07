package module.androidApp.panel;

import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import module.common.data.CommonUser;
import moduleManager.module.androidApp.PanelManager;

@Panel
public class RegistrationPanel {

	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public RegistrationPanel(PanelManager manager) {
		this.manager = manager;
	}

	// Locators
	//--------------------------------------------------------------------------------------------------------	
	public static class elements {
		public static final String androidApp = "io.selendroid.testapp:id/";
		public static EnhancedBy USERNAME_FIELD = Element.byId(androidApp + "inputUsername", "username field");
		public static EnhancedBy EMAIL_FIELD = Element.byAccessibility("email of the customer", "email field");
		public static EnhancedBy PASSWORD_FIELD = Element.byId(androidApp + "inputPassword", "password field");
		public static EnhancedBy NAME_FIELD = Element.byId(androidApp + "inputName", "name2 field");
		public static EnhancedBy PROGRAMMING_LANGUAGE_FIELD = Element.byId(androidApp + "input_preferedProgrammingLanguage", "language field");
		public static EnhancedBy PROGRAMMING_LANGUAGE_OPTIONS = Element.byClass("android.widget.CheckedTextView", "language options");

		public static EnhancedBy ACCEPT_ADS = Element.byId(androidApp + "input_adds", "accept ads");
		public static EnhancedBy REGISTER_VERIFY = Element.byId(androidApp + "btnRegisterUser", "register verify");
		public static EnhancedBy REGISTER_USER = Element.byId(androidApp + "buttonRegisterUser", "register user");

		
	}

	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void setField(CommonUser user) {
		

		Helper.form.setField(elements.USERNAME_FIELD, user.username);
		Helper.form.setField(elements.EMAIL_FIELD, user.email);
		Helper.form.setField(elements.PASSWORD_FIELD, user.password);
		Helper.form.setField(elements.NAME_FIELD, user.name);
		Helper.form.selectDropDown("Java", elements.PROGRAMMING_LANGUAGE_FIELD, elements.PROGRAMMING_LANGUAGE_OPTIONS);
		Helper.form.selectCheckBox(elements.ACCEPT_ADS, user.acceptAds);
	
		Helper.form.formSubmit(elements.REGISTER_VERIFY, elements.REGISTER_USER);
		Helper.form.formSubmit(elements.REGISTER_USER, MainPanel.elements.REGISTER_PANEL);

	}

	public void registerUser(CommonUser user) {
		setField(user);
	}
}