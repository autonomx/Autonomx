package AdditionalHelper;

import java.security.SecureRandom;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import core.helpers.Helper;
import core.uiCore.drivers.AbstractDriver;
import core.uiCore.webElement.EnhancedBy;
import core.uiCore.webElement.EnhancedWebElement;

public class HelperExt extends Helper {
	
	/**
	 * click using enhancedWebElement
	 * @param by
	 * @param index
	 */
	public static void clickSample(EnhancedBy by, int index) {
		EnhancedWebElement element = findElements(by);
		element.click(index);
	}
	
	/**
	 * hover element using action
	 * WebElement value: targetElement.get(0)
	 * @param by
	 */
	public static void hoverBySample(EnhancedBy by) {
		Actions actions = new Actions(AbstractDriver.getWebDriver());
		EnhancedWebElement targetElement = findElements(by);
		
		WebElement webElement = targetElement.get(0);
		actions.moveToElement(webElement).build().perform();
		
		Helper.waitForSeconds(0.5);
	}

	/**
	 * generates random int of length len
	 * 
	 * @param len
	 * @return
	 */
	public static String generateRandomInteger(int len) {
		String AB = "0123456789";
		SecureRandom rnd = new SecureRandom();

		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}
}