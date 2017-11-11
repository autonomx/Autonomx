package main;

import main.main_android.FocusAndroid;
import main.main_ios.FocusIos;
import main.main_web.FocusWeb;



/**
 * Manages all existing apps Abstract driver refers to this manager to call
 * methods in different apps
 */
public class AppManager {


	public FocusWeb focus_web = new FocusWeb();
	public FocusIos focus_ios = new FocusIos();
	public FocusAndroid focus_android = new FocusAndroid();
}