package main.main_android;

import core.driver.Capabilities;
import core.driver.DriverObject;
import core.driver.DriverObject.WebDriverType;
import main.main_android.Panels.LaunchPanel;
import main.main_android.Panels.SearchPanel;

public class FocusAndroid {

	// app info
	public static String APP = "Focus-2.3.apk";
	public static String AVD = "Pixel_XL_API_25";
	
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
				.withDefaultAndroidCapability()
				.withApp(APP)
				.withAvd(AVD);
		
		return new DriverObject().withDriverType(WebDriverType.ANDROID_DRIVER)
				.withCapabilities(capability.getCapability());
	}	
}