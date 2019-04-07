package module.services.interfaces;


import static io.restassured.RestAssured.given;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.JsonHelper;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.ServiceObject;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestInterface {

	/*
	 * (String TestSuite, String TestCaseID, String RunFlag, String Description,
	 * String InterfaceType, String UriPath, String ContentType, String Method,
	 * String Option, String RequestHeaders, String TemplateFile, String
	 * RequestBody, String OutputParams, String RespCodeExp, String
	 * ExpectedResponse, String ExpectedResponse, String NotExpectedResponse,
	 * String TcComments, String tcName, String tcIndex)
	 */

	/**
	 * interface for restfull api calls
	 * 
	 * @param apiObject
	 * @return
	 */
	public static Response RestfullApiInterface(ServiceObject apiObject) {
		
		if(apiObject == null) Helper.assertFalse("apiobject is null");
		
		// replace parameters for request body
		apiObject.withRequestBody(DataHelper.replaceParameters(apiObject.getRequestBody()));

		// set base uri
		setURI(apiObject);

		// send request And receive a response
		Response response = evaluateRequest(apiObject);

		// validate the response
		validateResponse(response, apiObject);

		return response;
	}

	/**
	 * sets base uri for api call
	 */
	public static void setURI(ServiceObject apiObject) {

		// replace place holder values for uri
		apiObject.withUriPath(DataHelper.replaceParameters(apiObject.getUriPath()));
		apiObject.withUriPath(Helper.stringRemoveLines(apiObject.getUriPath()));
		// if uri is full path, Then set base uri as whats provided in csv file
		// else use baseURI from properties as base uri And extend it with csv file uri
		// path
		if (apiObject.getUriPath().startsWith("http")) {
			RestAssured.baseURI = apiObject.getUriPath();
			apiObject.withUriPath("");

		} else {
			RestAssured.baseURI = Helper.stringRemoveLines(Config.getValue("api.uriPath"));
			TestLog.logPass("request URI: " + RestAssured.baseURI + apiObject.getUriPath());
		}
	}

	public static void validateResponse(Response response, ServiceObject apiObject) {

		// fail test if no response is returned
		if (response == null)
			Helper.assertTrue("no response returned", false);

		// validate status code
		if (!apiObject.getOutputParams().isEmpty()) {
			TestLog.logPass("expected status code: " + apiObject.getOutputParams() + " response status code: "
					+ response.getStatusCode());
			response.then().statusCode(Integer.valueOf(apiObject.getOutputParams()));
		}

		// saves response values to config object
		JsonHelper.saveOutboundJsonParameters(response, apiObject.getOutputParams());

		validateExpectedValues(response, apiObject);
	}

	public static void validateExpectedValues(Response response, ServiceObject apiObject) {
		// get response body as string
		String body = response.getBody().asString();
		TestLog.logPass("response: " + body);

		// validate response body against expected json string
		if (!apiObject.getExpectedResponse().isEmpty()) {
			apiObject.withExpectedResponse(DataHelper.replaceParameters(apiObject.getExpectedResponse()));

			// separate the expected response by &&
			String[] criteria = apiObject.getExpectedResponse().split("&&");
			for (String criterion : criteria) {
				Helper.assertTrue("expected is not valid format: " + criterion, JsonHelper.isValidExpectation(criterion));
				JsonHelper.validateByJsonBody(criterion, response);
				JsonHelper.validateByKeywords(criterion, response);
			}
		}
	}
	
	/**
	 * sets the header, content type And body based on specifications
	 * 
	 * @param apiObject
	 * @return
	 */
	public static RequestSpecification evaluateRequestHeaders(ServiceObject apiObject) {
		// set request
		RequestSpecification request = null;

		// if no RequestHeaders specified
		if (apiObject.getRequestHeaders().isEmpty()) {
			return given();
		}

		// replace parameters for request body
		apiObject.withRequestHeaders( DataHelper.replaceParameters(apiObject.getRequestHeaders()));

		// if Authorization is set
		if (apiObject.getRequestHeaders().contains("Authorization:")) {
			String token = apiObject.getRequestHeaders().replace("Authorization:", "");
			request = given().header("Authorization", token);
		}

		// if additional request headers
		switch (apiObject.getRequestHeaders()) {
		case "INVALID_TOKEN":
			request = given().header("Authorization", "invalid");
			break;
		case "NO_TOKEN":
			request = given().header("Authorization", "");
			break;
		default:
			break;
		}

		return request;
	}
	
	public static RequestSpecification evaluateRequestBody(ServiceObject apiObject, RequestSpecification request) {
		if(apiObject.getRequestBody().isEmpty()) return request;
		
		// set content type
		request = request.contentType(apiObject.getContentType());
		
		// set form data
		if(apiObject.getContentType().contains("form")) {
			request = request.config(RestAssured.config().encoderConfig(io.restassured.config.EncoderConfig.encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)));
			
			String[] formData = apiObject.getRequestBody().split(",");
			for(String data : formData) {
				String[] keyValue = data.split(":");
				request = request.formParam(keyValue[0], keyValue[1]);
			}
			return request;
		}
		
		// if json data type
		return request.body(apiObject.getRequestBody());
	}
	
	

	/**
	 * sets the header, content type And body based on specifications
	 * 
	 * @param apiObject
	 * @return
	 */
	public static RequestSpecification evaluateOption(ServiceObject apiObject, RequestSpecification request) {

		// if no option specified
		if (apiObject.getOption().isEmpty()) {
			return request;
		}

		// replace parameters for request body
		apiObject.withOption(DataHelper.replaceParameters(apiObject.getOption()));

		// if additional options
		switch (apiObject.getOption()) {
		default:
			break;
		}

		return request;
	}

	public static Response evaluateRequest(ServiceObject apiObject) {
		Response response = null;
		
		// set request header
		RequestSpecification request = evaluateRequestHeaders(apiObject);
		
		// set request body
		request = evaluateRequestBody(apiObject, request);

		// set options
	    request = evaluateOption(apiObject, request);

		TestLog.logPass("request body: " + Helper.stringRemoveLines(apiObject.getRequestBody()));
		TestLog.logPass("request type: " + apiObject.getMethod());


		switch (apiObject.getMethod()) {
		case "POST":
			response = request.when().post(apiObject.getUriPath());
			break;
		case "PUT":
			response = request.when().put(apiObject.getUriPath());
			break;
		case "PATCH":
			response = request.when().patch(apiObject.getUriPath());
			break;
		case "DELETE":
			response = request.when().delete(apiObject.getUriPath());
			break;
		case "GET":
			response = request.when().get(apiObject.getUriPath());
			break;
		case "OPTIONS":
			response = request.when().options(apiObject.getUriPath());
			break;
		case "HEAD":
			response = request.when().head(apiObject.getUriPath());
			break;
		default:
			Helper.assertTrue("request type not found", false);
			break;
		}
		TestLog.logPass("response: " + response.getBody().asString());

		return response.then().extract().response();
	}
}
