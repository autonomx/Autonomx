package test.module.framework.tests.functional.Helper;


import java.io.File;
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
	
	@Test()
	public void isFileContainsStringTest() {
		File file = new File(Helper.getRootDir() + "src/main/java/module/common/data/CommonUser.java");
		boolean isExist = Helper.isFileContainsString("@Panel", file);
		Helper.assertTrue("", !isExist);
		
		isExist = Helper.isFileContainsString("@Data", file);
		Helper.assertTrue("", isExist);
		
		isExist = Helper.isFileContainsString("//@Data", file);
		Helper.assertTrue("", !isExist);
		
		isExist = Helper.isFileContainsString("@Data //", file);
		Helper.assertTrue("", !isExist);
	}
	
	@Test()
	public void isServerOnlineTest() {
		Helper.assertTrue("http://demo.autonomx.io is offline",Helper.isServerOnline("http://demo.autonomx.io"));
		
		Helper.assertTrue("http://demo.autonomx.ca is online",!Helper.isServerOnline("http://demo.autonomx.ca"));
	}
}