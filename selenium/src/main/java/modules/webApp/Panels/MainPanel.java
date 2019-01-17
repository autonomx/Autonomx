package modules.webApp.Panels;

import core.helpers.Element;
import core.helpers.Helper;
import core.uiCore.webElement.EnhancedBy;
import modules.webApp.StrapiPanel;

public class MainPanel {

	StrapiPanel manager;

	public MainPanel(StrapiPanel manager) {
		this.manager = manager;
	}

	public static class elements {

		// using wildcard to find element
		public static EnhancedBy ADMIN_LOGO = Element.byCss("[class*='adminprojectName__admin']", "admin logo");
		public static EnhancedBy USERINFO_DROPDOWN = Element.byCss(".fa-caret-down", "userinfo dropdown");
		public static EnhancedBy SIGNOUT_OPTION = Element.byCss(".fa-sign-out", "signout option");
	}
	
	public void logout() {
		Helper.selectDropDown(elements.USERINFO_DROPDOWN, elements.SIGNOUT_OPTION);
		Helper.waitForElementToLoad(LoginPanel.elements.USER_NAME_FIELD);
	}

}