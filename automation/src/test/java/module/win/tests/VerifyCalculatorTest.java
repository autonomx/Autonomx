package module.win.tests;




import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.support.logger.TestLog;
import module.TestBase;



public class VerifyCalculatorTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.windowsApp.getWinAppDriver());
	}
	
	
	
	@Test(enabled=true) 
	public void validateCalculator() {

		TestLog.When("I calculate 2 numbers");
		app.windowsApp.calculate.calculate();
	}
}