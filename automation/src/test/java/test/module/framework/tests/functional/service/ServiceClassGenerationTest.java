package test.module.framework.tests.functional.service;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import core.helpers.Helper;
import core.support.annotation.template.service.ServiceClass;
import core.support.configReader.Config;
import test.module.framework.TestBase;



/**
 * @author ehsan matean
 *
 */
public class ServiceClassGenerationTest extends TestBase {
	
	@BeforeClass
	public void beforeClass()  {
		
		// wait to combat too many requests
		Helper.waitForSeconds(5);
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/csvClass/");
	}
	
	@Test()
	public void verifyServiceClass() throws Exception {
		
		ServiceClass.writeServiceGenerationClass();

	}

	
}