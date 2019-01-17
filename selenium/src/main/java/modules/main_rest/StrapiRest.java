package modules.main_rest;

import core.support.objects.DriverObject;
import core.uiCore.driverProperties.driverType.DriverType;
import modules.main_rest.Panels.LoginPanel;
import modules.main_rest.Panels.UserPanel;

public class StrapiRest {

	// app info

	public static String APP = "gaiaApi";

	// panel list
	public LoginPanel login = new LoginPanel(this);
	public UserPanel user = new UserPanel(this);

	/**
	 * sets url and browser capabilities URL from maven or properties file will
	 * override the current values
	 * 
	 * @return driver object
	 */
	public DriverObject getDriver() {
		return new DriverObject().withDriverType(DriverType.API).withApp(APP);
	}
}