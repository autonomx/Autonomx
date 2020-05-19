package module.webApp.panel;

/**
 * Rebuild or clean project after adding new panel or removing to generate associated files
 */
import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import moduleManager.module.webApp.PanelManager;

@Panel
public class SidePanel{
	
	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public SidePanel(PanelManager manager) {
		this.manager = manager;
	}

	public enum Panels {
		USERS, CONTENT_MANAGER, CONTENT_TYPE_BUILDER, FILE_UPLOAD, ROLES_PERMISSIONS
	}

	// Locators
	//--------------------------------------------------------------------------------------------------------	
	// side panel options
	public static EnhancedBy USERS = Element.byCss("[href*='user?source=users-permissions']", "users content type");
	public static EnhancedBy CONENT_MANAGER = Element.byCss("[href*='ctm-configurations']", "content manager");
	public static EnhancedBy CONTENT_TYPE_BUILDER = Element.byCss("[href*='content-type-builder']", "content type builder");
	public static EnhancedBy UPLOAD = Element.byCss("[href*='upload']", "upload");
	public static EnhancedBy ROlES_PERMISSIONS = Element.byCss("[href*='plugins/users-permissions']", "roles And permissions");

	
	// Actions
	//--------------------------------------------------------------------------------------------------------	
	public void selectPanel(Panels options) {

		switch (options) {
		case USERS:
			Helper.clickAndExpect(USERS, UserPanel.NEW_USER_BUTTON);
			break;
		case CONTENT_MANAGER:
			break;
		case CONTENT_TYPE_BUILDER:
			break;
		case FILE_UPLOAD:
			break;
		case ROLES_PERMISSIONS:
			break;
		default:
			throw new IllegalStateException("Unsupported option " + options.toString());
		}
	}
}