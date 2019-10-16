package test.module.framework.tests.functional;


import java.lang.reflect.Method;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import core.support.objects.TestObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class DefaultTestObjectTest extends TestBase {
	
	
	@BeforeSuite
	public void beforeSuite()  {
		String testName = TestObject.getDefaultTestInfo().testId;
		Helper.assertTrue("wrong default test id", testName.equals(TestObject.DEFAULT_TEST));
	}
	
	@BeforeClass
	public void beforeClass()  {
		String testName = TestObject.getDefaultTestInfo().testId;
		Helper.assertTrue("wrong default test id", testName.equals(TestObject.DEFAULT_TEST));

	}
	
	@BeforeMethod()
	public void beforeMethod(Method method) {
		String testName = TestObject.getDefaultTestInfo().testId;
		Helper.assertTrue("wrong default test id", testName.contains(TestObject.DEFAULT_TEST));
	}
	
	@AfterClass
	public void afterClass()  {
		String testName = TestObject.getDefaultTestInfo().testId;
		Helper.assertTrue("wrong default test id", testName.equals(TestObject.DEFAULT_TEST));
	}
	
	@AfterSuite
	public void afterSuite()  {
		String testName = TestObject.getDefaultTestInfo().testId;
		Helper.assertTrue("wrong default test id", testName.equals(TestObject.DEFAULT_TEST));
	}
	
	@Test(description = "verify test object is named based on thread count")
	public void verifyDefaultTestObject() {
		TestLog.When("I run a test using default test object in thread 1");
		String testName = TestObject.getDefaultTestInfo().testId;
		Helper.assertTrue("wrong default test id", testName.contains(TestObject.DEFAULT_TEST ));
	}
}