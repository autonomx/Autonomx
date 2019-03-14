package module.service.tests;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import module.TestBase;
import module.services.ServiceManager;

public class ServiceTestRunner extends TestBase {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
	
	}
	

	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void serviceRunner(String TestSuite, String TestCaseID, String RunFlag, String Description,
			String InterfaceType, String UriPath, String ContentType, String Method, String Option,
			String RequestHeaders, String TemplateFile, String RequestBody, String OutputParams, String RespCodeExp,
			String ExpectedResponse, String TcComments,
			String tcName, String tcIndex) throws Exception {

		ServiceManager.TestRunner(TestSuite, TestCaseID, RunFlag, Description, InterfaceType, UriPath, ContentType, Method,
				Option, RequestHeaders, TemplateFile, RequestBody, OutputParams, RespCodeExp, ExpectedResponse,
				TcComments, tcName, tcIndex);
	}
}