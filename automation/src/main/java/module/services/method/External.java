package module.services.method;

import core.support.configReader.Config;

public class External {
	
	
	public static void testMethod() {
		
		// values in Config are available by other tests in csv file through using <@key1> annotation
		Config.putValue("key1","test123");		
	}
}
