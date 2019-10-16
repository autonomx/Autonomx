package test.module.framework.tests.functional.service;


import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.interfaces.RestApiInterface;
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
	}
	
	@Test()
	public void verifyJson_by_keyword_invalid_isNotEmpty() {	
		
		TestLog.When("I set keyword expected response for service object");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("_VERIFY.JSON.PART_ confirmed:1:isNotEmpty");
		
		CommonUser user = Data.common.commonuser().withAdminLogin();

	    user = Data.common.commonuser().withDefaultUser();
		TestLog.And("I create user '" + user.username + "'");
		Response response = app.serviceUiIntegration.user.createUserUsingServiceObject(user);
			
		TestLog.And("I verify response does not match expected");
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
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
		RestApiInterface.validateExpectedValues(response, serviceObject);
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
		List<String> errors = RestApiInterface.validateExpectedValues(response, serviceObject);
		Helper.assertTrue("errors not caught", !errors.isEmpty());
	}
	
	
}