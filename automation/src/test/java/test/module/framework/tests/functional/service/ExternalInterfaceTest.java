package test.module.framework.tests.functional.service;


import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.interfaces.ExternalInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.objects.ServiceObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class ExternalInterfaceTest extends TestBase {
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
	}
	
	@Test(description = "")
	public void externalInterface_valid_no_parameters() throws Exception {	
		
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testMethod");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
		int value = Config.getIntValue("key1");
		Helper.assertEquals(3, value);
	}
	
	@Test(description = "")
	public void externalInterface_valid_with_parameters() throws Exception {	
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("keymap1", "val3");
		Config.putValue("keymap", map);

        
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testMethod")
				.withRequestBody("param1:value1; param2:3; param3:<@keymap>");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
		String value = Config.getValue("key1");
		Helper.assertEquals("value1", value);
	}
	
	@Test(description = "")
	public void externalInterface_valid_with_empty_parameter() throws Exception {	
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("keymap1", "val3");
		Config.putValue("keymap", map);

        
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testMethod")
				.withRequestBody("param1:value1; param2:3; param3:");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
		String value = Config.getValue("key1");
		Helper.assertEquals("value1", value);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void externalInterface_invalid_format() throws Exception {	
		
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void externalInterface_invalid_file() throws Exception {	
		
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External:testInvalidFile2");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
	}
}