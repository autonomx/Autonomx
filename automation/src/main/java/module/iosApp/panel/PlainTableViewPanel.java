package module.iosApp.panel;

/**
 * Rebuild or clean project after adding new panel or removing to generate associated files
 */
import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import module.iosApp.data.PlainTableViewObject;
import moduleManager.module.iosApp.PanelManager;

@Panel
public class PlainTableViewPanel {

	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public PlainTableViewPanel(PanelManager manager) {
		this.manager = manager;
	}

	// Locators
	//--------------------------------------------------------------------------------------------------------	
	public static class elements {
		public static EnhancedBy NAME = Element.byAccessibility("Name", "name field");
		public static EnhancedBy USER_NAME = Element.byAccessibility("Username", "username field");
		public static EnhancedBy EMAIL_ADDRESS = Element.byAccessibility("Email Address", "email address filed");
		public static EnhancedBy PASSWORD = Element.byAccessibility("Password", "password field");
		public static EnhancedBy BACK = Element.byAccessibility("Examples", "examples link");

	}
	
	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void fillForm(PlainTableViewObject form) {
		setForm(form);
		Helper.clickAndExpect(elements.BACK, MainPanel.elements.PLAIN_TABLE_VIEW_STYLE);
	}

	public void setForm(PlainTableViewObject form) {

		Helper.form.setField(elements.NAME, form.name);
		Helper.form.setField(elements.USER_NAME, form.username);
		Helper.form.setField(elements.EMAIL_ADDRESS, form.emailAddress);
		Helper.form.setField(elements.PASSWORD, form.password);
	}
}