package test.module.framework.tests.functional.service;


import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.apiCore.TestDataProvider;
import core.helpers.Helper;
import core.support.logger.TestLog;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
import core.uiCore.drivers.AbstractDriverTestNG;
import serviceManager.ServiceRunner;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ServiceRunnerRetryTest extends TestBase {
	
	@BeforeClass
	public void beforeClass()  {
		// override retry count using the test case id
		ConfigVariable.globalRetryCount().setValue(1);
	}
	
	@BeforeMethod()
	public void beforeMethod(Method method) {
		TestDataProvider.csvFileIndex.set(0);
	}
	
		// verifying test retry on service runner
		@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
		public void verifyApiRunner_retryCount(Object objects) throws Exception {
			
			
			Object[] object2 = (Object[]) objects;
			
			TestLog.When("I verify api runner test");
		
			// setup api driver to get test case id
			ServiceObject apiObject = new ServiceObject().setServiceObject(object2); 
			new AbstractDriverTestNG().setupApiDriver(apiObject); 

	        int runCount = TestObject.getTestInfo().runCount;
	        Helper.assertTrue("run count should start at 1", runCount != 0);
	     
	        Object[] object = object2.clone();
	        object[9] = "";
	        object[11] = "";
	        object[13] = "600";
	        
	        if(runCount == 1) {
	        	Helper.assertEquals(0, TestDataProvider.csvFileIndex.get());
	        	ServiceRunner.TestRunner(object);

	    	}else if(runCount == 2) {
	    		Helper.assertEquals(0, TestDataProvider.csvFileIndex.get());
	    		
	    		ServiceRunner.TestRunner(object2);
	    	}
		}
}