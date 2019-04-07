package module.services;


import core.apiCore.ServiceRunner;
import core.support.objects.ServiceObject;
import core.uiCore.drivers.AbstractDriverTestNG;
import module.services.interfaces.TestInterface;

public class ServiceManager {

	private static final String TEST_INTERFACE = "Test";

	public static void TestRunner(String TestSuite, String TestCaseID, String RunFlag, String Description,
			String InterfaceType, String UriPath, String ContentType, String Method, String Option,
			String RequestHeaders, String TemplateFile, String RequestBody, String OutputParams, String RespCodeExp,
			String ExpectedResponse, String TcComments,
			String tcName, String tcIndex) throws Exception {

		// add parameters to ServiceObject
		ServiceObject apiObject = new ServiceObject().setApiObject(TestSuite, TestCaseID, RunFlag, Description, InterfaceType,
				UriPath, ContentType, Method, Option, RequestHeaders, TemplateFile, RequestBody, OutputParams,
				RespCodeExp, ExpectedResponse, TcComments, tcName,
				tcIndex);

		// setup api driver
		new AbstractDriverTestNG().setupApiDriver(apiObject);

		switch (InterfaceType) {
		case TEST_INTERFACE:
			TestInterface.RestfullApiInterface(apiObject);
			break;
		default:
			ServiceRunner.TestRunner(apiObject);
			break;
		}
	}
}