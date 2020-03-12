package module.services.method;

import java.util.ArrayList;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;

import core.helpers.Helper;
import core.support.logger.TestLog;

public class Command {
	
	
	/**
	 * custom command with error returned as string
	 * eg. .price:command(Command.isAllNumbersGreaterThan,3)
	 * format: jsonpath:command(class.methodname, value1, value2)
	 * values are stored in array list of objects
	 * 
	 * used with _VERIFY_JSON_PART_
	 * 
	 * @param jsonpathResponse : json response from json path 
	 * @param values as ArrayList of String. Only Array list of String is supported for values
	 * @return error value as string. empty if success and no error
	 */
	public static String isAllNumbersGreaterThanSumOf(String response, ArrayList<String> values) {
	
		int targetValue = Integer.valueOf(values.get(0)) + Integer.valueOf(values.get(1));
		String[] responses = response.split(",");
		
		TestLog.ConsoleLog("validating all values in: " + response + " are greater than: " + targetValue);	
		
		for(String responseString : responses) {
			if(Helper.getDoubleFromString(responseString) <=  targetValue)
				return "value: " + responseString + " is less than or equal to: " + targetValue;
		}
		return StringUtils.EMPTY;
	}
	
	
	/**
	 * command for checking if values in array are unique
	 * used with _VERIFY_JSON_PART_
	 * @param jsonpathResponse
	 * @return
	 */
	public static String isAllDifferent(String response) {
		String[] responses = response.split(",");
		HashSet<String> set = new HashSet<>();
		for (String s : responses) {
			if (!set.add(s)) {
				return "values in response are not unique: " + responses;
			}
		}

		return StringUtils.EMPTY;
	}
	
	/**
	 * command for verifying if response has particular value
	 * used with _VERIFY_RESPONSE_BODY_
	 * 
	 * @param response
	 * @param values
	 * @return
	 */
	public static String containsValue(String response, ArrayList<String> values) {
		String value = values.get(0).toString();
		if(!response.contains(value))
			return "response does not contain value: " + value;
		
		return StringUtils.EMPTY;
	}

}
