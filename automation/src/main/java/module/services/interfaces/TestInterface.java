package module.services.interfaces;


import core.helpers.Helper;
import core.support.annotation.Service;
import core.support.objects.ServiceObject;

@Service
public class TestInterface {
	

	/**
	 * interface for restful API calls
	 * 
	 * @param apiObject
	 * @return
	 */
	public void testInterface(ServiceObject serviceObject) {

		if (serviceObject == null)
			Helper.assertFalse("serviceObject is null");	
	}
}
