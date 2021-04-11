package test.module.framework.tests.functional.service;


import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.JsonHelper;
import core.apiCore.interfaces.ExternalInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.ServiceObject;
import data.Data;
import io.restassured.response.Response;
import module.common.data.CommonUser;
import net.minidev.json.JSONArray;
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
			"                \"isbn\": \"H/APPR\",\n" + 
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
		
		boolean isJson = JsonHelper.isJSONValid(jsonString, false);
		Helper.assertTrue("is not valid json", isJson);
	}
	
	@Test()
	public void isJSONValid_invalid() {
		
		TestLog.When("I verify an invalid json string");
		String jsonStringInvalid = jsonString.replaceFirst("\\{", "[");
		boolean isJson = JsonHelper.isJSONValid(jsonStringInvalid, false);
		Helper.assertTrue("should be invalid json", !isJson);
	}
	
	@Test()
	public void isJSONValid_invalid_indicator() {
		
		TestLog.When("I verify an invalid json string");
		String keyword_indicator = DataHelper.VERIFY_JSON_PART_INDICATOR;
		boolean isJson = JsonHelper.isJSONValid(keyword_indicator + "\n " + jsonString, false);
		Helper.assertTrue("should be invalid json", !isJson);
		
		keyword_indicator = DataHelper.VERIFY_RESPONSE_BODY_INDICATOR;
		isJson = JsonHelper.isJSONValid(keyword_indicator + "\n " + jsonString, false);
		Helper.assertTrue("should be invalid json", !isJson);
		
		keyword_indicator = DataHelper.VERIFY_RESPONSE_NO_EMPTY;
		 isJson = JsonHelper.isJSONValid(keyword_indicator + "\n " + jsonString, false);
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
		JsonHelper.saveOutboundJsonParameters(response, "role.name:<$name>; provider:1:<$local>");
		Helper.assertEquals("Authenticated", Config.getValue("name"));
		Helper.assertEquals("local", Config.getValue("local"));
	}
	
	@Test()
	public void configMapJsonKeyValues_command() throws Exception {
		
		CommonUser user = Data.common.commonuser().withAdminLogin();
		
		TestLog.When("I create a user through rest api");
	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
		
		TestLog.Then("I verify role name is stored in a variable");
		JsonHelper.saveOutboundJsonParameters(response, "command(Command.getRoleNameValue):<$name>;  provider:1:<$local>");
		Helper.assertEquals("Authenticated", Config.getValue("name"));
		Helper.assertEquals("local", Config.getValue("local"));
		
		JsonHelper.saveOutboundJsonParameters(response, "command(Command.getRoleNameValue,param1,param2):<$name>;  provider:1:<$local>");
		Helper.assertEquals("Authenticated", Config.getValue("name"));
		Helper.assertEquals("local", Config.getValue("local"));
		
		// with empty response
		ServiceObject service = new ServiceObject();
		service.withRequestBody("response: ;values:parameter1")
		.withMethod("METHOD:Command.getRoleNameValue");
		Object value = ExternalInterface.evaluateTestMethod(service);
		Helper.assertTrue("value should be empty", value.toString().isEmpty());
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void configMapJsonKeyValues_invalid_variable_format() {
		TestLog.When("I create a user through rest api");

		CommonUser user = Data.common.commonuser().withAdminLogin();
	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
		
		TestLog.Then("I verify invalid variable format throws assertion error");
		JsonHelper.saveOutboundJsonParameters(response, "role.name:1:<$name>; provider:1:<@local>");
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
				"      \"isbn\" : \"H/APPR\",\n" + 
				"      \"price\" : 8.99\n" + 
				"   }\n" + 
				"]";
		
		String error = JsonHelper.validateByJsonBody(expectedResult, result);
		Helper.assertTrue("errors not caught: " + error, error.isEmpty());
	}
	
	@Test()
	public void getJsonValue_jsonString_array() {
		TestLog.Then("I verify getting json value from path");
		
		String jsonpath = "$..[?(@.workOrder=~ /.*50b3c835-d2cf-4739-ae3e-4e4285106174/i)].plannedStartDateTime";
		
		String json = "{\n" + 
				"   \"appointments\":[\n" + 
				"      {\n" + 
				"         \"appointmentUUID\":\"428f0df1-40f1-497f-b9ad-bec9b5e8ceff\",\n" + 
				"         \"appointmentId\":\"WO_SCHAPI_001\",\n" + 
				"         \"appointmentName\":\"Create WorkOrder WO_SCHAPI_001\",\n" + 
				"         \"appointmentDescription\":\"Auto Scheduled\",\n" + 
				"         \"plannedStartDateTime\":\"2020-06-25T09:08:16Z\",\n" + 
				"         \"plannedEndDateTime\":\"2020-06-25T09:38:16Z\",\n" + 
				"         \"workOrder\":\"50b3c835-d2cf-4739-ae3e-4e4285106174\",\n" + 
				"         \"assignedTo\":[\n" + 
				"            \"c06bed88-8698-4674-b3f5-9a1e3d80543c\"\n" + 
				"         ],\n" + 
				"         \"status\":\"Deleted\",\n" + 
				"         \n" + 
				"               \"group\":\"scheduling\"\n" + 
				"            }\n" + 
				"         ],\n" + 
				"         \"createdTimestamp\":\"2020-06-20T03:35:17.011Z\",\n" + 
				"         \"lastModifiedTimestamp\":\"2020-06-23T01:40:55.32Z\",\n" + 
				"         \"attachments\":[\n" + 
				"         ]\n" + 
				"      }\n" + 
				"   ]\n" + 
				"}";
		
		String uuid = JsonHelper.getJsonValue(json, jsonpath);
		Helper.assertEquals("2020-06-25T09:08:16Z", uuid);
	}
	
	@Test()
	public void getJsonValue_jsonString_array_keyValue() {
		TestLog.Then("I verify getting json value from path");
		
		String jsonpath = "$..[?(@.workOrder=~ /.*50b3c835-d2cf-4739-ae3e-4e4285106174/i)].plannedStartDateTime";
		
		String json = "{\n" + 
				"   \"appointments\":[\n" + 
				"      {\n" + 
				"         \"appointmentUUID\":\"428f0df1-40f1-497f-b9ad-bec9b5e8ceff\",\n" + 
				"         \"appointmentId\":\"WO_SCHAPI_001\",\n" + 
				"         \"appointmentName\":\"Create WorkOrder WO_SCHAPI_001\",\n" + 
				"         \"appointmentDescription\":\"Auto Scheduled\",\n" + 
				"         \"plannedStartDateTime\":\"2020-06-25T09:0816Z\",\n" + 
				"         \"plannedEndDateTime\":\"2020-06-25T09:38:16Z\",\n" + 
				"         \"workOrder\":\"50b3c835-d2cf-4739-ae3e-4e4285106174\",\n" + 
				"         \"assignedTo\":[\n" + 
				"            \"c06bed88-8698-4674-b3f5-9a1e3d80543c\"\n" + 
				"         ],\n" + 
				"         \"status\":\"Deleted\",\n" + 
				"         \n" + 
				"               \"group\":\"scheduling\"\n" + 
				"            }\n" + 
				"         ],\n" + 
				"         \"createdTimestamp\":\"2020-06-20T03:35:17.011Z\",\n" + 
				"         \"lastModifiedTimestamp\":\"2020-06-23T01:40:55.32Z\",\n" + 
				"         \"attachments\":[\n" + 
				"         ]\n" + 
				"      }\n" + 
				"   ]\n" + 
				"}";
		
		String uuid = JsonHelper.getJsonValue(json, jsonpath);
		Helper.assertEquals("2020-06-25T09:0816Z", uuid);
	}
	
	@Test()
	public void getJsonValue_jsonString_multiValueArray() {
		TestLog.Then("I verify getting json value from path");
		
		String jsonPath = ".category";
		String json = "[\n" + 
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
				"      \"isbn\" : \"H/APPR\",\n" + 
				"      \"price\" : 8.99\n" + 
				"   }\n" + 
				"]";
		
		String expectedResponse = "reference,fiction";
		String categories = JsonHelper.getJsonValue(json, jsonPath);
		Helper.assertEquals(expectedResponse, categories);
	}
	
	@Test()
	public void getJsonValue_allValue() {
		TestLog.Then("I verify getting json value from path");
		
		String jsonPath = ".";
		String json = "[\n" + 
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
				"      \"isbn\" : \"H/APPR\",\n" + 
				"      \"price\" : 8.99\n" + 
				"   }\n" + 
				"]";
		
		String categories = JsonHelper.getJsonValue(json, jsonPath);
		Helper.assertEquals(json, categories);
	}
	
	@Test()
	public void getJsonValue_jsonString_array_response() {
		TestLog.Then("I verify getting json value from path");
		
		String jsonpath = "$..[?(@.workOrder=~ /.*50b3c835-d2cf-4739-ae3e-4e4285106174/i)]";
		
		String json = "{\n" + 
				"   \"appointments\":[\n" + 
				"      {\n" + 
				"         \"appointmentUUID\":\"428f0df1-40f1-497f-b9ad-bec9b5e8ceff\",\n" + 
				"         \"appointmentId\":\"WO_SCHAPI_001\",\n" + 
				"         \"appointmentName\":\"Create WorkOrder WO_SCHAPI_001\",\n" + 
				"         \"appointmentDescription\":\"Auto Scheduled\",\n" + 
				"         \"plannedStartDateTime\":\"2020-06-25T09:08:16Z\",\n" + 
				"         \"plannedEndDateTime\":\"2020-06-25T09:38:16Z\",\n" + 
				"         \"workOrder\":\"50b3c835-d2cf-4739-ae3e-4e4285106174\",\n" + 
				"         \"assignedTo\":[\n" + 
				"            \"c06bed88-8698-4674-b3f5-9a1e3d80543c\"\n" + 
				"         ],\n" + 
				"         \"status\":\"Deleted\",\n" + 
				"         \n" + 
				"               \"group\":\"scheduling\"\n" + 
				"            }\n" + 
				"         ],\n" + 
				"         \"createdTimestamp\":\"2020-06-20T03:35:17.011Z\",\n" + 
				"         \"lastModifiedTimestamp\":\"2020-06-23T01:40:55.32Z\",\n" + 
				"         \"attachments\":[\n" + 
				"         ]\n" + 
				"      }\n" + 
				"   ]\n" + 
				"}";
		
		String expectedResponse = "[{\"appointmentUUID\":\"428f0df1-40f1-497f-b9ad-bec9b5e8ceff\",\"appointmentId\":\"WO_SCHAPI_001\",\"appointmentName\":\"Create WorkOrder WO_SCHAPI_001\",\"appointmentDescription\":\"Auto Scheduled\",\"plannedStartDateTime\":\"2020-06-25T09:08:16Z\",\"plannedEndDateTime\":\"2020-06-25T09:38:16Z\",\"workOrder\":\"50b3c835-d2cf-4739-ae3e-4e4285106174\",\"assignedTo\":[\"c06bed88-8698-4674-b3f5-9a1e3d80543c\"],\"status\":\"Deleted\",\"group\":\"scheduling\"}]";
		String uuid = JsonHelper.getJsonValue(json, jsonpath);
		Helper.assertEquals(expectedResponse, uuid);
	}
	
	
	public void getJsonValue_with_json_string() {
		
		TestLog.Then("I verify getting json value from path");
		String name = JsonHelper.getJsonValue(jsonString, "role.name");
		Helper.assertEquals("Authenticated", name);
	}
	
	@Test()
	public void getJsonValue_with_invalid_path() {
		
		String error = JsonHelper.getJsonValue(jsonString, "role[0].name");
		Helper.assertEquals(null, error);
	}
	
	@Test()
	public void getJsonValue_with_jsonArray_invalidpath() {
		
		String jsonValue = "[{\"id\":1,\"username\":\"autoAdmin1\",\"email\":\"aut@email\","
				+ "\"provider\":\"local\",\"confirmed\":true,\"blocked\":null,\"role\":1}]";
		
		TestLog.Then("I verify getting json value from path");
		String error = JsonHelper.getJsonValue(jsonValue, "id");
		Helper.assertEquals(null, error);
	}
	
	@Test()
	public void getJsonValue_with_JSONArray() {
		String jsonValue = "[{\"id\":1,\"username\":\"autoAdmin1\",\"email\":\"aut@email\","
				+ "\"provider\":\"local\",\"confirmed\":true,\"blocked\":null,\"role\":1}]";
		
		TestLog.Then("I verify getting json value from path");
		String id = JsonHelper.getJsonValue(jsonValue, ".id");
		Helper.assertEquals("1", id);
	}
	
	@Test()
	public void getJsonValue_with_invalid_unavailable_path() {
		
		TestLog.Then("I verify getting json value from path");
		String error = JsonHelper.getJsonValue(jsonString, "role.name2");
		Helper.assertEquals(null, error);
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
	public void replaceJsonPathValue_add_new_node_boolean() {
		
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
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, "additionalProperties", "false");
		String updatedValue = JsonHelper.getJsonValue(updatedJson, "additionalProperties");
		Helper.assertEquals("false", updatedValue);
	}
	
	@Test()
	public void replaceJsonPathValue_add_new_node_string() {
		
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
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, "additionalProperties", "value1");
		String updatedValue = JsonHelper.getJsonValue(updatedJson, "additionalProperties");
		Helper.assertEquals("value1", updatedValue);
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
	public void replaceJsonPathValue_null_value() {
		String jsonPath = ".prefix";
		String newVal = "value1";
		
		String originalJson = "{\n" + 
				"\"individualId\": \"DDDDDDDD-DDDD-4444\",\n" + 
				"\"familyName\": \"DDDD4\",\n" + 
				"\"givenName\": null,\n" + 
				"\"middleName\": null,\n" + 
				"\"preferredName\": null,\n" + 
				"\"prefix\": null,\n" + 
				"\"suffix\": null,\n" + 
				"\"workLocation\": null,\n" + 
				"\"partyStatus\": \"active\",\n" + 
				"\"partyRelationship\": null,\n" + 
				"\"solutionAttributes\": [],\n" + 
				"\"customAttributes\": []\n" + 
				"}";
		
		String updatedValue = JsonHelper.getJsonValue(originalJson, ".givenName");	
		Helper.assertEquals("null", updatedValue);
		
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, newVal);
		updatedValue = JsonHelper.getJsonValue(updatedJson, jsonPath);
		Helper.assertEquals(newVal, updatedValue);	
		
		updatedJson = JsonHelper.replaceJsonPathValue(originalJson, ".familyName", "null");
		updatedValue = JsonHelper.getJsonValue(updatedJson, jsonPath);
		Helper.assertEquals("null", updatedValue);
		
		updatedJson = JsonHelper.replaceJsonPathValue(originalJson, ".workLocation", "vancouver");
		updatedValue = JsonHelper.getJsonValue(updatedJson, ".workLocation");
		Helper.assertEquals("vancouver", updatedValue);
		
		updatedJson = JsonHelper.replaceJsonPathValue(originalJson, ".workLocation", "12.1");
		updatedValue = JsonHelper.getJsonValue(updatedJson, ".workLocation");
		Helper.assertEquals("12.1", updatedValue);
	}
	
	@Test()
	public void replaceJsonPathValue_number() {
		String jsonPath = ".givenName";
		
		String originalJson = "{\n" + 
				"\"individualId\": \"DDDDDDDD-DDDD-4444\",\n" + 
				"\"familyName\": \"DDDD4\",\n" + 
				"\"givenName\": 12.1,\n" + 
				"\"middleName\": null,\n" + 
				"\"preferredName\": null,\n" + 
				"\"prefix\": null,\n" + 
				"\"suffix\": null,\n" + 
				"\"workLocation\": null,\n" + 
				"\"partyStatus\": \"active\",\n" + 
				"\"partyRelationship\": null,\n" + 
				"\"solutionAttributes\": [],\n" + 
				"\"customAttributes\": []\n" + 
				"}";
		
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, "313");
		Object updatedValue = JsonHelper.getJsonPathValue(updatedJson, jsonPath, true);
	
		Object value = null;
		if(updatedValue instanceof List) {
			net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) updatedValue;
			if(!array.isEmpty())
				value = array.get(0);
		}
		Helper.assertTrue("updatedValue is not integer: " + value, value instanceof Integer);
		
		updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, "313.453");
		updatedValue = JsonHelper.getJsonPathValue(updatedJson, jsonPath, true);
	
		value = null;
		if(updatedValue instanceof List) {
			net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) updatedValue;
			if(!array.isEmpty())
				value = array.get(0);
		}
		Helper.assertTrue("updatedValue is not Double: " + value, value instanceof Double);
	}
	
	@Test()
	public void replaceJsonPathValue_boolean() {
		String jsonPath = ".partyStatus";
		
		String originalJson = "{\n" + 
				"\"individualId\": \"DDDDDDDD-DDDD-4444\",\n" + 
				"\"familyName\": \"DDDD4\",\n" + 
				"\"givenName\": 12,\n" + 
				"\"middleName\": null,\n" + 
				"\"preferredName\": null,\n" + 
				"\"prefix\": null,\n" + 
				"\"suffix\": null,\n" + 
				"\"workLocation\": null,\n" + 
				"\"partyStatus\": true,\n" + 
				"\"partyRelationship\": null,\n" + 
				"\"solutionAttributes\": [],\n" + 
				"\"customAttributes\": []\n" + 
				"}";
		
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, "false");
		Object updatedValue = JsonHelper.getJsonPathValue(updatedJson, jsonPath, true);
	
		Object value = null;
		if(updatedValue instanceof List) {
			net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) updatedValue;
			if(!array.isEmpty())
				value = array.get(0);
		}
		Helper.assertTrue("updatedValue is not boolean: " + value, value instanceof Boolean);
	}
	
	@Test()
	public void replaceJsonPathValue_null() {
		String jsonPath = ".prefix";
		
		String originalJson = "{\n" + 
				"\"individualId\": \"DDDDDDDD-DDDD-4444\",\n" + 
				"\"familyName\": \"DDDD4\",\n" + 
				"\"givenName\": 12,\n" + 
				"\"middleName\": null,\n" + 
				"\"preferredName\": null,\n" + 
				"\"prefix\": null,\n" + 
				"\"suffix\": null,\n" + 
				"\"workLocation\": null,\n" + 
				"\"partyStatus\": true,\n" + 
				"\"partyRelationship\": null,\n" + 
				"\"solutionAttributes\": [],\n" + 
				"\"customAttributes\": []\n" + 
				"}";
		
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, "null");
		Object updatedValue = JsonHelper.getJsonPathValue(updatedJson, jsonPath, true);
	
		Object value = null;
		if(updatedValue instanceof List) {
			net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) updatedValue;
			if(!array.isEmpty())
				value = array.get(0);
		}
		Helper.assertTrue("updatedValue is not null: " + value, value == null);
	}
	
	@Test()
	public void replaceJsonPathValue_String() {
		String jsonPath = ".individualId";
		
		String originalJson = "{\n" + 
				"\"individualId\": \"DDDDDDDD-DDDD-4444\",\n" + 
				"\"familyName\": \"DDDD4\",\n" + 
				"\"givenName\": 12,\n" + 
				"\"middleName\": null,\n" + 
				"\"preferredName\": null,\n" + 
				"\"prefix\": null,\n" + 
				"\"suffix\": null,\n" + 
				"\"workLocation\": null,\n" + 
				"\"partyStatus\": true,\n" + 
				"\"partyRelationship\": null,\n" + 
				"\"solutionAttributes\": [],\n" + 
				"\"customAttributes\": []\n" + 
				"}";
		
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, jsonPath, "person");
		Object updatedValue = JsonHelper.getJsonPathValue(updatedJson, jsonPath, true);
	
		Object value = null;
		if(updatedValue instanceof List) {
			net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) updatedValue;
			if(!array.isEmpty())
				value = array.get(0);
		}
		Helper.assertTrue("updatedValue is not string: " + value, value instanceof String);
		
		// testing replacing array
		
		updatedJson = JsonHelper.replaceJsonPathValue(originalJson, ".solutionAttributes", "[]");
		updatedValue = JsonHelper.getJsonPathValue(updatedJson, ".solutionAttributes", true);
	
		value = null;
		if(updatedValue instanceof List) {
			net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) updatedValue;
			if(!array.isEmpty())
				value = array.get(0);
		}
		Helper.assertTrue("updatedValue is not string: " + value, value instanceof JSONArray);
	}
	
	@Test()
	public void replaceJsonPathValue_Array() {
		
		String originalJson = "{\n" + 
				"\"individualId\": \"DDDDDDDD-DDDD-4444\",\n" + 
				"\"familyName\": \"DDDD4\",\n" + 
				"\"givenName\": 12,\n" + 
				"\"middleName\": null,\n" + 
				"\"preferredName\": null,\n" + 
				"\"prefix\": null,\n" + 
				"\"suffix\": null,\n" + 
				"\"workLocation\": null,\n" + 
				"\"partyStatus\": true,\n" + 
				"\"partyRelationship\": null,\n" + 
				"\"solutionAttributes\": [],\n" + 
				"\"customAttributes\": [ \"Sahil\", \"Vivek\", \"Rahul\" ]\n" + 
				"}";
		
		// testing replacing array
		
		String updatedJson = JsonHelper.replaceJsonPathValue(originalJson, ".solutionAttributes", "[]");
		Object updatedValue = JsonHelper.getJsonPathValue(updatedJson, ".solutionAttributes", true);
	
		Object value = null;
		if(updatedValue instanceof List) {
			net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) updatedValue;
			if(!array.isEmpty())
				value = array.get(0);
		}
		Helper.assertTrue("updatedValue is not array: " + value, value instanceof JSONArray);
		
		
		updatedJson = JsonHelper.replaceJsonPathValue(originalJson, ".customAttributes", "[ \"Sahil2\", \"Vivek2\", \"Rahul2\" ]");
		updatedValue = JsonHelper.getJsonPathValue(updatedJson, ".customAttributes", true);
	
		value = null;
		if(updatedValue instanceof List) {
			net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) updatedValue;
			if(!array.isEmpty())
				value = array.get(0);
		}
		Helper.assertTrue("updatedValue is not array: " + value, value instanceof JSONArray);
		
		updatedJson = JsonHelper.replaceJsonPathValue(originalJson, ".customAttributes", "[\n" + 
				"        {\"id\" : 101},\n" + 
				"        {\"id\" : 102},\n" + 
				"        {\"id\" : 103}\n" + 
				"  ]");
		updatedValue = JsonHelper.getJsonPathValue(updatedJson, ".customAttributes", true);
	
		value = null;
		if(updatedValue instanceof List) {
			net.minidev.json.JSONArray array = (net.minidev.json.JSONArray) updatedValue;
			if(!array.isEmpty())
				value = array.get(0);
		}
		Helper.assertTrue("updatedValue is not array: " + value, value instanceof JSONArray);
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

		String criteria = "_VERIFY_RESPONSE_BODY_ contains(confirmed)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_invalid() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ contains(invalid)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_hasItems() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ hasItems(confirmed)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ hasItems(invalid)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_notHaveItems() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ notHaveItems(invalid)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ notHaveItems(confirmed)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_equalTo() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ equalTo(response value)";
		String error = JsonHelper.validateResponseBody(criteria, "response value");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ equalTo({\"id\":\"1234\"})";
		error = JsonHelper.validateResponseBody(criteria, "{\"id\":\"1234\"}");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ equalTo(response)";
		error = JsonHelper.validateResponseBody(criteria, "response value");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_notEqualTo() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ notEqualTo(invalid)";
		String error = JsonHelper.validateResponseBody(criteria, "response value");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ notEqualTo(response value)";
		error = JsonHelper.validateResponseBody(criteria, "response value");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_notContain() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ notContain(invalid)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ notContain(confirmed)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
		
		String json2 = "{\n" + 
				"    \"workOrderId\": \"WO_<@_RAND6>\",\n" + 
				"    \"workOrderUUID\": \"WO_<@_RAND6>\",\n" + 
				"    \"description\": \"Create WorkOrder\"}";
		
		criteria = "_VERIFY_RESPONSE_BODY_ notContain(Create WorkOrder)";
		error = JsonHelper.validateResponseBody(criteria, json2);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}

	@Test()
	public void validateResponseBody_contains() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ contains(confirmed)";
		String error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ contains(\"confirmed\": null)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ contains(invalid)";
		error = JsonHelper.validateResponseBody(criteria, jsonString);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_integerGreaterThan() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ integerGreaterThan(4)";
		String error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ integerGreaterThan(6)";
		error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_integerLessThan() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ integerLessThan(6)";
		String error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ integerLessThan(4)";
		error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_integerEqual() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ integerEqual(5.32)";
		String error = JsonHelper.validateResponseBody(criteria, "5.32");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ integerEqual(4.33)";
		error = JsonHelper.validateResponseBody(criteria, "5.32");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_integerNotEqual() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ integerNotEqual(6)";
		String error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ integerNotEqual(5)";
		error = JsonHelper.validateResponseBody(criteria, "5");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_isNotEmpty() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ isNotEmpty";
		String error = JsonHelper.validateResponseBody(criteria, "json response");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ isNotEmpty";
		error = JsonHelper.validateResponseBody(criteria, "");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateResponseBody_isEmpty() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_RESPONSE_BODY_ isEmpty";
		String error = JsonHelper.validateResponseBody(criteria, "");
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ isEmpty";
		error = JsonHelper.validateResponseBody(criteria, "json response");
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_hasItems() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_JSON_PART_ store.book[?(@.price < 10)].author:hasItems(Herman Melville)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book..isbn:hasItems(H/APPR))";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
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
		
		String json = "{\n" + 
				"    \"data\": {\n" + 
				"        \"userStatus\": \"APPROVED\",\n" + 
				"        \"assetWork\": false,\n" + 
				"        \"plannedFinishDateTime\": \"2019-11-28T21:41:36.000Z\",\n" + 
				"        \"actualDuration\": 12421,\n" + 
				"        \"description\": \"Scheduling GUI Automation - pre test case test data cleanup\",\n" + 
				"        \"responsibleOrg\": {\n" + 
				"            \"partyUUID\": \"01\"\n" + 
				"        },\n" + 
				"        \"assignedTo\": {\n" + 
				"            \"partyUUID\": \"John Doe\"\n" + 
				"        },\n" + 
				"        \"legalEntity\": {\n" + 
				"            \"partyUUID\": \"\"\n" + 
				"        },\n" + 
				"        \"children\": [],\n" + 
				"        \"plannedDuration\": 3600000,\n" + 
				"        \"plannedStartDateTime\": \"2019-11-25T21:41:36.000Z\",\n" + 
				"        \"raisedBy\": {\n" + 
				"            \"partyUUID\": \"BLUDWIG\"\n" + 
				"        },\n" + 
				"        \"scheduleFlag\": \"Ready\",\n" + 
				"        \"raisedDateTime\": \"2019-10-15T16:00:00.000Z\",\n" + 
				"        \"parentWorkOrder\": {\n" + 
				"            \"workOrderUUID\": \"WO1\"\n" + 
				"        },\n" + 
				"        \"createdTimestamp\": \"2019-10-03T21:03:26.895Z\",\n" + 
				"        \"workOrderUUID\": \"5b92ffb2-c29b-4632-a694-ae9702f54065\",\n" + 
				"        \"priority\": 6,\n" + 
				"        \"lastModifiedTimestamp\": \"2020-03-20T01:52:06.646Z\",\n" + 
				"        \"initiationType\": \"Schedule\",\n" + 
				"        \"closedComments\": \"Completion comments\",\n" + 
				"        \"workType\": \"Preventative\",\n" + 
				"        \"solutionAttributes\": {\n" + 
				"            \"assetSuite\": {\n" + 
				"                \"unit\": \"12\",\n" + 
				"                \"shutdownNbr\": \"12345678\",\n" + 
				"                \"resourceCode\": \"56\",\n" + 
				"                \"referenceNbr\": \"002-111\",\n" + 
				"                \"referenceType\": \"WO\",\n" + 
				"                \"outageUnit\": \"123456\",\n" + 
				"                \"jobType\": \"CA\",\n" + 
				"                \"referenceSubNbr\": \"01\",\n" + 
				"                \"planner\": \"BLUDWIG\",\n" + 
				"                \"resourceType\": \"34\"\n" + 
				"            },\n" + 
				"            \"scheduling\": {\n" + 
				"                \"autoSchedulingFlag\": \"Ready\"\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"location\": {\n" + 
				"            \"locationUUID\": \"0f8decd3-21b4-4a0a-8975-8d5ca82a8b45\",\n" + 
				"            \"createdTimestamp\": \"2019-11-07T00:28:41.554Z\",\n" + 
				"            \"locationGIS\": {\n" + 
				"                \"coordinates\": [\n" + 
				"                    -123.150507,\n" + 
				"                    49.299778\n" + 
				"                ],\n" + 
				"                \"type\": \"Point\"\n" + 
				"            },\n" + 
				"            \"solutionAttributes\": {},\n" + 
				"            \"lastModifiedTimestamp\": \"2019-11-07T00:28:41.554Z\",\n" + 
				"            \"customAttributes\": {}\n" + 
				"        },\n" + 
				"        \"workOrderId\": \"WO_SCHAPI_002\",\n" + 
				"        \"extendedDescription\": \"Extended description\",\n" + 
				"        \"account\": {\n" + 
				"            \"accountUUID\": \"\"\n" + 
				"        },\n" + 
				"        \"status\": \"Closed\"\n" + 
				"    }\n" + 
				"}";
		
		criteria = "_VERIFY_JSON_PART_ data:hasItems(children)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ data:hasItems(workOrderUUID, workOrderId, description, status, initiationType, children, raisedDateTime)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ data:hasItems(children2)";
		error = JsonHelper.validateByKeywords(criteria, json);
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
	public void validateByKeywords_custom_command() {
		String criteria = "_VERIFY_JSON_PART_ .price:command(Command.isAllNumbersGreaterThanSumOf,3,5)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .price:command(Command.isAllNumbersGreaterThanSumOf,10,1)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_custom_command_no_parameter() {	
		
		String criteria = "_VERIFY_JSON_PART_ .price:command(Command.isAllDifferent)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void validateByKeywords_custom_command_wrong_parameters() {
		String criteria = "_VERIFY_JSON_PART_ .price:command(Command.isAllNumbersGreaterThanSumOf)";
		JsonHelper.validateByKeywords(criteria, jsonBookStore);
	}
	
	@Test( )
	public void validateByKeywords_command_date() {
		
		String dateJson = "{\"dates\":[\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-05T12:08:56\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-10T12:08:56\"\n" + 
				"            },\n" + 
				"           {\n" + 
				"               \"date\":\"2001-05-15T12:08:56\"\n" + 
				"            }\n" + 
				"]}";
		
		String criteria = "_VERIFY_JSON_PART_ .date:command(Command.isDateBetween,2001-05-04T12:08:56,2001-05-16T12:08:56)";
		List<String> error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors not caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_custom_command_empty_jsonpath() {
		String criteria = "_VERIFY_JSON_PART_ .price2:command(Command.isAllNumbersGreaterThanSumOf,3,5)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.get(0).equals("response is empty"));
	}
	
	@Test()
	public void validateByKeywords_custom_command_empty_jsonpath_null() {
		String criteria = "_VERIFY_JSON_PART_ store.book[?(@.price < 10)].author:command(Command.isAllNumbersGreaterThanSumOf,3,5)";
		List<String> error = JsonHelper.validateByKeywords(criteria, "[]");
		Helper.assertTrue("errors caught", error.get(0).equals("response is empty"));
	}
	
	@Test()
	public void validateResponseBody_custom_command() {
		
		String criteria = "_VERIFY_RESPONSE_BODY_ command(Command.containsValue,Nigel Rees)";
		String error = JsonHelper.validateResponseBody(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_RESPONSE_BODY_ command(Command.containsValue,invalid)";
		error = JsonHelper.validateResponseBody(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", !error.isEmpty());
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
		
		
		String json = "[{\"appointmentUUID\":\"2c50e604-3aa7-4d8e-888f-84d12fdd05b5\",\"party\":\"e6e42f5f-9375-410b-9ded-adc1c31ea789\",\"workOrder\":\"2c50e604-3aa7-4d8e-888f-84d12fdd05b5\",\"appointmentId\":\"WO_SCHAPI_001\",\"appointmentName\":\"Update WorkOrder WO_SCHAPI_001 planned start date time\",\"status\":\"Scheduled\",\"travelDuration\":603000,\"returnTravelDuration\":0,\"returnTravelStart\":1580320800000,\"estimatedOnsite\":1580288022000,\"estimatedComplete\":1580289822000,\"estimatedEnroute\":1580287419000,\"lockFlag\":0,\"shiftId\":\"12229\"},{\"appointmentUUID\":\"35d8e48f-bbb3-44b3-879f-885bcabd9a5a\",\"party\":\"e6e42f5f-9375-410b-9ded-adc1c31ea789\",\"workOrder\":\"35d8e48f-bbb3-44b3-879f-885bcabd9a5a\",\"appointmentId\":\"WO_SCHAPI_002\",\"appointmentName\":\"Create WorkOrder WO_SCHAPI_002\",\"status\":\"Scheduled\",\"travelDuration\":105000,\"returnTravelDuration\":159000,\"returnTravelStart\":1580320641000,\"estimatedOnsite\":1580289927000,\"estimatedComplete\":1580291727000,\"estimatedEnroute\":1580289822000,\"lockFlag\":0,\"shiftId\":\"12229\"},{\"appointmentUUID\":\"4beb68e5-553e-4f6d-9fec-500e4f5b43a8\",\"party\":\"e6e42f5f-9375-410b-9ded-adc1c31ea789\",\"workOrder\":\"4beb68e5-553e-4f6d-9fec-500e4f5b43a8\",\"appointmentId\":\"WO_SCHAPI_004\",\"appointmentName\":\"Create WorkOrder WO_SCHAPI_004\",\"status\":\"Scheduled\",\"travelDuration\":819000,\"returnTravelDuration\":0,\"returnTravelStart\":1580320800000,\"estimatedOnsite\":1580285619000,\"estimatedComplete\":1580287419000,\"estimatedEnroute\":1580284800000,\"lockFlag\":0,\"shiftId\":\"12229\"},{\"appointmentUUID\":\"a059e007-f53a-42d4-baae-4fced7da6b7f\",\"party\":\"40e2cc5d-6225-4447-9f0c-bc9015613ca8\",\"workOrder\":\"a059e007-f53a-42d4-baae-4fced7da6b7f\",\"appointmentId\":\"WO_SCHAPI_003\",\"appointmentName\":\"Create WorkOrder WO_SCHAPI_003\",\"status\":\"Scheduled\",\"travelDuration\":243000,\"returnTravelDuration\":243000,\"returnTravelStart\":1580320557000,\"estimatedOnsite\":1580285043000,\"estimatedComplete\":1580285943000,\"estimatedEnroute\":1580284800000,\"lockFlag\":0,\"shiftId\":\"12225\"}]";
		criteria = "_VERIFY.JSON.PART_ [?(@.workOrder == \"2c50e604-3aa7-4d8e-888f-84d12fdd05b5\")].estimatedOnsite:contains(2020-01-29T14:00:00.000Z)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	
		json = "{\"timestamp\":\"2020-02-11T20:59:55.119+0000\",\"status\":500,\"error\":\"Internal Server Error\",\"message\":\"Invalid Request Body: Json Validation Error for de.product.service.requirement.api.createupdate.request-1.0.0.json\",\"path\":\"/v1/productservicerequirements\"}\r\n" + 
				"2020-02-11 12:54";
		criteria = "_VERIFY_JSON_PART_ .message:contains(Invalid Request Body: Json Validation Error for de.product.service.requirement.api.createupdate.request-1.0.0.json)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .message:contains(Invalid, de.product, createupdate)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		json = "{\"key\":\"value1234\"}";
		criteria = "_VERIFY.JSON.PART_ .key:contains(1234)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		json = "{\n" + 
				"    \"Timestamp\": \"03/05/2020 08:23:01 +00:00\",\n" + 
				"    \"Status\": 400,\n" + 
				"    \"Error\": \"Bad Request\",\n" + 
				"    \"Message\": \"Json Validation Error against de.party.api.createupdate.request-1.0.0.json : #/PartyRelationship/2/startDate \\\"1582812826442\\\" is not in an acceptable \\\"date-time\\\" format.;#/PartyRelationship/2/startDate Values of type \\\"string\\\" are not one of the allowed types \\\"integer\\\".;#/PartyRelationship/2/startDate \\\"1582812826442\\\" is not in an acceptable \\\"date-time\\\" format.\",\n" + 
				"    \"Path\": \"/v1/parties\"\n" + 
				"}";
		
		criteria = "_VERIFY.JSON.PART_ $.Message:contains(\\\"1582812826442\\\" is not in an acceptable";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ $.Message:contains(\\\"1582812826443\\\" is not in an acceptable";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_notContain() {
		TestLog.When("I verify a valid json against correct value");

		String criteria = "_VERIFY_JSON_PART_ store.book[?(@.price < 10)].author:notContain(invalid)";
		List<String> error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ store.book[?(@.price < 10)].author:notContain(Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ store.book[?(@.price < 10)].author:notContain(Nigel Rees, Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ store.book[?(@.price < 10)].author:notContain(Herman Melville, invalid)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ store.book[?(@.price < 10)].author:notContain(Evelyn Waugh, invalid)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		
		String json = "{\n" + 
				"    \"data\": {\n" + 
				"        \"userStatus\": \"APPROVED\",\n" + 
				"        \"assetWork\": false,\n" + 
				"        \"plannedFinishDateTime\": \"2019-11-28T21:41:36.000Z\",\n" + 
				"        \"actualDuration\": 12421,\n" + 
				"        \"description\": \"Scheduling GUI Automation - pre test case test data cleanup\",\n" + 
				"        \"responsibleOrg\": {\n" + 
				"            \"partyUUID\": \"01\"\n" + 
				"        },\n" + 
				"        \"assignedTo\": {\n" + 
				"            \"partyUUID\": \"John Doe\"\n" + 
				"        },\n" + 
				"        \"legalEntity\": {\n" + 
				"            \"partyUUID\": \"\"\n" + 
				"        },\n" + 
				"        \"children\": [],\n" + 
				"        \"plannedDuration\": 3600000,\n" + 
				"        \"plannedStartDateTime\": \"2019-11-25T21:41:36.000Z\",\n" + 
				"        \"raisedBy\": {\n" + 
				"            \"partyUUID\": \"BLUDWIG\"\n" + 
				"        },\n" + 
				"        \"scheduleFlag\": \"Ready\",\n" + 
				"        \"raisedDateTime\": \"2019-10-15T16:00:00.000Z\",\n" + 
				"        \"parentWorkOrder\": {\n" + 
				"            \"workOrderUUID\": \"WO1\"\n" + 
				"        },\n" + 
				"        \"createdTimestamp\": \"2019-10-03T21:03:26.895Z\",\n" + 
				"        \"workOrderUUID\": \"5b92ffb2-c29b-4632-a694-ae9702f54065\",\n" + 
				"        \"priority\": 6,\n" + 
				"        \"lastModifiedTimestamp\": \"2020-03-20T01:52:06.646Z\",\n" + 
				"        \"initiationType\": \"Schedule\",\n" + 
				"        \"closedComments\": \"Completion comments\",\n" + 
				"        \"workType\": \"Preventative\",\n" + 
				"        \"solutionAttributes\": {\n" + 
				"            \"assetSuite\": {\n" + 
				"                \"unit\": \"12\",\n" + 
				"                \"shutdownNbr\": \"12345678\",\n" + 
				"                \"resourceCode\": \"56\",\n" + 
				"                \"referenceNbr\": \"002-111\",\n" + 
				"                \"referenceType\": \"WO\",\n" + 
				"                \"outageUnit\": \"123456\",\n" + 
				"                \"jobType\": \"CA\",\n" + 
				"                \"referenceSubNbr\": \"01\",\n" + 
				"                \"planner\": \"BLUDWIG\",\n" + 
				"                \"resourceType\": \"34\"\n" + 
				"            },\n" + 
				"            \"scheduling\": {\n" + 
				"                \"autoSchedulingFlag\": \"Ready\"\n" + 
				"            }\n" + 
				"        },\n" + 
				"        \"location\": {\n" + 
				"            \"locationUUID\": \"0f8decd3-21b4-4a0a-8975-8d5ca82a8b45\",\n" + 
				"            \"createdTimestamp\": \"2019-11-07T00:28:41.554Z\",\n" + 
				"            \"locationGIS\": {\n" + 
				"                \"coordinates\": [\n" + 
				"                    -123.150507,\n" + 
				"                    49.299778\n" + 
				"                ],\n" + 
				"                \"type\": \"Point\"\n" + 
				"            },\n" + 
				"            \"solutionAttributes\": {},\n" + 
				"            \"lastModifiedTimestamp\": \"2019-11-07T00:28:41.554Z\",\n" + 
				"            \"customAttributes\": {}\n" + 
				"        },\n" + 
				"        \"workOrderId\": \"WO_SCHAPI_002\",\n" + 
				"        \"extendedDescription\": \"Extended description\",\n" + 
				"        \"account\": {\n" + 
				"            \"accountUUID\": \"\"\n" + 
				"        },\n" + 
				"        \"status\": \"Closed\"\n" + 
				"    }\n" + 
				"}";
		
		
		criteria = "_VERIFY_JSON_PART_ data:notContain(children2)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ data:notContain(children)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors not caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ data:notContain(workOrderUUID, workOrderId, description, status, initiationType, children, raisedDateTime)";
		error = JsonHelper.validateByKeywords(criteria, json);
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
		
		String json1 = "{\"attachments\": [\n" + 
				"                {\n" + 
				"                    \"name\": \"<@_TIME_MS_13>-att 4\",\n" + 
				"                    \"url\": \"http://1000lifehacks.com/1\"\n" + 
				"                },\n" + 
				"                {\n" + 
				"                    \"name\": \"<@_TIME_MS_13>-att 3\",\n" + 
				"                    \"url\": \"http://1000lifehacks.com/\"\n" + 
				"                },\n" + 
				"                {\n" + 
				"                    \"name\": \"<@_TIME_MS_13>-att 1\",\n" + 
				"                    \"url\": \"http://1000lifehacks.com/1\"\n" + 
				"                },\n" + 
				"                {\n" + 
				"                    \"attachmentId\": \"<@_TIME_MS_13>-a1\",\n" + 
				"                    \"name\": \"<@_TIME_MS_13>-att 5\",\n" + 
				"                    \"url\": \"http://1000lifehacks.com/1\"\n" + 
				"                },\n" + 
				"                {\n" + 
				"                    \"attachmentId\": \"<@_TIME_MS_13>-a2\",\n" + 
				"                    \"name\": \"<@_TIME_MS_13>-att 3\",\n" + 
				"                    \"url\": \"http://1000lifehacks.com/2\"\n" + 
				"                }\n" + 
				"            ]}";
		criteria = "_VERIFY_JSON_PART_ $.attachments.length():equalTo(5)";
		error = JsonHelper.validateByKeywords(criteria, json1);
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
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.isbn)]:nodeSizeGreaterThan(1)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors caught", error.isEmpty());
		
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
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.isbn)]:nodeSizeExact(2)";
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
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ .book[?(@.isbn)]:nodeSizeLessThan(3)";
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
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:sequence(\"Nigel Rees\",  Herman Melville)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", error.isEmpty());
		
		criteria = "_VERIFY.JSON.PART_ store.book[?(@.price < 10)].author:sequence(Herman Melville,Nigel Rees)";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
		Helper.assertTrue("errors not caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_dateInBetween() {
		String dateJson = "{\"dates\":[\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-05T12:08:56\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-10T12:08:56\"\n" + 
				"            },\n" + 
				"           {\n" + 
				"               \"date\":\"2001-05-15T12:08:56\"\n" + 
				"            }\n" + 
				"]}";
		
		String criteria = "_VERIFY_JSON_PART_ .date:isBetweenDate(2001-05-04T12:08:56, 2001-05-16T12:08:56)";
		List<String> error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isBetweenDate(2001-05-06T12:08:56, 2001-05-15T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isBetweenDate(2001-05-06T12:08:56, 2001-05-15T12:08:57)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isBetweenDate(2001-05-05T12:08:56, 2001-05-16T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isBetweenDate(2001-05-05T12:08:57, 2001-05-15T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:2:isBetweenDate(2001-05-05T12:08:56, 2001-05-15T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:2:isBetweenDate(2001-05-05T12:08:55, 2001-05-15T12:08:57)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:1:isBetweenDate(2001-05-05T12:08:56, 2001-05-15T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:1:isBetweenDate(2001-05-05T12:08:57, 2001-05-07T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());	
		
		criteria = "_VERIFY_JSON_PART_ .date:1:isBetweenDate(987448137, 992718537)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:1:isBetweenDate(987448137000, 992718537000)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_dateAfter() {
		String dateJson = "{\"dates\":[\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-05T12:08:56\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-10T12:08:56\"\n" + 
				"            },\n" + 
				"           {\n" + 
				"               \"date\":\"2001-05-15T12:08:56\"\n" + 
				"            }\n" + 
				"]}";
		
		String criteria = "_VERIFY_JSON_PART_ .date:isDateAfter(2001-05-03T12:08:56)";
		List<String> error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isDateAfter(2001-05-09T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isDateAfter(2001-05-16T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isDateAfter(2001-05-05T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:1:isDateAfter(2001-05-09T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_dateBefore() {
		String dateJson = "{\"dates\":[\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-05T12:08:56\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-10T12:08:56\"\n" + 
				"            },\n" + 
				"           {\n" + 
				"               \"date\":\"2001-05-15T12:08:56\"\n" + 
				"            }\n" + 
				"]}";
		
		String criteria = "_VERIFY_JSON_PART_ .date:isDateBefore(2001-05-16T12:08:56)";
		List<String> error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isDateBefore(2001-05-15T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isDateBefore(2001-05-09T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isDateBefore(2001-05-05T12:08:57)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:isDateBefore(2001-05-05T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:1:isDateBefore(2001-05-15T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_dateEqual() {
		String dateJson = "{\"dates\":[\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-05T12:08:56\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"               \"date\":\"2001-05-10T12:08:56\"\n" + 
				"            },\n" + 
				"           {\n" + 
				"               \"date\":\"2001-05-15T12:08:56\"\n" + 
				"            }\n" + 
				"]}";
		
		String criteria = "_VERIFY_JSON_PART_ .date:1:isDateEqual(2001-05-05T12:08:56)";
		List<String> error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:2:isDateEqual(2001-05-09T12:08:57)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:1:isDateNotEqual(2001-05-05T12:08:56)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .date:3:isDateNotEqual(2001-05-15T12:08:57)";
		error = JsonHelper.validateByKeywords(criteria, dateJson);
		Helper.assertTrue("errors caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_allValuesEqualTo() {
		String json = "{\"sameVals\":[\n" + 
				"            {\n" + 
				"               \"same\":\"11\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"               \"same\":\"11\"\n" + 
				"            },\n" + 
				"           {\n" + 
				"               \"same\":\"11\"\n" + 
				"            }\n" + 
				"]}";
		
		String criteria = "_VERIFY_JSON_PART_ .same:allValuesEqualTo(11)";
		List<String> error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors caught", error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .same:allValuesEqualTo(12)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors caught", !error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_allValuesEqualTo_different() {
		String json = "{\"sameVals\":[\n" + 
				"            {\n" + 
				"               \"same\":\"11\"\n" + 
				"            },\n" + 
				"            {\n" + 
				"               \"same\":\"11\"\n" + 
				"            },\n" + 
				"           {\n" + 
				"               \"same\":\"12\"\n" + 
				"            }\n" + 
				"]}";
		
		String criteria = "_VERIFY_JSON_PART_ .same:allValuesEqualTo(11)";
		List<String> error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors caught", !error.isEmpty());
		
		criteria = "_VERIFY_JSON_PART_ .same:allValuesEqualTo(12)";
		error = JsonHelper.validateByKeywords(criteria, json);
		Helper.assertTrue("errors caught", !error.isEmpty());
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
		
		 partialJson ="[\n" + 
		 		"   {\n" + 
		 		"      \"attachments\" : [\n" + 
		 		"         {\n" + 
		 		"            \"name\" : \"Installation Guide\",\n" + 
		 		"            \"url\" : \"https://docs.jboss.org/author/display/WFLY8/Admin+Guide\"\n" + 
		 		"         }\n" + 
		 		"      ],\n" + 
		 		"      \"assetId\" : \"Asset_test\",\n" + 
		 		"      \"name\" : \"BIGTRACK42\",\n" + 
		 		"      \"assetModel\" : {\n" + 
		 		"         \"assetModelId\": \"TESTEGISTEVE\",\n" + 
		 		"                            \"name\": \"FUSE\",\n" + 
		 		"                            \"description\": \"FUSE_Model\",\n" + 
		 		"                            \"status\": \"Active\"\n" + 
		 		"      },\n" + 
		 		"      \"description\" : \"Test Equipment\",\n" + 
		 		"      \"status\" : \"Active\"\n" + 
		 		"   }\n" + 
		 		"]";

			criteria = "_VERIFY_JSON_PART_ .*[?(@.assetId=~ /.*Asset_test/i)]:jsonbody("+partialJson+")";
			error = JsonHelper.validateByKeywords(criteria, partialJson);
			Helper.assertTrue("errors caught", error.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_jsonbody_2() {
		TestLog.When("I verify a valid json against correct value");
		
		String partialJson ="[\n" + 
		 		"   {\n" + 
		 		"      \"attachments\" : [\n" + 
		 		"         {\n" + 
		 		"            \"name\" : \"Installation Guide\",\n" + 
		 		"            \"url\" : \"https://docs.jboss.org/author/display/WFLY8/Admin+Guide\"\n" + 
		 		"         }\n" + 
		 		"      ],\n" + 
		 		"      \"assetId\" : \"Asset_abcd\",\n" + 
		 		"      \"name\" : \"BIGTRACK42\",\n" + 
		 		"      \"assetModel\" : {\n" + 
		 		"         \"assetModelId\": \"TESTEGISTEVE\",\n" + 
		 		"                            \"name\": \"FUSE\",\n" + 
		 		"                            \"description\": \"FUSE_Model\",\n" + 
		 		"                            \"status\": \"Active\"\n" + 
		 		"      },\n" + 
		 		"      \"description\" : \"Test Equipment\",\n" + 
		 		"      \"status\" : \"Active\"\n" + 
		 		"   }\n" + 
		 		"]";

			String criteria = "_VERIFY_JSON_PART_ .*[?(@.assetId=~ /.*Asset_abcd/i)]:jsonbody("+partialJson+")";
			List<String> error = JsonHelper.validateByKeywords(criteria, partialJson);
			Helper.assertTrue("errors caught", error.isEmpty());
			
			String errorValues = DataHelper.validateExpectedCommand("jsonbody", "", partialJson, "");
			Helper.assertTrue("errors not caught", !errorValues.isEmpty());
	}
	
	@Test()
	public void validateByKeywords_jsonbody_invalidJson() {
		TestLog.When("I verify a valid json against correct value");
		
		String partialJson ="[\n" + 
		 		"   {\n" + 
		 		"      \"attachments\" : [\n" + 
		 		"         {\n" + 
		 		"            \"name\" : \"Installation Guide\",\n" + 
		 		"            \"url\" : \"https://docs.jboss.org/author/display/WFLY8/Admin+Guide\"\n" + 
		 		"         }\n" + 
		 		"      ],\n" + 
		 		"      \"assetId\" : \"Asset_test4\",\n" + 
		 		"      \"name\" : \"BIGTRACK42\",\n" + 
		 		"      \"assetModel\" : {\n" + 
		 		"         \"assetModelId\": \"TESTEGISTEVE\",\n" + 
		 		"                            \"name\": \"FUSE\",\n" + 
		 		"                            \"description\": \"FUSE_Model\",\n" + 
		 		"                            \"status\": \"Active\"\n" + 
		 		"      },\n" + 
		 		"      \"description\" : \"Test Equipment\",\n" + 
		 		"      \"status\" : \"Active\"\n" + 
		 		"   }\n" + 
		 		"]";

			String criteria = "_VERIFY_JSON_PART_ .*[?(@.assetId=~ /.*Asset_test4/i)]:jsonbody(invalid json)";
			List<String> error = JsonHelper.validateByKeywords(criteria, partialJson);
			Helper.assertTrue("errors caught", !error.isEmpty());
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
		
		criteria = "_VERIFY_JSON_PART_ .book[?(@.author =~ /.*REES/i)].author2:isEmpty";
		error = JsonHelper.validateByKeywords(criteria, jsonBookStore);
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
	
	@Test(expectedExceptions = { AssertionError.class } , description = "fail on illigal escape character")
	public void validateByKeywords_response_illegal_escape_key() {
		String response = "{\"partyBaseUri\": \"www.google\\.ca\", \"locationBaseUri\": \"www.yahoo.com\", \"csv\": \"Adrian Young,Adrian.Young@abb.com,\\\"-123.113918, 49.261100\\\",\\\"-123.113918, 49.261100\\\",Vancouver City Hall\\nAudrey May,Audrey.May@abb.com,\\\"-123.130507, 49.299778\\\",\\\"-123.130507, 49.299778\\\",Vancouver Aquarium\\nAustin Hughes,Austin.Hughes@abb.com,\\\"-123.179996, 49.194356\\\",\\\"-123.179996, 49.194356\\\",Vancouver Intl Airport\\nBenjamin Wilson,Benjamin.Wilson@abb.com,\\\"-123.118136, 49.282487\\\",\\\"-123.118136, 49.282487\\\",Vancouver City Center\" }";
		JsonHelper.getJsonValue(response, ".partyBaseUri");
	}
	
	@Test(expectedExceptions = { AssertionError.class } , description = "fail on invalid escape character")
	public void validateByKeywords_response_invalid_escape_key() {
		Config.putValue(JsonHelper.failOnEscapeChars, true);
		
		String response = "{\"partyBaseUri\": \"www.google.ca\", \"locationBaseUri\": \"www.yahoo.com\", \"csv\": \"Adrian Young,Adrian.Young@abb.com,\\\"-123.113918, 49.261100\\\",\\\"-123.113918, 49.261100\\\",Vancouver City Hall\\nAudrey May,Audrey.May@abb.com,\\\"-123.130507, 49.299778\\\",\\\"-123.130507, 49.299778\\\",Vancouver Aquarium\\nAustin Hughes,Austin.Hughes@abb.com,\\\"-123.179996, 49.194356\\\",\\\"-123.179996, 49.194356\\\",Vancouver Intl Airport\\nBenjamin Wilson,Benjamin.Wilson@abb.com,\\\"-123.118136, 49.282487\\\",\\\"-123.118136, 49.282487\\\",Vancouver City Center\" }";
		JsonHelper.getJsonValue(response, ".partyBaseUri");
	}
	
	@Test(description = "fail on back slash in response")
	public void containsEscapeChar_valid() {
		String response = "test\\\"";
		boolean containsEscapeChar = JsonHelper.containsEscapeChar(response);
		Helper.assertTrue("escape char not found", containsEscapeChar);
		response = "test\\b";
		containsEscapeChar = JsonHelper.containsEscapeChar(response);
		Helper.assertTrue("escape char not found", containsEscapeChar);
		response = "test\\n";
		containsEscapeChar = JsonHelper.containsEscapeChar(response);
		Helper.assertTrue("escape char not found", containsEscapeChar);
		response = "test\\r";
		containsEscapeChar = JsonHelper.containsEscapeChar(response);
		Helper.assertTrue("escape char not found", containsEscapeChar);
		response = "test\\f";
		containsEscapeChar = JsonHelper.containsEscapeChar(response);
		Helper.assertTrue("escape char not found", containsEscapeChar);
		response = "test\\'";
		containsEscapeChar = JsonHelper.containsEscapeChar(response);
		Helper.assertTrue("escape char not found", containsEscapeChar);
		response = "test\\\\";
		containsEscapeChar = JsonHelper.containsEscapeChar(response);
		Helper.assertTrue("escape char not found", containsEscapeChar);
		
		response = "test\\\\\\n";
		containsEscapeChar = JsonHelper.containsEscapeChar(response);
		Helper.assertTrue("escape char not found", containsEscapeChar);
	}
}