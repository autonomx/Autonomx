package test.module.framework.tests.functional.service;


import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.JsonHelper;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.ServiceObject;
import data.Data;
import io.restassured.response.Response;
import module.common.data.CommonUser;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class JsonHelperTest extends TestBase {
	
	private final String jsonString = "{\n" + 
			"	\"id\": 305,\n" + 
			"	\"username\": \"zzz_<@_TIME19>\",\n" + 
			"	\"email\": null,\n" + 
			"	\"provider\": \"local\",\n" + 
			"	\"confirmed\": null,\n" + 
			"	\"blocked\": null,\n" + 
			"	\"role\": {\n" + 
			"		\"id\": 2,\n" + 
			"		\"name\": \"Authenticated\",\n" + 
			"		\"description\": \"Default role given to authenticated user.\",\n" + 
			"		\"type\": \"Create WorkOrder\"\n" + 
			"	}\n" + 
			"}";
	
	
	private final String jsonBookStore = "{\n" + 
			"    \"store\": {\n" + 
			"        \"book\": [\n" + 
			"            {\n" + 
			"                \"category\": \"reference\",\n" + 
			"                \"author\": \"Nigel Rees\",\n" + 
			"                \"title\": \"\",\n" + 
			"                \"price\": 8.95\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"category\": \"fiction\",\n" + 
			"                \"author\": \"Evelyn Waugh\",\n" + 
			"                \"title\": \"Sword of Honour\",\n" + 
			"                \"price\": 12.99\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"category\": \"fiction\",\n" + 
			"                \"author\": \"Herman Melville\",\n" + 
			"                \"title\": \"Moby Dick\",\n" + 
			"                \"isbn\": \"0-553-21311-3\",\n" + 
			"                \"price\": 8.99\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"category\": \"fiction\",\n" + 
			"                \"author\": \"J. R. R. Tolkien\",\n" + 
			"                \"title\": \"The Lord of the Rings\",\n" + 
			"                \"isbn\": \"0-395-19395-8\",\n" + 
			"                \"price\": 22.99\n" + 
			"            }\n" + 
			"        ],\n" + 
			"        \"bicycle\": {\n" + 
			"            \"color\": \"red\",\n" + 
			"            \"price\": 19.95\n" + 
			"        }\n" + 
			"    },\n" + 
			"    \"expensive\": 10\n" + 
			"}";
	
	@BeforeClass
	public void beforeClass()  {
	}
	
	@Test()
	public void isJSONValid_valid() {
		
		TestLog.When("I verify a valid json string");
		
		boolean isJson = JsonHelper.isJSONValid(jsonString, true);
		Helper.assertTrue("is not valid json", isJson);
	}
	
	@Test()
	public void isJSONValid_invalid() {
		
		TestLog.When("I verify an invalid json string");
		String jsonStringInvalid = jsonString.replaceFirst("\\{", "[");
		boolean isJson = JsonHelper.isJSONValid(jsonStringInvalid, true);
		Helper.assertTrue("should be invalid json", !isJson);
	}
	
	@Test()
	public void isJSONValid_invalid_indicator() {
		
		TestLog.When("I verify an invalid json string");
		String keyword_indicator = DataHelper.VERIFY_JSON_PART_INDICATOR;
		boolean isJson = JsonHelper.isJSONValid(keyword_indicator + "\n " + jsonString, true);
		Helper.assertTrue("should be invalid json", !isJson);
		
		keyword_indicator = DataHelper.VERIFY_RESPONSE_BODY_INDICATOR;
		isJson = JsonHelper.isJSONValid(keyword_indicator + "\n " + jsonString, true);
		Helper.assertTrue("should be invalid json", !isJson);
		
		keyword_indicator = DataHelper.VERIFY_RESPONSE_NO_EMPTY;
		 isJson = JsonHelper.isJSONValid(keyword_indicator + "\n " + jsonString, true);
		Helper.assertTrue("should be invalid json", !isJson);
	}
	
	@Test()
	public void verifyJsonBodyTest() {
		TestLog.When("I verify a valid json comparison");

		String criteria = "{\n" + 
				"	\"id\": 305,\n" + 
				"	\"email\": null,\n" + 
				"	\"provider\": \"local\",\n" + 
				"	\"role\": {\n" + 
				"		\"id\": 2,\n" + 
				"		\"name\": \"Authenticated\",\n" + 
				"	}\n" + 
				"}";
		String errors = JsonHelper.validateByJsonBody(criteria, jsonString);
		Helper.assertTrue("errors occured: " +  errors, errors.isEmpty());
	}
	
	@Test()
	public void verifyJsonBodyTestInvalid() {
		TestLog.When("I verify a valid json against wrong value");

		String criteria = "{\n" + 
				"	\"id\": 301,\n" + 
				"	\"username\": \"zzz_autoUserv61w2\",\n" + 
				"	\"role\": {\n" + 
				"		\"id\": 3,\n" + 
				"		\"name\": \"Authenticated2\",\n" + 
				"		\"description\": \"Default role given to authenticated user.\",\n" + 
				"		\"type\": \"authenticated\"\n" + 
				"	}\n" + 
				"}";
		String error = JsonHelper.validateByJsonBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void verifyJsonBodyTestInvalid_StringReplace() {	
		TestLog.When("I verify a valid json against wrong value with parameter replacement");

		String criteria = "{\n" + 
				"	\"id\": 305,\n" + 
				"	\"username\": \"zzz_<@_TIME19>\",\n" + 
				"	\"email\": null,\n" + 
				"	\"role\": {\n" + 
				"		\"id\": 2\n" + 
				"	}\n" + 
				"}";
		String jsonString_updated = DataHelper.replaceParameters(jsonString);	
		String criteria_updated = DataHelper.replaceParameters(criteria);	

		String error = JsonHelper.validateByJsonBody(criteria_updated, jsonString_updated);
		Helper.assertTrue("errors not caught: " + error, error.isEmpty());
	}
	
	@Test()
	public void saveOutboundJsonParameters_verify() {
		CommonUser user = Data.common.commonuser().withAdminLogin();
		
		TestLog.When("I create a user through rest api");
	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
		
		TestLog.Then("I verify role name is stored in a variable");
		JsonHelper.saveOutboundJsonParameters(response, "role.name:<$name>; provider:1:<$local>");
		Helper.assertEquals("Authenticated", Config.getValue("name"));
		Helper.assertEquals("local", Config.getValue("local"));
	}
	
	
	@Test()
	public void configMapJsonKeyValues_verify() {
		
		CommonUser user = Data.common.commonuser().withAdminLogin();
		
		TestLog.When("I create a user through rest api");
	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
		
		TestLog.Then("I verify role name is stored in a variable");
		JsonHelper.configMapJsonKeyValues(response, "role.name:<$name>; provider:1:<$local>");
		Helper.assertEquals("Authenticated", Config.getValue("name"));
		Helper.assertEquals("local", Config.getValue("local"));
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void configMapJsonKeyValues_invalid_variable_format() {
		TestLog.When("I create a user through rest api");

		CommonUser user = Data.common.commonuser().withAdminLogin();
	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
		
		TestLog.Then("I verify invalid variable format throws assertion error");
		JsonHelper.configMapJsonKeyValues(response, "role.name:1:<$name>; provider:1:<@local>");
		Helper.assertEquals("Authenticated", Config.getValue("name"));
		Helper.assertEquals("local", Config.getValue("local"));
	}
	
	@Test()
	public void getJsonValue_valid() {
		CommonUser user = Data.common.commonuser().withAdminLogin();
		
		TestLog.When("I create a user through rest api");
	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
		
		TestLog.Then("I verify getting json value from path");
		String name = JsonHelper.getJsonValue(response, "role.name");
		Helper.assertEquals("Authenticated", name);
	}
	
	@Test()
	public void getJsonValue_jsonString_response() {
		TestLog.Then("I verify getting json value from path");
		String result = JsonHelper.getJsonValue(jsonBookStore, "store.book[?(@.price < 10)]");
		Helper.assertTrue("result is not json string: " + result, JsonHelper.isJSONValid(result.toString(), false));
		
		String expectedResult = "[\n" + 
				"   {\n" + 
				"      \"category\" : \"reference\",\n" + 
				"      \"author\" : \"Nigel Rees\",\n" + 
				"      \"title\" : \"\",\n" + 
				"      \"price\" : 8.95\n" + 
				"   },\n" + 
				"   {\n" + 
				"      \"category\" : \"fiction\",\n" + 
				"      \"author\" : \"Herman Melville\",\n" + 
				"      \"title\" : \"Moby Dick\",\n" + 
				"      \"isbn\" : \"0-553-21311-3\",\n" + 
				"      \"price\" : 8.99\n" + 
				"   }\n" + 
				"]";
		
		String error = JsonHelper.validateByJsonBody(expectedResult, result);
		Helper.assertTrue("errors not caught: " + error, error.isEmpty());
	}
	
	
	public void getJsonValue_with_json_string() {
		
		TestLog.Then("I verify getting json value from path");
		String name = JsonHelper.getJsonValue(jsonString, "role.name");
		Helper.assertEquals("Authenticated", name);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void getJsonValue_with_invalid_path() {
		
		TestLog.Then("I verify getting json value from path");
		JsonHelper.getJsonValue(jsonString, "role[0].name");
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void getJsonValue_with_jsonArray_invalidpath() {
		
		String jsonValue = "[{\"id\":1,\"username\":\"autoAdmin1\",\"email\":\"aut@email\","
				+ "\"provider\":\"local\",\"confirmed\":true,\"blocked\":null,\"role\":1}]";
		
		TestLog.Then("I verify getting json value from path");
		JsonHelper.getJsonValue(jsonValue, "id");
	}
	
	@Test()
	public void getJsonValue_with_JSONArray() {
		String jsonValue = "[{\"id\":1,\"username\":\"autoAdmin1\",\"email\":\"aut@email\","
				+ "\"provider\":\"local\",\"confirmed\":true,\"blocked\":null,\"role\":1}]";
		
		TestLog.Then("I verify getting json value from path");
		String id = JsonHelper.getJsonValue(jsonValue, ".id");
		Helper.assertEquals("1", id);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void getJsonValue_with_invalid_unavailable_path() {
		
		TestLog.Then("I verify getting json value from path");
		JsonHelper.getJsonValue(jsonString, "role.name2");
	}
	
	@Test()
	public void getJsonValue_with_json_price_query() {
		
		TestLog.Then("I verify getting json value from path");
		String name = JsonHelper.getJsonValue(jsonBookStore, "store.book[?(@.price < 10)].author");
		Helper.assertEquals("Nigel Rees,Herman Melville", name);
	}
	
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void getJsonValue_with_json_price_query_invalid_expected_with_quote() {
		
		TestLog.Then("I verify getting json value from path");
		String name = JsonHelper.getJsonValue(jsonBookStore, "store.book[?(@.price < 10)].author");
		Helper.assertEquals("\"Nigel Rees\",\"Herman Melville\"", name);
	}
	
	@Test()
	public void getJsonValue_with_json_regex() {
		
		TestLog.Then("I verify getting json value from path");
		String name = JsonHelper.getJsonValue(jsonBookStore, ".book[?(@.author =~ /.*REES/i)].author");
		Helper.assertEquals("Nigel Rees", name);
	}
	
	@Test()
	public void getJsonValue_with_json_search() {
		
		TestLog.Then("I verify getting json value from path");
		String authors = JsonHelper.getJsonValue(jsonBookStore, ".book[?(@.category == 'fiction')].author");
		Helper.assertEquals("Evelyn Waugh,Herman Melville,J. R. R. Tolkien", authors);
	}
	
	@Test()
	public void getJsonValue_with_json_array() {
		String jsonString = "{\n" + 
				"\"name\":\"John\",\n" + 
				"\"age\":30,\n" + 
				"\"cars\":[ \"Ford\", \"BMW\", \"Fiat\" ]\n" + 
				"}";
		
		TestLog.Then("I verify getting json value from path");
		String cars = JsonHelper.getJsonValue(jsonString, "cars");
		Helper.assertEquals("Ford,BMW,Fiat", cars);
	}
	
	
	@Test()
	public void getJsonValueFromXml_verify() {
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<root>\n" + 
				"   <age>30</age>\n" + 
				"   <cars>\n" + 
				"      <element>Ford</element>\n" + 
				"      <element>BMW</element>\n" + 
				"      <element>Fiat</element>\n" + 
				"   </cars>\n" + 
				"   <name>John</name>\n" + 
				"</root>";
						
		TestLog.When("I verify getting json path value from xml");
		String cars = JsonHelper.getJsonValueFromXml(xmlString, "root.cars.element");
		Helper.assertEquals("Ford,BMW,Fiat", cars);
	}
	
	@Test()
	public void replaceJsonPathValue_verify() {
		String jsonPath = "session.name";
		String newVal = "updatedSession";
		
		String originalJson = "{\n"
		        + "\"session\":\n"
		        + "    {\n"
		        + "        \"name\":\"JSESSIONID\",\n"
		        + "        \"value\":\"5864FD56A1F84D5B0233E641B5D63B52\"\n"
		        + "    },\n"
		        + "\"loginInfo\":\n"
		        + "    {\n"
		        + "        \"loginCount\":77,\n"
		        + "        \"previousLoginTime\":\"2014-12-02T11:11:58.561+0530\"\n"
		        + "    }\n"
		        + "}";
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, newVal);
		String updatedValue = JsonHelper.getJsonValue(updatedJson, jsonPath);
		
		Helper.assertEquals(newVal, updatedValue);
	}
	
	@Test()
	public void replaceJsonPathValue_jsonArray() {
		String jsonPath = "store.book[0].category";
		String newVal = "action";
		
		String originalJson = "{\n" + 
				"\"store\":{\n" + 
				"    \"book\":[\n" + 
				"        {\n" + 
				"            \"category\":\"reference\",\n" + 
				"            \"author\":\"Nigel Rees\",\n" + 
				"            \"title\":\"Sayings of the Century\",\n" + 
				"            \"price\":8.95\n" + 
				"        },\n" + 
				"        {\n" + 
				"            \"category\":\"fiction\",\n" + 
				"            \"author\":\"Evelyn Waugh\",\n" + 
				"            \"title\":\"Sword of Honour\",\n" + 
				"            \"price\":12.99,\n" + 
				"            \"isbn\":\"0-553-21311-3\"\n" + 
				"        }\n" + 
				"    ],\n" + 
				"    \"bicycle\":{\n" + 
				"        \"color\":\"red\",\n" + 
				"        \"price\":19.95\n" + 
				"    }\n" + 
				"   }\n" + 
				" }";
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, newVal);
		String updatedValue = JsonHelper.getJsonValue(updatedJson, jsonPath);
		
		Helper.assertEquals(newVal, updatedValue);
	}
	
	@Test()
	public void replaceJsonPathValue_valid_work_order() {
		String jsonPath = ".data..workOrders[0].asset";
		String newVal = "RMQAS_012345678";
		
		String originalJson = "{\n" + 
				"\"sourceSystem\":\"TEST\",\n" + 
				"\"data\":{\n" + 
				"\"workOrders\":[\n" + 
				"{\n" + 
				"\"workOrderId\":\"RMQWO_0010efsx8\",\n" + 
				"\"workOrderTemplate\":\"TMP01\",\n" + 
				"\"description\":\"Test DEIM WO Ingestion\",\n" + 
				"\"status\":\"Potential\",\n" + 
				"\"userStatus\":\"\",\n" + 
				"\"assetWork\":true,\n" + 
				"\"workType\":\"Maintenance - Scheduled\",\n" + 
				"\"priority\":1,\n" + 
				"\"raisedDateTime\":1557460241123,\n" + 
				"\"raisedBy\":\"Service Scheduler\",\n" + 
				"\"assigedTo\":\"Service Scheduler\",\n" + 
				"\"requiredByDateTime\":1557460241245,\n" + 
				"\"plannedStartDateTime\":1557460241378,\n" + 
				"\"plannedFinishDateTime\":1557460241376,\n" + 
				"\"actualStartDateTime\":1557460241456,\n" + 
				"\"actualFinishDateTime\":1557460241376,\n" + 
				"\"closedDateTime\":1557460241987,\n" + 
				"\"plannedDuration\":2880000,\n" + 
				"\"actualDuration\":1440000,\n" + 
				"\"completedBy\":\"Maintenance Team\",\n" + 
				"\"completionComments\":\"Ring successfully replaced.\",\n" + 
				"\"asset\":\"ERSASSET01\"\n" + 
				"}\n" + 
				"]\n" + 
				"}\n" + 
				"}";
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, newVal);
		String updatedValue = JsonHelper.getJsonValue(updatedJson, jsonPath);
		
		Helper.assertEquals(newVal, updatedValue);
	}
	
	@Test()
	public void replaceJsonPathValue_jsonArray2() {
		String jsonPath = "cars[1].name";
		String newVal = "toyota";
		
		String originalJson = "{\n" + 
				"  \"name\":\"John\",\n" + 
				"  \"age\":30,\n" + 
				"  \"cars\": [\n" + 
				"    { \"name\":\"Ford\", \"models\":[ \"Fiesta\", \"Focus\", \"Mustang\" ] },\n" + 
				"    { \"name\":\"BMW\", \"models\":[ \"320\", \"X3\", \"X5\" ] },\n" + 
				"    { \"name\":\"Fiat\", \"models\":[ \"500\", \"Panda\" ] }\n" + 
				"  ]\n" + 
				" }";
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, newVal);
		String updatedValue = JsonHelper.getJsonValue(updatedJson, jsonPath);
		
		Helper.assertEquals(newVal, updatedValue);
	}
	
 	@Test()
	public void replaceJsonPathValue_jsonArray_replaceAll() {
		String jsonPath = "cars..name";
		String newVal = "toyota";
		String originalJson = "{\n" + 
				"  \"name\":\"John\",\n" + 
				"  \"age\":30,\n" + 
				"  \"cars\": [\n" + 
				"    { \"name\":\"Ford\", \"models\":[ \"Fiesta\", \"Focus\", \"Mustang\" ] },\n" + 
				"    { \"name\":\"BMW\", \"models\":[ \"320\", \"X3\", \"X5\" ] },\n" + 
				"    { \"name\":\"Fiat\", \"models\":[ \"500\", \"Panda\" ] }\n" + 
				"  ]\n" + 
				" }";
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, newVal);
		String updatedValue = JsonHelper.getJsonValue(updatedJson, jsonPath);
		Helper.assertEquals("toyota,toyota,toyota", updatedValue);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void replaceJsonPathValue_invalid_path() {
		String jsonPath = "cars.name";
		String newVal = "toyota";
		
		String originalJson = "{\n" + 
				"  \"name\":\"John\",\n" + 
				"  \"age\":30,\n" + 
				"  \"cars\": [\n" + 
				"    { \"name\":\"Ford\", \"models\":[ \"Fiesta\", \"Focus\", \"Mustang\" ] },\n" + 
				"    { \"name\":\"BMW\", \"models\":[ \"320\", \"X3\", \"X5\" ] },\n" + 
				"    { \"name\":\"Fiat\", \"models\":[ \"500\", \"Panda\" ] }\n" + 
				"  ]\n" + 
				" }";
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, newVal);
		String updatedValue = JsonHelper.getJsonValue(updatedJson, jsonPath);
		
		Helper.assertEquals(newVal, updatedValue);
	}
	
	@Test()
	public void getRequestBodyFromJsonTemplate_valid() {
		Config.putValue("quizItem", "quiz2");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Quiz.json")
				.withRequestBody("quiz.sport.q1.options:2:value_<@quizItem>");
		
		String updatedJson = JsonHelper.getRequestBodyFromJsonTemplate(serviceObject);
		updatedJson = DataHelper.replaceParameters(updatedJson);

		String updatedValue = JsonHelper.getJsonValue(updatedJson, "quiz.sport.q1.options");
		
		Helper.assertEquals("value_quiz2", updatedValue);
	}
	
	@Test()
	public void getRequestBodyFromJsonTemplate_valid_no_body() {
		Config.putValue("quizItem", "quiz2");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Quiz.json")
				.withRequestBody("");
		
		String updatedJson = JsonHelper.getRequestBodyFromJsonTemplate(serviceObject);
		String updatedValue = JsonHelper.getJsonValue(updatedJson, "quiz.sport.q1.options");
		
		Helper.assertEquals("New York Bulls,Los Angeles Kings,Golden State Warriros,Huston Rocket", updatedValue);
	}
	
	@Test()
	public void getJsonValue_with_json_quotes() {
		
		String jsonString = "{\n" + 
				"    \"shifts\": [{\n" + 
				"        \"shift\": {\n" + 
				"            \"id\": 3303,\n" + 
				"            \"worker\": \"aditimusunur@aditimusunur.ca\",\n" + 
				"            \"startTime\": \"2019-11-03T08:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-03T17:00:00.000Z\",\n" + 
				"            \"startLocation\": {\n" + 
				"                \"x\": 114.761706,\n" + 
				"                \"y\": 44.274767\n" + 
				"            },\n" + 
				"            \"endLocation\": {\n" + 
				"                \"x\": 112.188459,\n" + 
				"                \"y\": 47.648567\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"breaks\": [{\n" + 
				"            \"id\": 3303,\n" + 
				"            \"shiftId\": 3303,\n" + 
				"            \"startTime\": \"2019-11-03T12:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-03T13:00:00.000Z\"\n" + 
				"        }]\n" + 
				"    }, {\n" + 
				"        \"shift\": {\n" + 
				"            \"id\": 3301,\n" + 
				"            \"worker\": \"aditimusunur@aditimusunur.ca\",\n" + 
				"            \"startTime\": \"2019-11-01T08:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-01T17:00:00.000Z\",\n" + 
				"            \"startLocation\": {\n" + 
				"                \"x\": 114.761706,\n" + 
				"                \"y\": 44.274767\n" + 
				"            },\n" + 
				"            \"endLocation\": {\n" + 
				"                \"x\": 112.188459,\n" + 
				"                \"y\": 47.648567\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"breaks\": [{\n" + 
				"            \"id\": 3301,\n" + 
				"            \"shiftId\": 3301,\n" + 
				"            \"startTime\": \"2019-11-01T12:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-01T13:00:00.000Z\"\n" + 
				"        }]\n" + 
				"    }, {\n" + 
				"        \"shift\": {\n" + 
				"            \"id\": 3302,\n" + 
				"            \"worker\": \"aditimusunur@aditimusunur.ca\",\n" + 
				"            \"startTime\": \"2019-11-02T08:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-02T17:00:00.000Z\",\n" + 
				"            \"startLocation\": {\n" + 
				"                \"x\": 114.761706,\n" + 
				"                \"y\": 44.274767\n" + 
				"            },\n" + 
				"            \"endLocation\": {\n" + 
				"                \"x\": 112.188459,\n" + 
				"                \"y\": 47.648567\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"breaks\": [{\n" + 
				"            \"id\": 3302,\n" + 
				"            \"shiftId\": 3302,\n" + 
				"            \"startTime\": \"2019-11-02T12:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-02T13:00:00.000Z\"\n" + 
				"        }]\n" + 
				"    }, {\n" + 
				"        \"shift\": {\n" + 
				"            \"id\": 3306,\n" + 
				"            \"worker\": \"devasrusubramanyan@devasrusubramanyan.com\",\n" + 
				"            \"startTime\": \"2019-11-02T08:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-02T17:00:00.000Z\",\n" + 
				"            \"startLocation\": {\n" + 
				"                \"x\": -123.042148,\n" + 
				"                \"y\": 49.1790825\n" + 
				"            },\n" + 
				"            \"endLocation\": {\n" + 
				"                \"x\": -123.033908,\n" + 
				"                \"y\": 49.1901353\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"breaks\": [{\n" + 
				"            \"id\": 3306,\n" + 
				"            \"shiftId\": 3306,\n" + 
				"            \"startTime\": \"2019-11-02T12:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-02T13:00:00.000Z\"\n" + 
				"        }]\n" + 
				"    }, {\n" + 
				"        \"shift\": {\n" + 
				"            \"id\": 3304,\n" + 
				"            \"worker\": \"aditimusunur@aditimusunur.ca\",\n" + 
				"            \"startTime\": \"2019-11-04T08:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-04T17:00:00.000Z\",\n" + 
				"            \"startLocation\": {\n" + 
				"                \"x\": 114.761706,\n" + 
				"                \"y\": 44.274767\n" + 
				"            },\n" + 
				"            \"endLocation\": {\n" + 
				"                \"x\": 112.188459,\n" + 
				"                \"y\": 47.648567\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"breaks\": [{\n" + 
				"            \"id\": 3304,\n" + 
				"            \"shiftId\": 3304,\n" + 
				"            \"startTime\": \"2019-11-04T12:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-04T13:00:00.000Z\"\n" + 
				"        }]\n" + 
				"    }, {\n" + 
				"        \"shift\": {\n" + 
				"            \"id\": 3308,\n" + 
				"            \"worker\": \"devasrusubramanyan@devasrusubramanyan.com\",\n" + 
				"            \"startTime\": \"2019-11-04T08:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-04T17:00:00.000Z\",\n" + 
				"            \"startLocation\": {\n" + 
				"                \"x\": -123.042148,\n" + 
				"                \"y\": 49.1790825\n" + 
				"            },\n" + 
				"            \"endLocation\": {\n" + 
				"                \"x\": -123.033908,\n" + 
				"                \"y\": 49.1901353\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"breaks\": [{\n" + 
				"            \"id\": 3308,\n" + 
				"            \"shiftId\": 3308,\n" + 
				"            \"startTime\": \"2019-11-04T12:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-04T13:00:00.000Z\"\n" + 
				"        }]\n" + 
				"    }, {\n" + 
				"        \"shift\": {\n" + 
				"            \"id\": 3307,\n" + 
				"            \"worker\": \"devasrusubramanyan@devasrusubramanyan.com\",\n" + 
				"            \"startTime\": \"2019-11-03T08:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-03T17:00:00.000Z\",\n" + 
				"            \"startLocation\": {\n" + 
				"                \"x\": -123.042148,\n" + 
				"                \"y\": 49.1790825\n" + 
				"            },\n" + 
				"            \"endLocation\": {\n" + 
				"                \"x\": -123.033908,\n" + 
				"                \"y\": 49.1901353\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"breaks\": [{\n" + 
				"            \"id\": 3307,\n" + 
				"            \"shiftId\": 3307,\n" + 
				"            \"startTime\": \"2019-11-03T12:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-03T13:00:00.000Z\"\n" + 
				"        }]\n" + 
				"    }, {\n" + 
				"        \"shift\": {\n" + 
				"            \"id\": 3305,\n" + 
				"            \"worker\": \"devasrusubramanyan@devasrusubramanyan.com\",\n" + 
				"            \"startTime\": \"2019-11-01T08:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-01T17:00:00.000Z\",\n" + 
				"            \"startLocation\": {\n" + 
				"                \"x\": -123.042148,\n" + 
				"                \"y\": 49.1790825\n" + 
				"            },\n" + 
				"            \"endLocation\": {\n" + 
				"                \"x\": -123.033908,\n" + 
				"                \"y\": 49.1901353\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"breaks\": [{\n" + 
				"            \"id\": 3305,\n" + 
				"            \"shiftId\": 3305,\n" + 
				"            \"startTime\": \"2019-11-01T12:00:00.000Z\",\n" + 
				"            \"endTime\": \"2019-11-01T13:00:00.000Z\"\n" + 
				"        }]\n" + 
				"    }]\n" + 
				"}";
		
		TestLog.Then("I verify getting json value from path");
		String name = JsonHelper.getJsonValue(jsonString, "shifts[?(@.shift.worker == \"aditimusunur@aditimusunur.ca\")]..id");
		Helper.assertEquals("3303,3303,3301,3301,3302,3302,3304,3304", name);
		
		String path = Helper.removeSurroundingQuotes("\"shifts[?(@.shift.worker == \"aditimusunur@aditimusunur.ca\")]..id\"");
		name = JsonHelper.getJsonValue(jsonString, path);
		Helper.assertEquals("3303,3303,3301,3301,3302,3302,3304,3304", name);
		
		String workers = JsonHelper.getJsonValue(jsonString, "shifts[?(@.shift.worker == \"aditimusunur@aditimusunur.ca\")]..worker");

		String errors = DataHelper.validateCommand("nodeSizeExact", workers, "4");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
			
		errors = DataHelper.validateCommand("nodeSizeExact", workers, "5");
		Helper.assertTrue("errors not caught: " + errors, !errors.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_contain() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ contains(confirmed)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_invalid() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ contains(invalid)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_hasItems() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ hasItems(confirmed)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ hasItems(invalid)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_notHaveItems() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ notHaveItems(invalid)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ notHaveItems(confirmed)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_equalTo() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ equalTo(response value)";
		String error = JsonHelper.validateResponseBody(criteria, "response value");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ equalTo({\"id\":\"1234\"})";
		error = JsonHelper.validateResponseBody(criteria, "{\"id\":\"1234\"}");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ equalTo(response)";
		error = JsonHelper.validateResponseBody(criteria, "response value");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_notEqualTo() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ notEqualTo(invalid)";
		String error = JsonHelper.validateResponseBody(criteria, "response value");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ notEqualTo(response value)";
		error = JsonHelper.validateResponseBody(criteria, "response value");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_notContain() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ notContain(invalid)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ notContain(confirmed)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
		
		String json2 = "{\n" + 
				"    \"workOrderId\": \"WO_<@_RAND6>\",\n" + 
				"    \"workOrderUUID\": \"WO_<@_RAND6>\",\n" + 
				"    \"description\": \"Create WorkOrder\"}";
		
		criteria = "_VERIFY.RESPONSE.BODY_ notContain(Create WorkOrder)";
		error = JsonHelper.validateResponseBody(criteria, json2);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}

	@Test()
	public void validateResponseBody_contains() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ contains(confirmed)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ contains(\"confirmed\": null)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ contains(invalid)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_integerGreaterThan() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ integerGreaterThan(4)";
		String error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ integerGreaterThan(6)";
		error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_integerLessThan() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ integerLessThan(6)";
		String error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ integerLessThan(4)";
		error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_integerEqual() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ integerEqual(5.32)";
		String error = JsonHelper.validateResponseBody(criteria, "5.32");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ integerEqual(4.33)";
		error = JsonHelper.validateResponseBody(criteria, "5.32");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_integerNotEqual() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ integerNotEqual(6)";
		String error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ integerNotEqual(5)";
		error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_isNotEmpty() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ isNotEmpty";
		String error = JsonHelper.validateResponseBody(criteria, "json response");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ isNotEmpty";
		error = JsonHelper.validateResponseBody(criteria, "");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_isEmpty() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.RESPONSE.BODY_ isEmpty";
		String error = JsonHelper.validateResponseBody(criteria, "");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.RESPONSE.BODY_ isEmpty";
		error = JsonHelper.validateResponseBody(criteria, "json response");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_hasItems() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:hasItems(Herman Melville)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:hasItems(Nigel Rees,Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:2:hasItems(Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:2:hasItems(Nigel Rees)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:hasItems(invalid)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_notHaveItems() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:notHaveItems(invalid)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:notHaveItems(Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_notEqualTo() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:notEqualTo(invalid)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:notEqualTo(Nigel Rees,Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:1:notEqualTo(Nigel Rees)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:1:notEqualTo(Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_equalTo() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:equalTo(invalid)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:equalTo(Nigel Rees,Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:equalTo(Herman Melville,Nigel Rees)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:1:equalTo(Nigel Rees)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:1:equalTo(Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_contains() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:contains(Herman Melville)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:contains(Nigel Rees,Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:2:contains(Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:2:contains(Nigel Rees)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:contains(invalid)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_notContain() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:notContain(invalid)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:notContain(Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_containsInAnyOrder() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:containsInAnyOrder(Nigel Rees,Herman Melville)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:containsInAnyOrder(Herman Melville,Nigel Rees)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:1:containsInAnyOrder(Nigel Rees)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:1:containsInAnyOrder(Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:1:containsInAnyOrder(Nigel Rees,Herman Melville, invalid)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_integerGreaterThan() {
		TestLog.When("I verify a valid json against correct value");
         // correct value: 8.95
		String criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerGreaterThan(8)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerGreaterThan(8.96)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerGreaterThan(8.95)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_integerLessThan() {
		TestLog.When("I verify a valid json against correct value");
         // correct value: 8.95
		String criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerLessThan(8)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerLessThan(8.96)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerLessThan(8.95)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_integerEqual() {
		TestLog.When("I verify a valid json against correct value");
         // correct value: 8.95
		String criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerEqual(8)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerEqual(8.96)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerEqual(8.95)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerEqual(8.950)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_integerNotEqual() {
		TestLog.When("I verify a valid json against correct value");
         // correct value: 8.95
		String criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerNotEqual(8)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerNotEqual(8.96)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerNotEqual(8.95)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].price:integerNotEqual(8.950)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_nodeSizeGreaterThan() {
		TestLog.When("I verify a valid json against correct value");
         // correct value: 5
		String criteria = "_VERIFY.JSON.PART_ store..price:nodeSizeGreaterThan(4)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store..price:nodeSizeGreaterThan(5)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store..price:nodeSizeGreaterThan(6)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_nodeSizeExact() {
		TestLog.When("I verify a valid json against correct value");
         // correct value: 5
		String criteria = "_VERIFY.JSON.PART_ store..price:nodeSizeExact(4)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store..price:nodeSizeExact(5)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store..price:nodeSizeExact(6)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_nodeSizeLessThan() {
		TestLog.When("I verify a valid json against correct value");
         // correct value: 5
		String criteria = "_VERIFY.JSON.PART_ store..price:nodeSizeLessThan(4)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store..price:nodeSizeLessThan(5)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store..price:nodeSizeLessThan(6)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_sequence() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:sequence(invalid)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:sequence(Nigel Rees,Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:sequence(Herman Melville,Nigel Rees)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_jsonbody() {
		TestLog.When("I verify a valid json against correct value");
		
		String partialJson ="[\n" + 
				"   {\n" + 
				"      \"category\" : \"reference\",\n" + 
				"      \"author\" : \"Nigel Rees\",\n" + 
				"      \"title\" : \"\",\n" + 
				"      \"price\" : 8.95\n" + 
				"   }\n" + 
				"]";

		String criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)]:jsonbody("+partialJson+")";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_jsonbody_invalid() {
		TestLog.When("I verify a valid json against correct value");
		
		String partialJson ="[\n" + 
				"   {\n" + 
				"      \"category\" : \"reference2\",\n" + 
				"      \"author\" : \"Nigel Rees\",\n" + 
				"      \"title\" : \"Sayings of the Century\",\n" + 
				"      \"price\" : 8.96\n" + 
				"   }\n" + 
				"]";

		String criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)]:jsonbody("+partialJson+")";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_isNotEmpty() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:isNotEmpty";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:1:isNotEmpty";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].title:isNotEmpty";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_isEmpty() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].title:isEmpty";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.author =~ /.*REES/i)].author:isEmpty";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_jsonbody_same_field_as_commands() {
		TestLog.When("I verify a valid json against correct value");
		String partialJson ="[\r\n" + 
				"   {\r\n" + 
				"      \"id\" : \"R5578\",\r\n" + 
				"      \"type\" : \"Feature\",\r\n" + 
				"      \"properties\" : {\r\n" + 
				"         \"data\" : {\r\n" + 
				"            \"businessUnit\" : \"ABB\",\r\n" + 
				"            \"attachments\" : [\r\n" + 
				"               \r\n" + 
				"            ],\r\n" + 
				"            \"adms\" : {\r\n" + 
				"               \"normal\" : \"C\",\r\n" + 
				"               \"distanceFromFeeder\" : \"163\",\r\n" + 
				"               \"line\" : \"110015788\",\r\n" + 
				"               \"ratedCurrent\" : \"0\",\r\n" + 
				"               \"sequence\" : \"2174\",\r\n" + 
				"               \"node\" : \"60023311\",\r\n" + 
				"               \"recordedAt\" : \"2018-09-19T18:32:02Z\",\r\n" + 
				"               \"deviceSize\" : \"100\",\r\n" + 
				"               \"name\" : \"J433\",\r\n" + 
				"               \"action\" : \"Create\",\r\n" + 
				"               \"jsonbody\" : \"C\",\r\n" + 
				"               \"state\" : \"1\",\r\n" + 
				"               \"id\" : \"R5578\",\r\n" + 
				"               \"category\" : \"Fuse\",\r\n" + 
				"               \"class\" : \"Device\",\r\n" + 
				"               \"status\" : \"Closed\",\r\n" + 
				"               \"ganged\" : \"false\"\r\n" + 
				"            },\r\n" + 
				"            \"assetId\" : \"R5578\",\r\n" + 
				"            \"createdTimestamp\" : 1562953573995,\r\n" + 
				"            \"assetName\" : \"J433\",\r\n" + 
				"            \"assetGroupTypeId\" : \"ADMS\",\r\n" + 
				"            \"assetClass\" : \"Fuse\",\r\n" + 
				"            \"assetClassDescription\" : \"Fuse\",\r\n" + 
				"            \"lastModifiedTimestamp\" : 1562953573995\r\n" + 
				"         }\r\n" + 
				"      },\r\n" + 
				"      \"geometry\" : {\r\n" + 
				"         \"type\" : \"Point\",\r\n" + 
				"         \"coordinates\" : [\r\n" + 
				"            -98.49466699999999,\r\n" + 
				"            29.529902999999997\r\n" + 
				"         ]\r\n" + 
				"      }\r\n" + 
				"   }\r\n" + 
				"]";
		String jsonOriginal = "{\r\n" + 
				"  \"latest\": 1570765905390,\r\n" + 
				"  \"total\": 1585,\r\n" + 
				"  \"features\": {\r\n" + 
				"    \"type\": \"FeatureCollection\",\r\n" + 
				"    \"features\": [\r\n" + 
				"      {\r\n" + 
				"        \"id\": \"R5578\",\r\n" + 
				"        \"type\": \"Feature\",\r\n" + 
				"        \"properties\": {\r\n" + 
				"          \"data\": {\r\n" + 
				"            \"businessUnit\": \"ABB\",\r\n" + 
				"            \"attachments\": [],\r\n" + 
				"            \"adms\": {\r\n" + 
				"              \"normal\": \"C\",\r\n" + 
				"              \"distanceFromFeeder\": \"163\",\r\n" + 
				"              \"line\": \"110015788\",\r\n" + 
				"              \"ratedCurrent\": \"0\",\r\n" + 
				"              \"sequence\": \"2174\",\r\n" + 
				"              \"node\": \"60023311\",\r\n" + 
				"              \"recordedAt\": \"2018-09-19T18:32:02Z\",\r\n" + 
				"              \"deviceSize\": \"100\",\r\n" + 
				"              \"name\": \"J433\",\r\n" + 
				"              \"action\": \"Create\",\r\n" + 
				"              \"jsonbody\": \"C\",\r\n" + 
				"              \"state\": \"1\",\r\n" + 
				"              \"id\": \"R5578\",\r\n" + 
				"              \"category\": \"Fuse\",\r\n" + 
				"              \"class\": \"Device\",\r\n" + 
				"              \"status\": \"Closed\",\r\n" + 
				"              \"ganged\": \"false\"\r\n" + 
				"            },\r\n" + 
				"            \"assetId\": \"R5578\",\r\n" + 
				"            \"createdTimestamp\": 1562953573995,\r\n" + 
				"            \"assetName\": \"J433\",\r\n" + 
				"            \"assetGroupTypeId\": \"ADMS\",\r\n" + 
				"            \"assetClass\": \"Fuse\",\r\n" + 
				"            \"assetClassDescription\": \"Fuse\",\r\n" + 
				"            \"lastModifiedTimestamp\": 1562953573995\r\n" + 
				"          }\r\n" + 
				"        },\r\n" + 
				"        \"geometry\": {\r\n" + 
				"          \"type\": \"Point\",\r\n" + 
				"          \"coordinates\": [\r\n" + 
				"            -98.49466699999999,\r\n" + 
				"            29.529902999999997\r\n" + 
				"          ]\r\n" + 
				"        }\r\n" + 
				"      }]}}";
		String criteria = "_VERIFY_JSON_PART_ .features.features[?(@.id=~ /.*R5578/i)]:jsonbody("+partialJson+")";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonOriginal);
		Helper.assertTrue("errors caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_jsonbody_with_dataField() {
		TestLog.When("I verify a valid json against correct value");
		String partialJson ="[\n" + 
				"   {\n" + 
				"      \"attachments\" : [\n" + 
				"         {\n" + 
				"            \"name\" : \"Installation Guide\",\n" + 
				"            \"url\" : \"https://docs.jboss.org/author/display/WFLY8/Admin+Guide\"\n" + 
				"         }\n" + 
				"      ],\n" + 
				"      \"assetId\" : \"Asset_2019-11-13T21:47:07\",\n" + 
				"      \"createdTimestamp\" : \"2019-11-13T14:16:32.406Z\",\n" + 
				"      \"name\" : \"BIGTRACK42\",\n" + 
				"      \"assetModel\" : {\n" + 
				"         \"assetModelUUID\" : \"55ca1b2a-de9e-461f-aaff-a6442430e720\",\n" + 
				"         \"assetModelId\" : \"TESTEGISTEVE\",\n" + 
				"         \"name\" : \"TESTEGISTEVE\",\n" + 
				"         \"description\" : \"TESTEGISTEVE_Model\",\n" + 
				"         \"status\" : \"Active\"\n" + 
				"      },\n" + 
				"      \"description\" : \"Test Equipment\",\n" + 
				"      \"assetUUID\" : \"9ba35603-b29f-4dbe-9e40-8cb98e6a422d\",\n" + 
				"      \"lastModifiedTimestamp\" : \"2019-11-13T14:16:32.406Z\",\n" + 
				"      \"status\" : \"Active\"\n" + 
				"   }\n" + 
				"]";
		String jsonOriginal = "{\n" + 
				"\n" + 
				"  \"latest\": 1573680172113,\n" + 
				"\n" + 
				"  \"total\": 2132,\n" + 
				"\n" + 
				"  \"features\": {\n" + 
				"\n" + 
				"    \"type\": \"FeatureCollection\",\n" + 
				"\n" + 
				"    \"features\": [\n" + 
				"\n" + 
				"      {\n" + 
				"\n" + 
				"        \"id\": \"9ba35603-b29f-4dbe-9e40-8cb98e6a422d\",\n" + 
				"\n" + 
				"        \"type\": \"Feature\",\n" + 
				"\n" + 
				"        \"properties\": {\n" + 
				"\n" + 
				"          \"data\": {\n" + 
				"\n" + 
				"            \"attachments\": [\n" + 
				"\n" + 
				"              {\n" + 
				"\n" + 
				"                \"name\": \"Installation Guide\",\n" + 
				"\n" + 
				"                \"url\": \"https://docs.jboss.org/author/display/WFLY8/Admin+Guide\"\n" + 
				"\n" + 
				"              }\n" + 
				"\n" + 
				"            ],\n" + 
				"\n" + 
				"            \"assetId\": \"Asset_2019-11-13T21:47:07\",\n" + 
				"\n" + 
				"            \"createdTimestamp\": \"2019-11-13T14:16:32.406Z\",\n" + 
				"\n" + 
				"            \"name\": \"BIGTRACK42\",\n" + 
				"\n" + 
				"            \"assetModel\": {\n" + 
				"\n" + 
				"              \"assetModelUUID\": \"55ca1b2a-de9e-461f-aaff-a6442430e720\",\n" + 
				"\n" + 
				"              \"assetModelId\": \"TESTEGISTEVE\",\n" + 
				"\n" + 
				"              \"name\": \"TESTEGISTEVE\",\n" + 
				"\n" + 
				"              \"description\": \"TESTEGISTEVE_Model\",\n" + 
				"\n" + 
				"              \"status\": \"Active\"\n" + 
				"\n" + 
				"            },\n" + 
				"\n" + 
				"            \"description\": \"Test Equipment\",\n" + 
				"\n" + 
				"            \"assetUUID\": \"9ba35603-b29f-4dbe-9e40-8cb98e6a422d\",\n" + 
				"\n" + 
				"            \"lastModifiedTimestamp\": \"2019-11-13T14:16:32.406Z\",\n" + 
				"\n" + 
				"            \"status\": \"Active\"\n" + 
				"\n" + 
				"          }\n" + 
				"\n" + 
				"        },\n" + 
				"\n" + 
				"        \"geometry\": {\n" + 
				"\n" + 
				"          \"type\": \"Point\",\n" + 
				"\n" + 
				"          \"coordinates\": [\n" + 
				"\n" + 
				"            -98.505845,\n" + 
				"\n" + 
				"            29.553513\n" + 
				"\n" + 
				"          ]\n" + 
				"\n" + 
				"        }\n" + 
				"\n" + 
				"      },\n" + 
				"\n" + 
				"      {\n" + 
				"\n" + 
				"        \"id\": \"0088e84a-692d-41db-b243-1221b8d6d769\",\n" + 
				"\n" + 
				"        \"type\": \"Feature\",\n" + 
				"\n" + 
				"        \"properties\": {\n" + 
				"\n" + 
				"          \"data\": {\n" + 
				"\n" + 
				"            \"attachments\": [\n" + 
				"\n" + 
				"              {\n" + 
				"\n" + 
				"                \"name\": \"Installation Guide\",\n" + 
				"\n" + 
				"                \"url\": \"https://docs.jboss.org/author/display/WFLY8/Admin+Guide\"\n" + 
				"\n" + 
				"              }\n" + 
				"\n" + 
				"            ],\n" + 
				"\n" + 
				"            \"assetId\": \"A_1573680108593\",\n" + 
				"\n" + 
				"            \"createdTimestamp\": \"2019-11-13T21:22:52.113Z\",\n" + 
				"\n" + 
				"            \"name\": \"BIGTRACK42\",\n" + 
				"\n" + 
				"            \"assetModel\": {\n" + 
				"\n" + 
				"              \"assetModelUUID\": \"55ca1b2a-de9e-461f-aaff-a6442430e720\",\n" + 
				"\n" + 
				"              \"assetModelId\": \"TESTEGISTEVE\",\n" + 
				"\n" + 
				"              \"name\": \"TESTEGISTEVE\",\n" + 
				"\n" + 
				"              \"description\": \"TESTEGISTEVE_Model\",\n" + 
				"\n" + 
				"              \"status\": \"Active\"\n" + 
				"\n" + 
				"            },\n" + 
				"\n" + 
				"            \"description\": \"Test Equipment\",\n" + 
				"\n" + 
				"            \"assetUUID\": \"0088e84a-692d-41db-b243-1221b8d6d769\",\n" + 
				"\n" + 
				"            \"lastModifiedTimestamp\": \"2019-11-13T21:22:52.113Z\",\n" + 
				"\n" + 
				"            \"status\": \"Active\"\n" + 
				"\n" + 
				"          }\n" + 
				"\n" + 
				"        },\n" + 
				"\n" + 
				"        \"geometry\": {\n" + 
				"\n" + 
				"          \"type\": \"Point\",\n" + 
				"\n" + 
				"          \"coordinates\": [\n" + 
				"\n" + 
				"            -98.505845,\n" + 
				"\n" + 
				"            29.553513\n" + 
				"\n" + 
				"          ]\n" + 
				"\n" + 
				"        }\n" + 
				"\n" + 
				"      }\n" + 
				"\n" + 
				"    ]\n" + 
				"\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		String criteria = "_VERIFY.JSON.PART_ .data[?(@.assetId=~ /.*Asset_2019-11-13T21:47:07/i)]:jsonbody("+partialJson+")";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonOriginal);
		Helper.assertTrue("errors caught", error.isEmpty());
	}
}