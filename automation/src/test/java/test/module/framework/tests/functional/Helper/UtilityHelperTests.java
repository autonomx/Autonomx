package test.module.framework.tests.functional.Helper;


import java.net.URL;

import org.testng.annotations.Test;

import core.helpers.Helper;
import test.module.framework.TestBase;

/*
 * prerequisite:
 * global.timeout.seconds should not be 90
 * appium.logging = false
 * appium.logging.level = info 
 */
public class UtilityHelperTests extends TestBase {
	
	
	
	
	
	@Test
	public void convertToUrl_valid() {
		URL aURL = Helper.convertToUrl("http://example.com:80/docs/books/tutorial"
                + "/index.html?name=networking#DOWNLOADING");

		Helper.assertEquals("http", aURL.getProtocol());
		Helper.assertEquals("example.com", aURL.getHost());
		Helper.assertEquals(80, aURL.getPort());
		Helper.assertEquals("/docs/books/tutorial/index.html", aURL.getPath());
		Helper.assertEquals("name=networking", aURL.getQuery());
	}
	
	@Test
	public void convertToUrl_validMultipleQuery() {
		URL aURL = Helper.convertToUrl("http://example.com:80/docs/books/tutorial"
                + "/index.html?name=networking&address=home");

		Helper.assertEquals("name=networking&address=home", aURL.getQuery());
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void convertToUrl_invalid() {
		Helper.convertToUrl("example.com:80/docs/books/tutorial"
                + "/index.html?name=networking&address=home");
	}
	
	
}