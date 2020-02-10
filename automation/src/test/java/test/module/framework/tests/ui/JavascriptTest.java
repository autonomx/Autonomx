package test.module.framework.tests.ui;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import module.framework.panel.LoginPanel;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class JavascriptTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		setupWebDriver(app.framework.getWebDriver());
	}
	
	@Test
	public void setAttributeTest() {
		Helper.setAttribute(LoginPanel.elements.USER_NAME_FIELD, "value", "sample name");
		String value = Helper.getAttribute(LoginPanel.elements.USER_NAME_FIELD, "value");
		
		Helper.assertEquals("sample name", value);
	}
}