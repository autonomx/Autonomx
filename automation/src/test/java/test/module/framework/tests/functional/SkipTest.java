package test.module.framework.tests.functional;


import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.helpers.Helper;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class SkipTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		ConfigVariable.globalSkipTests().setValue("SkipTest-verifySkipTest");
	}
	
	@Test(description = "verify skip test")
	public void verifySkipTest() {
			
	}
	
	@AfterMethod
	public void afterMethod(ITestResult iTestResult)  {
		
		// status 3 is skip
		Helper.assertEquals(3, iTestResult.getStatus() );
	}
}