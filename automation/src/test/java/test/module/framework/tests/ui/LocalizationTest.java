package test.module.framework.tests.ui;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.helpers.Helper;
import core.support.logger.TestLog;
import module.framework.panel.LoginPanel;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class LocalizationTest extends TestBase {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		
		ConfigVariable.localizeFile().setValue("local.csv");
		Helper.localizationSetupCsv("French");
		ConfigVariable.setValue("chrome.pref.intl.accept_languages" , "fr");
		
		setupWebDriver(app.framework.getWebDriver());
	}
	
	@Test(description = "test localization and validation feature")
	public void verifyCrossProjectManager() {
		
		TestLog.Then("I verify username and password localized labels: Username = Nom d'utilisateur");
		Helper.verifyTextDisplayed("Mot de Passe");
		
		String usernameLabel = Helper.getTextValue(LoginPanel.USERNAME_LABEL);
		String password = Helper.getTextValue(LoginPanel.PASSWORD_LABEL);
		
		Helper.assertEquals("Email", usernameLabel);
		Helper.assertEquals("Mot de Passe", password);
		
		Helper.assertEquals(usernameLabel, Helper.localize("Username"));
		Helper.assertEquals(password, Helper.localize("Password"));
	}
}