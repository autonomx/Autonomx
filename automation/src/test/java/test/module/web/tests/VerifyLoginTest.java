package test.module.web.tests;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.logger.TestLog;
import data.Data;
import data.webApp.User;
import data.webApp.UserInvalid;
import module.webApp.panel.LoginPanel;
import module.webApp.panel.MainPanel;
import test.module.web.TestBase;
/**
 * this test class demonstrates 3 data types: data object, csv data and data provider
 * @author ehsan matean
 *
 */
public class VerifyLoginTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		
//		Map<String, Object> prefs = new HashMap<String, Object>();
//		prefs.put("download.default_directory",  "C:\\Temp\\downloads");
//		prefs.put("intl.accept_languages",  "fr");
//
//		ChromeOptions options = new ChromeOptions();
//		options.setExperimentalOption("prefs", prefs);
//		ChromeDriver driver = new ChromeDriver(options);
//		driver.get("http://demo.autonomx.io/admin/");
		
//		Map<String, Object> prefs = new HashMap<String, Object>();
//		prefs.put("download.default_directory",  "C:\\Temp\\downloads");
//		prefs.put("intl.accept_languages",  "fr");
//
//		ChromeOptions options = new ChromeOptions();
//		options.setExperimentalOption("prefs", prefs);
//		DesiredCapabilities capabilities = new DesiredCapabilities();
//		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
//		
//		ChromeOptions chromeOptions = new ChromeOptions();
//		chromeOptions.merge(capabilities);
//		
//		ChromeDriver driver = new ChromeDriver(capabilities);
//		driver.get("http://demo.autonomx.io/admin/");
		
		setupWebDriver(app.webApp.getHybridDriver());
	}
	
	@Test()
	public void verifyAdminUserWithCsvData() {
		
	    User user = Data.webApp.user().admin();
		TestLog.When("I login with user " + user.getUsername());
		app.webApp.login.loginWithCsvData(user);
				
		TestLog.Then("I verify admin logo is displayed");
		Helper.verifyElementIsDisplayed(MainPanel.ADMIN_LOGO);
		
		TestLog.When("I logout");
		app.webApp.main.logout();

		TestLog.Then("I should see the login panel");
		Helper.verifyElementIsDisplayed(LoginPanel.LOGIN_SUBMIT);
	}
	
	/**
	 * uses data from UserInvalid.csv at webApp.data
	 * reruns the test per row of csv file
	 * @param username
	 * @param password
	 */
	@Test(dataProvider = "DataRunner", dataProviderClass = UserInvalid.class)
	public void invalidUserLogin(String username, String password) {
		TestLog.ConsoleLog("username "  + username);
		TestLog.ConsoleLog("password "  + password);

		User user = Data.webApp.user().withUsername(username).withPassword(password);
		TestLog.When("I login with invalid user");
		app.webApp.login.loginError(user);

	}
}