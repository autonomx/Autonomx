package test.module.framework.tests.functional.Helper;


import org.testng.annotations.Test;

import core.helpers.Helper;
import test.module.framework.TestBase;

/*
 * prerequisite:
 * global.timeout.seconds should not be 90
 * appium.logging = false
 * appium.logging.level = info 
 */
public class AssertHelperTest extends TestBase {
	
	
	@Test
	public void softAssertTest() {
		String value1 = "value1";
		String value2 = "value2";
		String value3 = "value3";

		Helper.softAssertEqual("value1", value1);
		Helper.softAssertEqual("value2", value2);
		Helper.softAssertEqual("value2", value3);
		Helper.softAssertEqual("value4", value3);

		try {
		 Helper.softAssertAll();
		}catch(AssertionError error) {
			Helper.assertEquals("The following asserts failed:\n" + 
					"	expected [value2] but found [value3],\n" + 
					"	expected [value4] but found [value3]", error.getMessage());
		}
	}
	
	@Test
	public void softAssertTest_all_api() {
		String value1 = "value1";
		String value2 = "value2";
		String value3 = "value3";

		Helper.softAssert().assertEquals("value1", value1);
		Helper.softAssert().assertEquals("value2", value2);
		Helper.softAssert().assertEquals("value2", value3);
		Helper.softAssert().assertEquals("value4", value3);

		try {
		 Helper.softAssertAll();
		}catch(AssertionError error) {
			Helper.assertEquals("The following asserts failed:\n" + 
					"	expected [value3] but found [value2],\n" + 
					"	expected [value3] but found [value4]", error.getMessage());
		}
	}
	
	
}