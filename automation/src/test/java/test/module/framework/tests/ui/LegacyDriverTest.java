package test.module.framework.tests.ui;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.legacy.DriverLegacy;
import core.helpers.legacy.Helper;
import io.github.bonigarcia.wdm.WebDriverManager;
/**
 * this test class demonstrates 3 data types: data object, csv data and data provider
 * @author ehsan matean
 *
 */
public class LegacyDriverTest {

	@BeforeMethod
	public void beforeMethod() throws Exception {
		
	}
	
	@FindBy(id="email")
	public WebElement email;
	
	@FindBy(id="password")
	public WebElement password;
	
	@FindBy(css="[type='submit']")
	public WebElement submit;
	
	@FindBy(css=".projectName")
	public WebElement logo;
	
	@FindBy(css="input")
	List<WebElement> inputs;
	
	@SuppressWarnings("deprecation")
	@Test()
	public void verifyAdminUserWithCsvData() throws IOException {

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        PageFactory.initElements(driver, this);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        driver.navigate().to("http://demo.autonomx.io/admin/auth/login");

		email.clear();
		Helper.setLegacyDriver(driver);
	    Helper.setField(email, "autouser313@gmail.com");
	    Helper.setField(password, "autoPass1");
	    Helper.clickAndExpect(submit, logo);
	    
	    String val = "Proxy element for: DefaultElementLocator 'By.cssSelector: [ng-show*='deviceEntryForm'].error-messages:not(.ng-hide)'";
	    
	   String[] locators =  DriverLegacy.getLocatorThroughParsing(val);
	   Helper.assertTrue("value should be cssSelector, was: " + locators[0],locators[0].equals("cssSelector"));
	   Helper.assertTrue("size should be 2, was: " + locators.length,locators.length == 2);

	   val = "[[ChromeDriver: chrome on MAC (3d75dd68e7b27687d2da9f4426d21863)] -> xpath: //span[contains(text(),'Current Offer')]/parent::div[1]/parent::div[1]/following-sibling::div[1]/div[1]/span[1]]";
	   locators =  DriverLegacy.getLocatorThroughParsing(val);
	   Helper.assertTrue("value should be cssSelector, was: " + locators[0],locators[0].equals("xpath"));
	   Helper.assertTrue("size should be 2, was: " + locators.length,locators.length == 2);
   
	    //Close the Browser.
	     driver.close();
	}
}