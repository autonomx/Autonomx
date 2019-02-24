package module.iosApp.panels;

import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import module.iosApp.objects.ListSectionsObject;
import moduleManager.module.iosApp.PanelManager;

@Panel
public class ListSections {

	// ensure the proper panel manager is imported, containing project "module name".
	//eg. moduleManager.module.<module name>.PanelManager
	PanelManager manager;
	public ListSections(PanelManager manager) {
		this.manager = manager;
	}

// Locators
//--------------------------------------------------------------------------------------------------------		
	public static class elements{
		
		// dynamic element
		public EnhancedBy question(String question) {
			return Element.byAccessibility(question, question + " field");
		}
		
		// static element
		public static EnhancedBy BACK = Element.byAccessibility("Examples", "examples link");

	}
	
// Actions
//--------------------------------------------------------------------------------------------------------	
	public void fillForm(ListSectionsObject form) {
		setForm(form);
		Helper.clickAndExpect(elements.BACK, MainPanel.elements.EUREKA);
	}

	public void setForm(ListSectionsObject form) {
		elements elements = new elements();
		
		Helper.form.selectCheckBox(elements.question(form.whereYouLive().get()), true);
		Helper.form.selectCheckBox(elements.question(form.oceans().get()), true);
	}
}