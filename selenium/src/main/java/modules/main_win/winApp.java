package modules.main_win;


import core.support.objects.DriverObject;
import core.uiCore.driverProperties.driverType.DriverType;
import core.uiCore.driverProperties.capabilities.WinAppCapabilities;
import modules.main_win.Panels.calculatePanel;


public class winApp {

	// app info
	
	public static String APP = "winApp";
	
	// panel list
	public calculatePanel cal = new calculatePanel(this);


	/**
	 * URL from maven or properties file will override the current values
	 * @return driver object
	 */
	public DriverObject getDriver() {
		WinAppCapabilities capability = new WinAppCapabilities().withWinAppdCapability();
		
		return new DriverObject()
				.withApp(APP)
				.withDriverType(DriverType.WINAPP_DRIVER)
				.withCapabilities(capability.getCapability());	
	}	
}