package modules.win;



import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.support.logger.TestLog;
import modules.TestBase;



public class VerifyCalculatorTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.win.getDriver());
	}
	
	
	
	@Test(enabled=true) 
	public void validateCalculator() {

		TestLog.When("I calculate 2 numbers");
		app.win.cal.calculate();
	}
}