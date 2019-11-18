package test.module.framework.tests.functional.service;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.helpers.DataHelper;
import core.helpers.Helper;
import core.support.objects.ServiceObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class SqlInterfaceTest extends TestBase {
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
	}
	
	@Test()
	public void verifySqlStatement() {	
		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("SELECT * FROM PARTY.INDIVIDUAL WHERE ID='AAAAAAAA-AAAA-AAAA-AAAA-AAAA11111111'");
		
		// replace parameters for request body
		serviceObject.withRequestBody(DataHelper.replaceParameters(serviceObject.getRequestBody()));

		
		// get query
		String sql = serviceObject.getRequestBody();
		Helper.assertEquals("SELECT * FROM PARTY.INDIVIDUAL WHERE ID='AAAAAAAA-AAAA-AAAA-AAAA-AAAA11111111'", sql);

	}

	
	
}