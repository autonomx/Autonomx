package module.iosApp.panel;

/**
 * Rebuild or clean project after adding new panel to generate associated files
 */
import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import module.iosApp.data.ListSectionsObject;
import moduleManager.module.iosApp.PanelManager;

@Panel
public class ListSections {

	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public ListSections(PanelManager manager) {
		this.manager = manager;
	}

// Locators
//--------------------------------------------------------------------------------------------------------		
		
	// dynamic element
	public static EnhancedBy question(String question) {
		return Element.byAccessibility(question, question + " field");
	}
	
	// static element
	public static EnhancedBy BACK = Element.byAccessibility("Examples", "examples link");

	
// Actions
//--------------------------------------------------------------------------------------------------------	
	public void fillForm(ListSectionsObject form) {
		setForm(form);
		Helper.clickAndExpect(BACK, MainPanel.EUREKA);
	}

	public void setForm(ListSectionsObject form) {
		
		Helper.form.selectCheckBox(question(form.whereYouLive), true);
		Helper.form.selectCheckBox(question(form.oceans), true);
	}
}