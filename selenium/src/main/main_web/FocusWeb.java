package main.main_web;

import core.driver.BaseUrl;
import core.driver.Capabilities;
import core.driver.DriverObject;
import main.main_web.Panels.SearchPanel;

public class FocusWeb {

	// app info
	public static String APP = "google";
	public static String URL = "http://www.google.com";
	
	// panel list
	public SearchPanel search = new SearchPanel(this);


	/**
	 * sets url and  browser capabilities
	 * URL from maven or properties file will override the current url
	 * @return driver object
	 */
	public DriverObject getDriver() {
		Capabilities capability = new Capabilities().withDefaultBrowserCapability();
		
		return new DriverObject()
				.withApp(APP)
				.withUrl(BaseUrl.getUrl(APP, URL))
				.withCapabilities(capability.getCapability());
	}	
}