package modules;

import modules.main_android.SelenoidAndroid;
import modules.main_ios.EurekaIos;
import modules.main_rest.StrapiRest;
import modules.main_win.winApp;
import modules.webApp.StrapiPanel;

/**
 * Manages all existing apps Abstract driver refers to this manager to call
 * methods in different apps
 */
public class AppManager {

	public StrapiPanel strapi = new StrapiPanel();
	public EurekaIos eureka = new EurekaIos();
	public SelenoidAndroid selenoid = new SelenoidAndroid();
	public StrapiRest rest = new StrapiRest();
	public winApp win = new winApp();

}