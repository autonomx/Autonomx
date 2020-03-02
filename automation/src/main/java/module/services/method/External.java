package module.services.method;

import core.helpers.Helper;
import core.support.configReader.Config;

public class External {
	
	
	/**
	 * supports primitive and object data types
	 * get and store values in config
	 * values from config can be used in test file using <@variable> syntax
	 * @param param1
	 * @param param2
	 * @param param3
	 */
	public static void testMethod(String param1, int param2, Object param3) {
		
		// values in Config are available by other tests in csv file through using <@key1> annotation
		// we can place string, int, boolean and objects in Config
		Config.putValue("key1",param1);	
		Config.putValue("key2",param2);	
		Config.putValue("key3",true);
		Config.putValue("key4", param3);

		// getting the values from Config through code
		String key = Config.getValue("key1");
		Helper.assertEquals(param1, key);
		
		int intkey = Config.getIntValue("key2");
		Helper.assertEquals(param2, intkey);
		
		boolean boolkey = Config.getBooleanValue("key3");
		Helper.assertEquals(true, boolkey);
		
		Object objectkey = Config.getObjectValue("key4");
		Helper.assertTrue("objects did not match", objectkey.equals(param3));
	}
	
	public static void testMethod() {
		Config.putValue("key1",3);
		Config.putValue("key2",true);
	}
}
