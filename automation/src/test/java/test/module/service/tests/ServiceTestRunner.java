package test.module.service.tests;


import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import serviceManager.ServiceRunner;
import test.module.service.TestBase;

public class ServiceTestRunner extends TestBase {

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() throws Exception {
	}

	@Test(dataProvider = "parallelRun", dataProviderClass = TestDataProvider.class, threadPoolSize = 1, invocationCount = 1)
	public void serviceRunner(Object objects) throws Exception {
		
		ServiceRunner.TestRunner(objects);
	}
}