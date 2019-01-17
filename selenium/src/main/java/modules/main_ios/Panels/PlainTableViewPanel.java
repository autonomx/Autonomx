package modules.main_ios.Panels;

import core.helpers.Element;
import core.helpers.Helper;
import core.uiCore.webElement.EnhancedBy;
import modules.main_ios.EurekaIos;
import modules.main_ios.objects.PlainTableViewObject;

public class PlainTableViewPanel {

	EurekaIos manager;

	public PlainTableViewPanel(EurekaIos manager) {
		this.manager = manager;

	}

	public static class elements {
		public static EnhancedBy NAME = Element.byAccessibility("Name", "name field");
		public static EnhancedBy USER_NAME = Element.byAccessibility("Username", "username field");
		public static EnhancedBy EMAIL_ADDRESS = Element.byAccessibility("Email Address", "email address filed");
		public static EnhancedBy PASSWORD = Element.byAccessibility("Password", "password field");
		public static EnhancedBy BACK = Element.byAccessibility("Examples", "examples link");

	}
	
	public void fillForm(PlainTableViewObject form) {
		setForm(form);
		Helper.clickAndExpect(elements.BACK, MainPanel.elements.PLAIN_TABLE_VIEW_STYLE);
	}

	public void setForm(PlainTableViewObject form) {

		Helper.form.setField(elements.NAME, form.name().get());
		Helper.form.setField(elements.USER_NAME, form.username().get());
		Helper.form.setField(elements.EMAIL_ADDRESS, form.emailAddress().get());
		Helper.form.setField(elements.PASSWORD, form.password().get());
	}
}