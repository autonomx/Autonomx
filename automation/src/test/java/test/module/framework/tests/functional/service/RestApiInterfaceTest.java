package test.module.framework.tests.functional.service;


import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.helpers.DataHelper;
import core.apiCore.interfaces.RestApiInterface;
import core.helpers.Helper;
import core.helpers.StopWatchHelper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.ServiceObject;
import data.Data;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import module.common.data.CommonUser;
import serviceManager.Service;
import serviceManager.ServiceRunner;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class RestApiInterfaceTest extends TestBase {
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
	}
	
	@Test()
	public void verifyJsonValidator() {	
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("{\n" + 
						"	\"provider\": \"local\",\n" + 
						"	\"role\": {\n" + 
						"		\"id\": 2,\n" + 
						"		\"name\": \"Authenticated\",\n" + 
						"		\"description\": \"Default role given to authenticated user.\",\n" + 
						"		\"type\": \"authenticated\"\n" + 
						"	}\n" + 
						"}");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
		
		TestLog.And("I verify response matches expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());

	}
	
	@Test()
	public void verifyJsonValidator_invalid_requestValue() {	
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("{\n" + 
						"	\"id\": 301,\n" + 
						"	\"username\": \"zzz_autoUserv61w2;email\",\n" + 
						"	\"role\": {\n" + 
						"		\"id\": 3,\n" + 
						"		\"name\": \"Authenticated2\",\n" + 
						"		\"description\": \"Default role given to authenticated user.\",\n" + 
						"		\"type\": \"authenticated\"\n" + 
						"	}\n" + 
						"}");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_equalTo() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:equalTo(local); provider:equalTo(local); provider:equalTo(<@localVariable>) ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_invalid_expectation1() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local2");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:equalTo(local2); provider:equalTo(local2); provider:equalTo(<@localVariable>) ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
	Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test( )
	public void verifyJson_by_keyword_invalid_expectation2() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local2");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:equalTo(local2)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_invalid_expectation() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local2");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:equalTo(<@localVariable>) ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void verifyJson_by_keyword_invalid_expectation_format() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:local");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_hasItems() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:hasItems(local); provider:hasItems(local); provider:hasItems(<@localVariable>) ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_hasItems_invalid() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:hasItems(local2)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_contains() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:contains(loc); provider:contains(local); provider:contains(<@localVariable>) ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());
	}
	
	@Test( )
	public void verifyJson_by_keyword_contains_invalid() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:contains(local2)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_containsInAnyOrder() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:contains(loc); provider:contains(local); provider:contains(<@localVariable>) ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_containsInAnyOrder_invalid() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:contains(local2)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_nodeSizeGreaterThan() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:nodeSizeGreaterThan(0)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_nodeSizeGreaterThan_invalid() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:nodeSizeGreaterThan(1)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_nodeSizeExact() {	
		
		TestLog.When("I set keyword expected response for service object");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:nodeSizeExact(1)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_nodeSizeExact_invalid() {	
		
		TestLog.When("I set keyword expected response for service object");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:nodeSizeExact(2)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_sequence() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:sequence(local); provider:sequence(local); provider:sequence(<@localVariable>) ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_invalid_sequence() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local2");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:sequence(local2)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_isNotEmpty() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:isNotEmpty");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_invalid_isNotEmpty() {	
		
		TestLog.When("I set keyword expected response for service object");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ confirmed:1:isEmpty");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_isEmpty() {	
		
		TestLog.When("I set keyword expected response for service object");
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ confirmed:1:isEmpty");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());

	}
	
	@Test()
	public void verifyJson_by_keyword_invalid_isEmpty() {	
		
		TestLog.When("I set keyword expected response for service object");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ provider:1:isEmpty");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	@Test()
	public void verifyJson_by_keyword_jsonString() {	
		
		TestLog.When("I set keyword expected response for service object");
		
		String expectedJson =
				"[{\n" + 
				"   \"name\" : \"Authenticated\",\n" + 
				"   \"description\" : \"Default role given to authenticated user.\",\n" + 
				"   \"type\" : \"authenticated\"\n" + 
				"}]";
		
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ role:jsonbody("+ expectedJson +"); provider:sequence(local); ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void verifyJson_by_keyword_jsonString_invalid_expected_json() {	
		
		TestLog.When("I set keyword expected response for service object");
		
		String expectedJson =
				"{\n" + 
				"   \"name\" : \"Authenticated\",\n" + 
				"   \"description\" : \"Default role given to authenticated user.\",\n" + 
				"   \"type\" : \"authenticated\"\n" + 
				"}";
		
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ role:jsonbody("+ expectedJson +"); provider:sequence(local); ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_jsonString_invalid() {	
		
		TestLog.When("I set keyword expected response for service object");
		
		String expectedJson =
				"[{\n" + 
				"   \"name\" : \"Authenticated2\",\n" + 
				"   \"description\" : \"Default role given to authenticated user.\",\n" + 
				"   \"type\" : \"authenticated2\"\n" + 
				"}]";
		
		Config.putValue("localVariable", "local");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ role:jsonbody("+ expectedJson +"); provider:sequence(local); ");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught", !errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_IntegerGreaterThan() {	
		
		TestLog.When("I set keyword expected response for service object");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ id:1:integerGreaterThan(1)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_IntegerGreaterThan_invalid() {	
		
		TestLog.When("I set keyword expected response for service object");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ id:1:integerGreaterThan(12356789)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	@Test()
	public void verifyJson_by_keyword_IntegerLessThan() {	
		
		TestLog.When("I set keyword expected response for service object");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ id:1:integerLessThan(123456789)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), errors.isEmpty());
	}
	
	@Test()
	public void verifyJson_by_keyword_IntegerLessThan_invalid() {	
		
		TestLog.When("I set keyword expected response for service object");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ id:1:integerLessThan(1)");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> responses = new ArrayList<String>();
		responses.add(response.getBody().asString());
		List<String> errors = DataHelper.validateExpectedValues(responses, serviceObject.getExpectedResponse());
		Helper.assertTrue("errors not caught: " + Arrays.toString(errors.toArray()), !errors.isEmpty());
	}
	
	@Test()
	public void queryParameterSplitTest() {
		String param = "key=val&key2=val2";
		String[] params = param.split("(&&)|(&)");
		Helper.assertEquals(2, params.length);
		
		param = "key=val&&key2=val2";
		params = param.split("(&&)|(&)");
		Helper.assertEquals(2, params.length);
		
		param = "key=val&&key2=val2&key3=val3";
		params = param.split("(&&)|(&)");
		Helper.assertEquals(3, params.length);
	}
	

	@Test()
	public void evaluateQueryParameters_valid() {
		ServiceObject service = new ServiceObject().withUriPath("https://de-qa1b.digital.enterprisesoftware.abb/v1/locationAssociations/findByEntity?entityUUID=&entityType=WorkOrder");
		// set request
		RequestSpecification request = given();
		
		RestApiInterface.evaluateQueryParameters(service, request);
	}
	
	@Test()
	public void verifyTestMultipleRuns() throws Exception {	
		
		ServiceObject userAPI = Service.create()
				.withInterfaceType("RESTfulAPI")
				.withUriPath("http://www.google.ca")
				.withOption("RUN_COUNT:2")
				.withMethod("GET")
				.withRespCodeExp("200");
			
		ServiceRunner.runInterface(userAPI);	
	//	Helper.assertEquals(2, Config.getIntValue(ServiceManager.SERVICE_RUN_CURRENT_COUNT));
	}
	
	@Test(expectedExceptions = { AssertionError.class })
	public void verifyTestMultipleRuns_failingTests() throws Exception {	
		
		ServiceObject userAPI = Service.create()
				.withInterfaceType("RESTfulAPI")
				.withUriPath("http://www.google.ca")
				.withOption("RUN_COUNT:2")
				.withMethod("GET")
				.withRespCodeExp("400");
			
		ServiceRunner.runInterface(userAPI);	
	//	Helper.assertEquals(2, Config.getIntValue(ServiceManager.SERVICE_RUN_CURRENT_COUNT));
	}
	
	@Test()
	public void resetapi_request_timeout_exception() throws Exception {
		StopWatchHelper watch = StopWatchHelper.start();
		long passedTimeInSeconds = 0;
		Config.putValue(RestApiInterface.API_RESPONSE_TIMEOUT_SECONDS, "2");
		
		ServiceObject userAPI = Service.create()
				.withInterfaceType("RESTfulAPI")
				.withUriPath("http://example.com:81")
				.withMethod("GET")
				.withRespCodeExp("200");
		
		try {
			ServiceRunner.runInterface(userAPI);
		}catch(AssertionError e) {
			e.getMessage();
			passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
			Helper.assertTrue("wrong error returned: " + e.getMessage(), e.getMessage().contains("no response returned"));
			Helper.assertTrue("timeout was less than 2 seconds: " + passedTimeInSeconds , passedTimeInSeconds >= 2);
			Helper.assertTrue("timeout was less than 2 seconds: " + passedTimeInSeconds, passedTimeInSeconds < 10);
		}	
	}	
	
	@Test
	public void restapi_wait_for_response() throws Exception {	
		
		long passedTimeInSeconds = 0;
		
		String expected = "{\n" + 
				"  \"userId\": 2,\n" + 
				"  \"id\": 1,\n" + 
				"  \"title\": \"delectus aut autem\",\n" + 
				"  \"completed\": false\n" + 
				"}";
		String url = "https://jsonplaceholder.typicode.com/todos/1";
		ServiceObject serviceObject = new ServiceObject()
				.withUriPath(url)
				.withMethod("GET")
				.withRequestHeaders("Authorization:Bearer1")
				.withRespCodeExp("200")
				.withOption("WAIT_FOR_RESPONSE:2")
				.withExpectedResponse(expected);
		
	StopWatchHelper watch = StopWatchHelper.start();
	 try {
		 RestApiInterface.RestfullApiInterface(serviceObject);
	 }catch(AssertionError e) {
			passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
			Helper.assertTrue("timeout was less than 2 seconds: " + passedTimeInSeconds, passedTimeInSeconds >= 2);
			Helper.assertTrue("timeout was more than 7 seconds: " + passedTimeInSeconds, passedTimeInSeconds <= 10);
	 }
	}
	
	@Test
	public void restapi_pagination_valid() throws Exception {	
		
		
		String expected = "_VERIFY.JSON.PART_ .*[?(@.id =~ /.*121/i)].id:hasItems(121)";
		String url = "http://jsonplaceholder.typicode.com/todos?page=<@PAGINATION>";
		ServiceObject serviceObject = new ServiceObject()
				.withUriPath(url)
				.withMethod("GET")
				.withRespCodeExp("200")
				.withOption("PAGINATION_FROM:0;PAGINATION_INCREMENT:1;PAGINATION_MAX_PAGES:3;PAGINATION_STOP_CRITERIA:.id")
				.withExpectedResponse(expected);
		
		 RestApiInterface.RestfullApiInterface(serviceObject);
	}
	
	@Test(expectedExceptions = { AssertionError.class }, description = "invalid expected value")
	public void restapi_pagination_invalid() throws Exception {	
		
		
		String expected = "_VERIFY.JSON.PART_ .*[?(@.id =~ /.*121/i)].id:hasItems(122)";
		String url = "http://jsonplaceholder.typicode.com/todos?page=<@PAGINATION>";
		ServiceObject serviceObject = new ServiceObject()
				.withUriPath(url)
				.withMethod("GET")
				.withRespCodeExp("200")
				.withOption("PAGINATION_FROM:0;PAGINATION_INCREMENT:1;PAGINATION_MAX_PAGES:3;PAGINATION_STOP_CRITERIA:.id")
				.withExpectedResponse(expected);
		
		 RestApiInterface.RestfullApiInterface(serviceObject);
	}
	
	@Test( description = "invalid expected value, wait for response")
	public void restapi_pagination_invalid_response_timeout() throws Exception {	
		
		StopWatchHelper watch = StopWatchHelper.start();
		
		String expected = "_VERIFY.JSON.PART_ .*[?(@.id =~ /.*121/i)].id:hasItems(122)";
		String url = "http://jsonplaceholder.typicode.com/todos?page=<@PAGINATION>";
		ServiceObject serviceObject = new ServiceObject()
				.withUriPath(url)
				.withMethod("GET")
				.withRespCodeExp("200")
				.withOption("WAIT_FOR_RESPONSE:2;PAGINATION_FROM:0;PAGINATION_INCREMENT:1;PAGINATION_MAX_PAGES:3;PAGINATION_STOP_CRITERIA:.id")
				.withExpectedResponse(expected);
		
		try {
		 RestApiInterface.RestfullApiInterface(serviceObject);
		}catch(AssertionError e) {
			e.getMessage();
		}
		 long passedTimeInSeconds = watch.time(TimeUnit.SECONDS);
		Helper.assertTrue("timout did not wait long enough: " + passedTimeInSeconds + " seconds", passedTimeInSeconds >= 2);
		Helper.assertTrue("timout waited too long: " + passedTimeInSeconds + " seconds", passedTimeInSeconds < 5);

	}
	
	@Test(expectedExceptions = { AssertionError.class }, description = "invalid criteria path")
	public void restapi_pagination_invalid_criteria() throws Exception {	
			
		String expected = "_VERIFY.JSON.PART_ .*[?(@.id =~ /.*121/i)].id:hasItems(121)";
		String url = "http://jsonplaceholder.typicode.com/todos?page=<@PAGINATION>";
		ServiceObject serviceObject = new ServiceObject()
				.withUriPath(url)
				.withMethod("GET")
				.withRespCodeExp("200")
				.withOption("PAGINATION_FROM:0;PAGINATION_INCREMENT:1;PAGINATION_MAX_PAGES:3;PAGINATION_STOP_CRITERIA:.id2")
				.withExpectedResponse(expected);
		
		 RestApiInterface.RestfullApiInterface(serviceObject);
	}
	
	@Test(description = "valid criteria path")
	public void restapi_pagination_valid_criteria_json_array() throws Exception {	
			
		String expected = "_VERIFY.JSON.PART_ .*[?(@.id =~ /.*121/i)].id:hasItems(121)";
		String url = "http://jsonplaceholder.typicode.com/todos?page=<@PAGINATION>";
		ServiceObject serviceObject = new ServiceObject()
				.withUriPath(url)
				.withMethod("GET")
				.withRespCodeExp("200")
				.withOption("PAGINATION_FROM:0;PAGINATION_INCREMENT:1;PAGINATION_MAX_PAGES:3;PAGINATION_STOP_CRITERIA:.*[?(@.id =~ /.*1/i)]")
				.withExpectedResponse(expected);
		
		 RestApiInterface.RestfullApiInterface(serviceObject);
	}
	
	@Test()
	public void restapi_create_user_update() throws Exception {	
		
		// create user
		String expected = "_VERIFY.JSON.PART_ .id:isNotEmpty;";
		ServiceObject serviceObject = new ServiceObject()
				.withUriPath("http://demo.autonomx.io/content-manager/explorer/user/?source=users-permissions")
				.withContentType("application/x-www-form-urlencoded")
				.withMethod("POST")
				.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>")
				.withRequestBody("username:zzz_test<@_RAND16>,\n" + 
						"email:testuser+<@_TIME_STRING_16>@gmail.com,\n" + 
						"password:password<@_TIME_STRING_16>,\n" + 
						"confirmed:true")
				.withRespCodeExp("201")
				.withOutputParams("id:<$userId>")
				.withExpectedResponse(expected);
		
		 RestApiInterface.RestfullApiInterface(serviceObject);
		 
		 // update user with template
		 serviceObject = new ServiceObject()
					.withUriPath("http://demo.autonomx.io/content-manager/explorer/user/<@userId>?source=users-permissions")
					.withContentType("application/json")
					.withMethod("PUT")
					.withRequestHeaders("Authorization: Bearer <@accessTokenAdmin>")
					.withRequestBody("{\"username\":\"zzz_update<@_RAND16>\",\n" + 
							"\"email\":\"testUpdate+<@_TIME_STRING_16>@gmail.com\",\n" + 
							"\"password\":\"password<@_TIME_STRING_16>\",\n" + 
							"\"confirmed\":true}")
					.withRespCodeExp("200")
					.withExpectedResponse("_VERIFY.JSON.PART_ email:1:equalTo(testUpdate+<@_TIME_STRING_16>@gmail.com);");
			
			 RestApiInterface.RestfullApiInterface(serviceObject);	 
	}
}