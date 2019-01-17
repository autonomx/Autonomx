package modules.main_android.Panels;


import core.helpers.Element;
import core.helpers.Helper;
import core.uiCore.webElement.EnhancedBy;
import modules.main_android.SelenoidAndroid;

public class MainPanel {

	SelenoidAndroid manager;

	public MainPanel(SelenoidAndroid manager) {
		this.manager = manager;

	}

	public static class elements {
		public static EnhancedBy REGISTER_PANEL = Element.byAccessibility("startUserRegistrationCD", "registration button");
		public static EnhancedBy POPUP_BUTTON = Element.byAccessibility("showPopupWindowButtonCD", "popup button");

	}

	public void selectRegisterPanel() {
		Helper.clickAndExpect(elements.REGISTER_PANEL, RegistrationPanel.elements.USERNAME_FIELD);
	}
}