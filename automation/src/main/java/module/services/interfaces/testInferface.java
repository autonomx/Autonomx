package module.services.interfaces;


import static io.restassured.RestAssured.given;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.JsonHelper;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.ApiObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestInferface {

	/*
	 * (String TestSuite, String TestCaseID, String RunFlag, String Description,
	 * String InterfaceType, String UriPath, String ContentType, String Method,
	 * String Option, String RequestHeaders, String TemplateFile, String
	 * RequestBody, String OutputParams, String RespCodeExp, String
	 * ExpectedResponse, String ExpectedResponse, String NotExpectedResponse,
	 * String TcComments, String tcName, String tcIndex)
	 */

	/**
	 * interface for restful api calls
	 * 
	 * @param apiObject
	 * @return
	 */
	public static Response RestfullApiInterface(ApiObject apiObject) {
		// replace parameters for request body
		apiObject.RequestBody = DataHelper.replaceParameters(apiObject.RequestBody);

		// set base uri
		setURI(apiObject);

		// send request and receive a response
		Response response = evaluateRequest(apiObject);

		// validate the response
		validateResponse(response, apiObject);

		return response;
	}

	/**
	 * sets base uri for api call
	 */
	public static void setURI(ApiObject apiObject) {

		// replace place holder values for uri
		apiObject.UriPath = DataHelper.replaceParameters(apiObject.UriPath);

		// if uri is full path, then set base uri as whats provided in csv file
		// else use baseURI from properties as base uri and extend it with csv file uri
		// path
		if (apiObject.UriPath.startsWith("http")) {
			RestAssured.baseURI = apiObject.UriPath;
			apiObject.UriPath = "";

		} else {
			RestAssured.baseURI = Config.getValue("UriPath");
			TestLog.logPass("baseURI: " + RestAssured.baseURI);
			TestLog.logPass("request URI: " + RestAssured.baseURI + apiObject.UriPath);
		}
	}

	public static void validateResponse(Response response, ApiObject apiObject) {

		// fail test if no response is returned
		if (response == null)
			Helper.assertTrue("no response returned", false);

		// validate status code
		if (!apiObject.RespCodeExp.isEmpty()) {
			TestLog.logPass("expected status code: " + apiObject.RespCodeExp + " response status code: "
					+ response.getStatusCode());
			response.then().statusCode(Integer.valueOf(apiObject.RespCodeExp));
		}

		// saves response values to config object
		JsonHelper.saveOutboundJsonParameters(response, apiObject.OutputParams);

		validateExpectedValues(response, apiObject);
	}

	public static void validateExpectedValues(Response response, ApiObject apiObject) {
		// get response body as string
		String body = response.getBody().asString();
		TestLog.logPass("response: " + body);

		// validate response body against expected json string
		if (!apiObject.ExpectedResponse.isEmpty()) {
			apiObject.ExpectedResponse = DataHelper.replaceParameters(apiObject.ExpectedResponse);

			// separate the expected response by &&
			String[] criteria = apiObject.ExpectedResponse.split("&&");
			for (String criterion : criteria) {
				JsonHelper.validateByJsonBody(criterion, response);
				JsonHelper.validateByKeywords(criterion, response);
			}
		}
	}

	/**
	 * sets the header, content type and body based on specifications
	 * 
	 * @param apiObject
	 * @return
	 */
	public static RequestSpecification evaluateRequestHeaders(ApiObject apiObject) {
		// set request
		RequestSpecification request = null;

		// if no RequestHeaders specified
		if (apiObject.RequestHeaders.isEmpty()) {
			return given().contentType(apiObject.ContentType).body(apiObject.RequestBody);
		}

		// replace parameters for request body
		apiObject.RequestHeaders = DataHelper.replaceParameters(apiObject.RequestHeaders);

		// if Authorization is set
		if (apiObject.RequestHeaders.contains("Authorization:")) {
			String token = apiObject.RequestHeaders.replace("Authorization:", "");
			request = given().header("Authorization", token);
		}

		// if additional request headers
		switch (apiObject.RequestHeaders) {
		default:
			break;
		}
		request = request.contentType(apiObject.ContentType).body(apiObject.RequestBody);

		return request;
	}

	/**
	 * sets the header, content type and body based on specifications
	 * 
	 * @param apiObject
	 * @return
	 */
	public static RequestSpecification evaluateOption(ApiObject apiObject, RequestSpecification request) {

		// if no option specified
		if (apiObject.Option.isEmpty()) {
			return request;
		}

		// replace parameters for request body
		apiObject.Option = DataHelper.replaceParameters(apiObject.Option);

		// if additional options
		switch (apiObject.Option) {
		case "INVALID_TOKEN":
			request = request.header("Authorization", "invalid");
			break;
		case "NO_TOKEN":
			request = request.header("Authorization", "");
			break;
		default:
			break;
		}
		request = request.contentType(apiObject.ContentType).body(apiObject.RequestBody);

		return request;
	}

	public static Response evaluateRequest(ApiObject apiObject) {
		Response response = null;
		// set request

		RequestSpecification request = evaluateRequestHeaders(apiObject);
		
		request = evaluateOption(apiObject, request);
		
		

		TestLog.logPass("request body: " + Helper.stringRemoveLines(apiObject.RequestBody));
		TestLog.logPass("request type: " + apiObject.Method);

		switch (apiObject.Method) {
		case "POST":
			response = request.when().post(apiObject.UriPath);
			break;
		case "PUT":
			response = request.when().put(apiObject.UriPath);
			break;
		case "PATCH":
			response = request.when().patch(apiObject.UriPath);
			break;
		case "DELETE":
			response = request.when().delete(apiObject.UriPath);
			break;
		case "GET":
			response = request.when().get(apiObject.UriPath);
			break;
		case "OPTIONS":
			response = request.when().options(apiObject.UriPath);
			break;
		case "HEAD":
			response = request.when().head(apiObject.UriPath);
			break;
		default:
			Helper.assertTrue("request type not found", false);
			break;
		}
		TestLog.logPass("response: " + response.getBody().asString());

		return response.then().contentType(apiObject.ContentType).extract().response();
	}
}
