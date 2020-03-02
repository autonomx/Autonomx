package test.module.framework.tests.functional.service;


import java.util.ArrayList;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.interfaces.Authentication;
import core.apiCore.interfaces.RestApiInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.objects.ServiceObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
@SuppressWarnings("unchecked")
public class AuthenticationInterfaceTest extends TestBase {
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
	}
	
	@Test()
	public void verifyBasicAuthentication() {	
		ServiceObject serviceObject = new ServiceObject()
				.withOption("BASIC").withRequestBody("username:admin, password:pass1").withOutputParams("auth:<$basic>");
		
		Authentication.authenticator(serviceObject);
		
		ArrayList<String> basicRequest = (ArrayList<String>) Config.getObjectValue("basic");
		Helper.assertEquals(2, basicRequest.size());
		Helper.assertEquals("admin", basicRequest.get(0));
		Helper.assertEquals("pass1", basicRequest.get(1));
		
		serviceObject = new ServiceObject()
				.withMethod("GET")
				.withRequestHeaders("BASIC:<@basic>");
		
		RestApiInterface.RestfullApiInterface(serviceObject);
	}
	
	@Test()
	public void verifyNTLMAuthentication() {	
		ServiceObject serviceObject = new ServiceObject()
				.withOption("NTLM")
				.withRequestBody("username:admin, password:pass1, workstation:work1,domain:domain1")
				.withOutputParams("auth:<$tmnl>");
		
		Authentication.authenticator(serviceObject);
		
		ArrayList<String> tmnlRequest = (ArrayList<String>) Config.getObjectValue("tmnl");
		Helper.assertEquals(4, tmnlRequest.size());
		Helper.assertEquals("admin", tmnlRequest.get(0));
		Helper.assertEquals("pass1", tmnlRequest.get(1));
		Helper.assertEquals("work1", tmnlRequest.get(2));
		Helper.assertEquals("domain1", tmnlRequest.get(3));
		
		serviceObject = new ServiceObject()
				.withMethod("GET")
				.withRequestHeaders("NTLM:<@tmnl>");
		
		RestApiInterface.RestfullApiInterface(serviceObject);
	}
	
	@Test()
	public void verifyNTLMAuthentication_no_request() {	
		ServiceObject serviceObject = new ServiceObject()
				.withOption("NTLM")
				.withOutputParams("auth:<$tmnl>");
		
		Authentication.authenticator(serviceObject);
		
		ArrayList<String> tmnlRequest = (ArrayList<String>) Config.getObjectValue("tmnl");
		Helper.assertEquals(4, tmnlRequest.size());
		Helper.assertEquals(null, tmnlRequest.get(0));
		Helper.assertEquals(null, tmnlRequest.get(1));
		Helper.assertEquals(null, tmnlRequest.get(2));
		Helper.assertEquals(null, tmnlRequest.get(3));
	}

}