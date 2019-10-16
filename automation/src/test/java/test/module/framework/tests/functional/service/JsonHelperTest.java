package test.module.framework.tests.functional.service;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.JsonHelper;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
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
			"		\"type\": \"authenticated\"\n" + 
			"	}\n" + 
			"}";
	
	@BeforeClass
	public void beforeClass()  {
	}
	
	@Test()
	public void verifyJsonString() {
		
		TestLog.When("I verify a valid json string");
		
		boolean isJson = JsonHelper.isJSONValid(jsonString, true);
		Helper.assertTrue("is not valid json", isJson);
		
		TestLog.When("I verify an invalid json string");
		String jsonStringInvalid = jsonString.replaceFirst("\\{", "[");
		isJson = JsonHelper.isJSONValid(jsonStringInvalid, true);
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
		JsonHelper.validateByJsonBody(criteria, jsonString);
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
				"	\"id\": 306,\n" + 
				"	\"username\": \"zzz_<@_TIME19>\",\n" + 
				"	\"email\": null,\n" + 
				"	\"role\": {\n" + 
				"		\"id\": 2\n" + 
				"	}\n" + 
				"}";
		String jsonString_updated = DataHelper.replaceParameters(jsonString);	
		String criteria_updated = DataHelper.replaceParameters(criteria);	

		String error =JsonHelper.validateByJsonBody(criteria_updated, jsonString_updated);
		Helper.assertTrue("errors not caught", !error.isEmpty());
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
		JsonHelper.configMapJsonKeyValues(response, "role.name:<$name>; provider:1:<@local>");
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
	public void getJsonValue_with_json_string() {
		
		TestLog.Then("I verify getting json value from path");
		String name = JsonHelper.getJsonValue(jsonString, "role.name");
		Helper.assertEquals("Authenticated", name);
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
	
	@Ignore
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
		
		xmlString = "<?xml version=\\\"1.0\\\" ?><test attrib=\\\"moretest\\\">Turn this to JSON</test>";
		
		TestLog.When("I verify getting json path value from xml");
		String cars = JsonHelper.getJsonValueFromXml(xmlString, "cars");
		Helper.assertEquals("Ford,BMW,Fiat", cars);
	}
}