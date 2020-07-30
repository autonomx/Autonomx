package test.module.framework.tests.ui;


import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.objects.TestObject;
import core.uiCore.drivers.AbstractDriver;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class WebPropertiesTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
	}
	
	@Test
	public void verifyDriverVersion() throws Exception {
		Config.putValue("web.browserType ", "CHROME");
		Config.putValue("web.driver.manager.version", "70.0.3538.16");
		Config.setGlobalValue("web.driver.manager.version", "70.0.3538.16");

		setupWebDriver(app.framework.getWebDriver());
		Helper.assertEquals("70.0.3538.16", TestObject.getTestInfo().currentDriver.driverVersion);
		Capabilities caps = ((RemoteWebDriver) AbstractDriver.getWebDriver()).getCapabilities();
	
		 @SuppressWarnings("unchecked")
		Map<String, String> a = (Map<String, String>) caps.getCapability("chrome");
		
		 Helper.assertTrue("version not match: actual: " + a.get("chromedriverVersion") + " .expected 70.0.3538.16" , a.get("chromedriverVersion").contains("70.0.3538.16"));
	}
}