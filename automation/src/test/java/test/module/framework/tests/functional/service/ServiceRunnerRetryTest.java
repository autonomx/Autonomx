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
	public void verifyApiRunner_retryCount(String TestSuite, String TestCaseID, String RunFlag, String Description,
			String InterfaceType, String UriPath, String ContentType, String Method, String Option,
			String RequestHeaders, String TemplateFile, String RequestBody, String OutputParams, String RespCodeExp,
			String ExpectedResponse, String TcComments,
			String tcName, String tcIndex, String testType, Object serviceSteps) throws Exception {
		
		
		TestLog.When("I verify api runner test");
	
		// setup api driver to get test case id
		ServiceObject apiObject = new ServiceObject().setServiceObject(TestSuite, TestCaseID, RunFlag, Description, InterfaceType, 
		UriPath, ContentType, Method, Option, RequestHeaders, TemplateFile, RequestBody, OutputParams, 
		RespCodeExp, ExpectedResponse, TcComments, tcName, 
		tcIndex, testType, serviceSteps); 
		new AbstractDriverTestNG().setupApiDriver(apiObject); 

        int runCount = TestObject.getTestInfo().runCount;
        Helper.assertTrue("run count should start at 1", runCount != 0);
     
        if(runCount == 1) {
        	Helper.assertEquals(0, TestDataProvider.csvFileIndex.get());
        	ServiceRunner.TestRunner(TestSuite, TestCaseID, RunFlag, Description, InterfaceType, UriPath, ContentType, Method,
    				Option, "", TemplateFile, "", OutputParams, "600", ExpectedResponse,
    				TcComments, tcName, tcIndex, testType, serviceSteps);

    	}else if(runCount == 2) {
    		Helper.assertEquals(0, TestDataProvider.csvFileIndex.get());
    		
    		ServiceRunner.TestRunner(TestSuite, TestCaseID, RunFlag, Description, InterfaceType, UriPath, ContentType, Method,
    				Option, RequestHeaders, TemplateFile, RequestBody, OutputParams, RespCodeExp, ExpectedResponse,
    				TcComments, tcName, tcIndex, testType, serviceSteps);
    	}
	}
}