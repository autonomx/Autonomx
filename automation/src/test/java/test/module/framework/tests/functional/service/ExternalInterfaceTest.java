package test.module.framework.tests.functional.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.ServiceManager;
import core.apiCore.interfaces.ExternalInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;

/**
 * @author ehsan matean
 *
 */
public class ExternalInterfaceTest {
	
	
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
	public void externalInterface_valid_overload_same_parameter_count() throws Exception {	
		
		List<String> list = new ArrayList<String>();
		Config.putValue("keylist", list);

        
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testMethod")
				.withRequestBody("param1:value1; param2:<@keylist>");
				
		 ExternalInterface.ExternalInterfaceRunner(serviceObject);
		String value = Config.getValue("key1");
		Helper.assertEquals("value1", value);
	}
	
	@Test(description = "")
	public void externalInterface_valid_with_parameters_without_parameter_names() throws Exception {	
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("keymap1", "val3");
		Config.putValue("keymap", map);

        
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testMethod")
				.withRequestBody("value1; 3; <@keymap>");
				
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
		String value = Config.getValue("key4");
		Helper.assertEquals("", value);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void externalInterface_invalid_wrong_parameter_name() throws Exception {	
		
		List<String> list = new ArrayList<String>();
		Config.putValue("keylist", list);

        
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testMethod")
				.withRequestBody("param4:value1; param5:3; param2:<@keylist>");
				
		 ExternalInterface.ExternalInterfaceRunner(serviceObject);
		String value = Config.getValue("key1");
		Helper.assertEquals("value1", value);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void externalInterface_valid_with_parameter_type() throws Exception {	
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("keymap1", "val3");
		Config.putValue("keymap", map);

        
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testMethod")
				.withRequestBody("param1:value1; param2:notNumber; param3:<@keymap>");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
		String value = Config.getValue("key1");
		Helper.assertEquals("value1", value);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void externalInterface_valid_with_wrong_parameter_count() throws Exception {	
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("keymap1", "val3");
		Config.putValue("keymap", map);

        
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testMethod")
				.withRequestBody("param1:1; param2:2; param3:3; param4:4");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void externalInterface_invalid_format() throws Exception {	
		
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void externalInterface_invalid_filename() throws Exception {	
		
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testInvalidFile2");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void externalInterface_invalid_file() throws Exception {	
		
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External:testInvalidFile2");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
	}
	
	@Test(description = "will run the generated TestInterface interface" )
	public void ServiceManager_runCombinedRunner() throws Exception {
		ServiceObject service = new ServiceObject().withInterfaceType("TestInterface");
		TestObject.getTestInfo().activeServiceObject = service;
		ServiceManager.runCombinedInterface();
	}
}