package main.main_ios;

import core.driver.Capabilities;
import core.driver.DriverObject;
import core.driver.DriverObject.WebDriverType;
import main.main_ios.Panels.LaunchPanel;
import main.main_ios.Panels.SearchPanel;

public class FocusIos {

	// app info
	public static String APP = "FirefoxFocus.app";
	public static String VERSION = "11.1";
	public static String DEVICE = "iPhone 8";
	
	// panel list
	public LaunchPanel launch = new LaunchPanel(this);
	public SearchPanel search = new SearchPanel(this);


	/**
	 * sets url and  browser capabilities
	 * URL from maven or properties file will override the current url
	 * @return driver object
	 */
	public DriverObject getDriver() {
		Capabilities capability = new Capabilities()
				.withDefaultIosCapability()
				.withApp(APP)
				.withPlatformVersion(VERSION)
				.withDeviceName(DEVICE);
		
		return new DriverObject().withDriverType(WebDriverType.IOS_DRIVER)
				.withCapabilities(capability.getCapability());
	}	
}