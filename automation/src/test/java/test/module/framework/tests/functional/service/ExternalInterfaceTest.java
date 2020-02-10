package test.module.framework.tests.functional.service;


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
	public void externalInterface_valid() throws Exception {	
		
		ServiceObject serviceObject = new ServiceObject()
				.withMethod("METHOD:External.testMethod");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
		String value = Config.getValue("key1");
		Helper.assertEquals("test123", value);
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
				.withMethod("METHOD:External2");
				
		ExternalInterface.ExternalInterfaceRunner(serviceObject);
	}
}