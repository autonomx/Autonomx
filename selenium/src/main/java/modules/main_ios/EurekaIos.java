package modules.main_ios;

import core.support.objects.DriverObject;
import core.uiCore.driverProperties.driverType.DriverType;
import core.uiCore.driverProperties.capabilities.IosCapability;
import modules.main_ios.Panels.MainPanel;
import modules.main_ios.Panels.PlainTableViewPanel;

public class EurekaIos {

	// panel list
	public PlainTableViewPanel plain = new PlainTableViewPanel(this);
	public MainPanel main = new MainPanel(this);

	/**
	 * sets url and browser capabilities URL from maven or properties file will
	 * override the current url
	 *
	 * @return driver object
	 */
	public synchronized DriverObject getDriver() {
		IosCapability capability = new IosCapability().withDevice1().withIosCapability();
		return new DriverObject().withDriverType(DriverType.IOS_DRIVER).withCapabilities(capability.getCapability());
	}

}
