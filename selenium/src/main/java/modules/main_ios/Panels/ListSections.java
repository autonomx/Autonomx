package modules.main_ios.Panels;

import core.helpers.Element;
import core.helpers.Helper;
import core.uiCore.webElement.EnhancedBy;
import modules.main_ios.EurekaIos;
import modules.main_ios.objects.ListSectionsObject;

public class ListSections {

	EurekaIos manager;

	public ListSections(EurekaIos manager) {
		this.manager = manager;

	}

	
	public static class elements{
		
		// dynamic element
		public EnhancedBy question(String question) {
			return Element.byAccessibility(question, question + " field");
		}
		
		// static element
		public static EnhancedBy BACK = Element.byAccessibility("Examples", "examples link");

	}
	
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