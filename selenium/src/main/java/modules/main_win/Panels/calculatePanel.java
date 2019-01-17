package modules.main_win.Panels;





import core.helpers.Element;
import core.helpers.Helper;
import core.uiCore.webElement.EnhancedBy;
import modules.main_win.winApp;

public class calculatePanel {
	
	winApp manager; 	
	public calculatePanel(winApp manager) {
		this.manager = manager;
	}
	
	public static class elements {
	    public static EnhancedBy ONE = Element.byName("One", "one button");
	    public static EnhancedBy TWO = Element.byName("Two", "two button");
	    public static EnhancedBy PLUS = Element.byName("Plus", "plus button");
	    public static EnhancedBy EQUALS = Element.byName("Equals", "equal button");
	    public static EnhancedBy RESULTS = Element.byAccessibility("CalculatorResults", "calculator results");

	}
	
	
	/**
	 * 
	 */
	public void calculate() {
		Helper.click.clickAndExpect(elements.ONE, elements.TWO);
		Helper.click.clickAndExpect(elements.PLUS, elements.TWO);
		Helper.click.clickAndExpect(elements.TWO, elements.EQUALS);
		Helper.click.clickAndExpect(elements.EQUALS, elements.RESULTS);
		verifyResults("3");
	}
	
    protected void verifyResults(String val)
    {
        // trim extra text and whitespace off of the display value
    	
       String result =  Helper.form.getTextValue(elements.RESULTS).replace("Display is", "").trim();
       Helper.assertEquals(val, result);
    }

}