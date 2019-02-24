package module.services;


import core.apiCore.apiRunner;
import core.support.objects.ApiObject;
import core.uiCore.drivers.AbstractDriverTestNG;
import module.services.interfaces.testInferface;

public class apiManager {

	private static final String TEST_INTERFACE = "Test";

	public static void TestRunner(String TestSuite, String TestCaseID, String RunFlag, String Description,
			String InterfaceType, String UriPath, String ContentType, String Method, String Option,
			String RequestHeaders, String TemplateFile, String RequestBody, String OutputParams, String RespCodeExp,
			String ExpectedResponse, String TcComments,
			String tcName, String tcIndex) throws Exception {

		// add parameters to ApiObject
		ApiObject apiObject = new ApiObject().setApiObject(TestSuite, TestCaseID, RunFlag, Description, InterfaceType,
				UriPath, ContentType, Method, Option, RequestHeaders, TemplateFile, RequestBody, OutputParams,
				RespCodeExp, ExpectedResponse, TcComments, tcName,
				tcIndex);

		// setup api driver
		new AbstractDriverTestNG().setupApiDriver(apiObject);

		switch (InterfaceType) {
		case TEST_INTERFACE:
			testInferface.RestfullApiInterface(apiObject);
			break;
		default:
			apiRunner.TestRunner(apiObject);
			break;
		}
	}
}