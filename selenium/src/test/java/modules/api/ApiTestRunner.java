package modules.api;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.dataProvider;
import modules.TestBase;
import modules.main_api.apiManager;

public class ApiTestRunner extends TestBase {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
	
	}

	@Test(dataProvider = "parallelRun", dataProviderClass = dataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void parallelApiRunner(String TestSuite, String TestCaseID, String RunFlag, String Description,
			String InterfaceType, String UriPath, String ContentType, String Method, String Option,
			String RequestHeaders, String TemplateFile, String RequestBody, String OutputParams, String RespCodeExp,
			String ExpectedResponse, String PartialExpectedResponse, String NotExpectedResponse, String TcComments,
			String tcName, String tcIndex) throws Exception {

		apiManager.TestRunner(TestSuite, TestCaseID, RunFlag, Description, InterfaceType, UriPath, ContentType, Method,
				Option, RequestHeaders, TemplateFile, RequestBody, OutputParams, RespCodeExp, ExpectedResponse,
				PartialExpectedResponse, NotExpectedResponse, TcComments, tcName, tcIndex);
	}
}