package test.module.win.tests;




import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.support.logger.TestLog;
import moduleManager.module.ModuleBase;



public class VerifyCalculatorTest extends ModuleBase {

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