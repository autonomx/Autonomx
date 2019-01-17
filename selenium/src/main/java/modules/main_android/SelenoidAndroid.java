package modules.main_android;


import core.support.objects.DriverObject;
import core.uiCore.driverProperties.driverType.DriverType;
import core.uiCore.driverProperties.capabilities.AndroidCapability;
import modules.main_android.Panels.MainPanel;
import modules.main_android.Panels.RegistrationPanel;

public class SelenoidAndroid {

	// panel list
	public RegistrationPanel register = new RegistrationPanel(this);
	public MainPanel main = new MainPanel(this);

	/**
	 * sets url and browser capabilities URL from maven or properties file will
	 * override the current url
	 *
	 * @return driver object
	 */
	public synchronized DriverObject getDriver() {
		AndroidCapability capability = new AndroidCapability().withDevice1().withAndroidCapability();
		return new DriverObject().withDriverType(DriverType.ANDROID_DRIVER).withCapabilities(capability.getCapability());
	}

}
