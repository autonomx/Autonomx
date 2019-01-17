package modules.webApp;

import core.support.objects.DriverObject;
import core.uiCore.driverProperties.capabilities.WebCapability;
import modules.webApp.Panels.LoginPanel;
import modules.webApp.Panels.MainPanel;
import modules.webApp.Panels.SidePanelNavigation;
import modules.webApp.Panels.UserPanel;

public class StrapiPanel {

	// app info
	public static String APP = "strapi";
	public static String URL = "http://45.76.245.149:1337/admin/";

	// panel list
	public LoginPanel login = new LoginPanel(this);
	public SidePanelNavigation side = new SidePanelNavigation(this);
	public MainPanel main = new MainPanel(this);
	public UserPanel user = new UserPanel(this);

	/**
	 * sets url and browser capabilities URL from maven or properties file will
	 * override the current url
	 * 
	 * @return driver object
	 */
	public synchronized DriverObject getDriver() {
		WebCapability capability = new WebCapability().withBrowserCapability();
		
		return new DriverObject().withApp(APP).withDriverType(capability.getWebDriverType())
				.withBrowserType(capability.getBrowser()).withDriverVersion(capability.getDriverVersion())
				.withUrl(capability.getUrl(APP, URL))

				.withCapabilities(capability.getCapability());
	}
}