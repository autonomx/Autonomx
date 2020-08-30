package test.module.framework.tests.functional.service;


import org.testng.annotations.Test;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.JsonHelper;
import core.apiCore.interfaces.RestApiInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.objects.ServiceObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class JsonTemplateTest extends TestBase {
	
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
	public void getRequestBodyFromJsonTemplate_requestbody_update() {
		Config.putValue("quizItem", "quiz2");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Quiz.json")
				.withRequestBody("_UPDATE_REQUEST_ quiz.sport.q1.options:2:value_<@quizItem>");
		
		String updatedJson = DataHelper.getRequestBodyIncludingTemplate(serviceObject);


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
	public void restapi_create_user_template() throws Exception {	
		
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
					.withTemplateFile("User.json")
					.withRequestBody("username:zzz_update<@_RAND16>;email:testUpdate1+<@_TIME_STRING_16>@gmail.com;")
					.withRespCodeExp("200")
					.withExpectedResponse("_VERIFY.JSON.PART_ email:1:equalTo(testUpdate1+<@_TIME_STRING_16>@gmail.com);");
			
			 RestApiInterface.RestfullApiInterface(serviceObject);
	}
	
	@Test()
	public void getRequestBodyFromJsonTemplate_data_file() {
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Car.json")
				.withRequestBody("DataFile:Car:ninja");
		
		String updatedJson = DataHelper.getRequestBodyIncludingTemplate(serviceObject);

		String updatedValue = JsonHelper.getJsonValue(updatedJson, ".type");
		Helper.assertEquals("motorcycle", updatedValue);
		
		updatedValue = JsonHelper.getJsonValue(updatedJson, ".year");
		Helper.assertEquals("2018", updatedValue);
		
		updatedValue = JsonHelper.getJsonValue(updatedJson, ".model");
		Helper.assertEquals("null", updatedValue);
	}
}