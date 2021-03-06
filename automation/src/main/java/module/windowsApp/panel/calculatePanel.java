package module.windowsApp.panel;





import core.helpers.Element;
import core.helpers.Helper;
import core.support.annotation.Panel;
import core.uiCore.webElement.EnhancedBy;
import moduleManager.module.windowsApp.PanelManager;

@Panel
public class calculatePanel {
	
	// ensure the proper panel manager is imported, containing project "test.module name2".
	//eg. moduleManager.module.<test.module name2>.PanelManager
	PanelManager manager;
	public calculatePanel(PanelManager manager) {
		this.manager = manager;
	}
	
	// Locators
	//--------------------------------------------------------------------------------------------------------	
	public static class elements {
	    public static EnhancedBy ONE = Element.byName("One", "one button");
	    public static EnhancedBy TWO = Element.byName("Two", "two button");
	    public static EnhancedBy PLUS = Element.byName("Plus", "plus button");
	    public static EnhancedBy EQUALS = Element.byName("Equals", "equal button");
	    public static EnhancedBy RESULTS = Element.byAccessibility("CalculatorResults", "calculator results");

	}
	
	
	// Actions
	//--------------------------------------------------------------------------------------------------------	
	/**
	 * 
	 */
	public void calculate() {
		
		Helper.clickAndWait(elements.ONE, 0);
		Helper.clickAndWait(elements.PLUS, 0);
		Helper.clickAndWait(elements.TWO, 0);
		Helper.clickAndWait(elements.EQUALS, 0);
		verifyResults("3");
	}
	
    protected void verifyResults(String val)
    {
        // trim extra text And whitespace off of the display value
    	
       String result =  Helper.form.getTextValue(elements.RESULTS).replace("Display is", "").trim();
       Helper.assertEquals(val, result);
    }

}