package test.module.framework.tests.functional.service;


import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.JsonHelper;
import core.apiCore.helpers.XmlHelper;
import core.helpers.DateHelper;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.KeyValue;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class DataHelperTest extends TestBase {
	
	private final String jsonBookStore = "{\n" + 
			"    \"store\": {\n" + 
			"        \"book\": [\n" + 
			"            {\n" + 
			"                \"category\": \"reference\",\n" + 
			"                \"author\": \"Nigel Rees\",\n" + 
			"                \"title\": \"Sayings of the Century\",\n" + 
			"                \"price\": 8.95\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"category\": \"fiction\",\n" + 
			"                \"author\": \"Evelyn Waugh\",\n" + 
			"                \"title\": \"Sword of Honour\",\n" + 
			"                \"price\": 12.99\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"category\": \"fiction\",\n" + 
			"                \"author\": \"Herman Melville\",\n" + 
			"                \"title\": \"Moby Dick\",\n" + 
			"                \"isbn\": \"0-553-21311-3\",\n" + 
			"                \"price\": 8.99\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"category\": \"fiction\",\n" + 
			"                \"author\": \"J. R. R. Tolkien\",\n" + 
			"                \"title\": \"The Lord of the Rings\",\n" + 
			"                \"isbn\": \"0-395-19395-8\",\n" + 
			"                \"price\": 22.99\n" + 
			"            }\n" + 
			"        ],\n" + 
			"        \"bicycle\": {\n" + 
			"            \"color\": \"red\",\n" + 
			"            \"price\": 19.95\n" + 
			"        }\n" + 
			"    },\n" + 
			"    \"expensive\": 10\n" + 
			"}";
	
	
	@Test()
	public void getValidationMap_keyValue() {
		
		TestLog.Then("I verify key, value combination");
		List<KeyValue> keywords = DataHelper.getValidationMap("EquipmentID:equip_1");
		Helper.assertEquals("EquipmentID", keywords.get(0).key);
		Helper.assertEquals("", keywords.get(0).position);
		Helper.assertEquals("equip_1", keywords.get(0).value.toString());
	}
	
	@Test()
	public void getValidationMap_jsonPath() {
		
		TestLog.Then("I verify key, value combination");
		List<KeyValue> keywords = DataHelper.getValidationMap("event.data.parties..data.partyUUID:contains(1234)");
		Helper.assertEquals("event.data.parties..data.partyUUID", keywords.get(0).key);
		Helper.assertEquals("", keywords.get(0).position);
		Helper.assertEquals("contains(1234)", keywords.get(0).value.toString());
	}
	
	@Test()
	public void getValidationMap_keyValue2() {
		
		TestLog.Then("I verify key, value combination");
		List<KeyValue> keywords = DataHelper.getValidationMap("results..id: hasItems(QA1-AST-001)");
		Helper.assertEquals("results..id", keywords.get(0).key);
		Helper.assertEquals("", keywords.get(0).position);
		Helper.assertEquals("hasItems(QA1-AST-001)", keywords.get(0).value.toString());
	}
	
	@Test()
	public void getValidationMap_keyValue_hasItems() {
		
		TestLog.Then("I verify key, value combination");
		List<KeyValue> keywords = DataHelper.getValidationMap("EquipmentID:hasItems(equip_1)");
		Helper.assertEquals("EquipmentID", keywords.get(0).key);
		Helper.assertEquals("", keywords.get(0).position);
		Helper.assertEquals("hasItems(equip_1)", keywords.get(0).value.toString());
	}
	
	@Test()
	public void getValidationMap_keyValue3() {
		
		TestLog.Then("I verify key, value combination");
		List<KeyValue> keywords = DataHelper.getValidationMap("$..[?(@.priority && !@.responsibleOrg)].workOrderUUID:isEmpty");
		Helper.assertEquals("$..[?(@.priority && !@.responsibleOrg)].workOrderUUID", keywords.get(0).key);
		Helper.assertEquals("", keywords.get(0).position);
		Helper.assertEquals("isEmpty", keywords.get(0).value.toString());
	}
	
	@Test()
	public void getValidationMap_json_string() {
		
		TestLog.Then("I verify json string value");
		
		String jsonString = "[\n" + 
				"   {\n" + 
				"      \"category\" : \"reference\",\n" + 
				"      \"author\" : \"Nigel Rees\",\n" + 
				"      \"title\" : \"Sayings of the Century\",\n" + 
				"      \"price\" : 8.95\n" + 
				"   },\n" + 
				"   {\n" + 
				"      \"category\" : \"fiction\",\n" + 
				"      \"author\" : \"Herman Melville\",\n" + 
				"      \"title\" : \"Moby Dick\",\n" + 
				"      \"isbn\" : \"0-553-21311-3\",\n" + 
				"      \"price\" : 8.99\n" + 
				"   }\n" + 
				"]";
		
		List<KeyValue> keywords = DataHelper.getValidationMap("store.book[?(@.price < 10)]:jsonbody("+ jsonString + ")");
		Helper.assertEquals("store.book[?(@.price < 10)]", keywords.get(0).key);
		Helper.assertEquals("", keywords.get(0).position);
		
		String[] jsonResponseValue =  keywords.get(0).value.toString().split("[\\(\\)]");

		String error = JsonHelper.validateByJsonBody(jsonString,  jsonResponseValue[1]);
		Helper.assertTrue("errors not caught: " + error, error.isEmpty());
	}
	
	@Test()
	public void verifySingleKey() {
		
		TestLog.Then("I verify key, value combination");
		List<KeyValue> keywords = DataHelper.getValidationMap("single_value");
		Helper.assertEquals("single_value", keywords.get(0).key);
	}
	
	@Test()
	public void replaceParameters_valid() {
		ConfigVariable.setValue("user1", "bob");

		TestLog.Then("I verify key, value combination");
		String result = DataHelper.replaceParameters("user:<@user1>");
		Helper.assertEquals("user:bob", result);
	}
	
	@Test()
	public void replaceParameters_time_initialTime() {
		
		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("<@_TIME;setInitialDate:1526796552>");
		Helper.assertEquals("2018-05-20T06:09:12.000Z", result.toString());
		
		Config.putValue("epochTime", "1526796552");
		result = DataHelper.replaceParameters("<@_TIME;setInitialDate:{@epochTime}>");
		Helper.assertEquals("2018-05-20T06:09:12.000Z", result.toString());
		
		result = DataHelper.replaceParameters("<@_TIME;setInitialDate:{@epochTime};FORMAT:yyyyMMddHHmmssSSS>");
		Helper.assertEquals("20180520060912000", result.toString());
	}
	
	@Test()
	public void replaceParameters_time_format() {
		
		TestLog.Then("I verify date replacement");
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");
		
		String result = DataHelper.replaceParameters("<@_TIME>");
		Helper.assertEquals("2020-09-16T04:37:30.804Z", result);
		
		result = DataHelper.replaceParameters("user:<@_TIME17>");
		Helper.assertEquals(22, result.length());
		
		result = DataHelper.replaceParameters("user:<@_TIME20_>");
		Helper.assertEquals(25, result.length());
		
		result = DataHelper.replaceParameters("user:<@_TIME_17;FORMAT:yyyyMMddHHmmssSSS>");
		Helper.assertEquals(22, result.length());
		
		result = DataHelper.replaceParameters("user:<@_TIME_15;FORMAT:MM/dd/yyyy>");
		Helper.assertEquals(15, result.length());
		
		result = DataHelper.replaceParameters("user:<@_TIME_17;FORMAT:dd-M-yyyy hh:mm:ss>");
		Helper.assertEquals(22, result.length());
		
		result = DataHelper.replaceParameters("user:<@_TIME17;FORMAT:dd MMM yyyy HH:mm:ss z>");
		Helper.assertEquals(22, result.length());
		
		result = DataHelper.replaceParameters("user:<@_TIME_17+72h;FORMAT:yyyyMMddHHmmssSSS>");
		Helper.assertEquals(22, result.length());
		
		result = DataHelper.replaceParameters("user:<@_TIME17+72h;FORMAT:yyyy.MM.dd G 'at' HH:mm:ss z>");
		Helper.assertEquals(22, result.length());
		
		result = DataHelper.replaceParameters("user:<@_TIME_34+72h>");
		Instant time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		Instant newTime = time.plus(72, ChronoUnit.HOURS);
		Helper.assertEquals("user:" + newTime.toString(), result);
		
		result = DataHelper.replaceParameters("user:<@_TIME34+72h_>");
		time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		newTime = time.plus(72, ChronoUnit.HOURS);
		Helper.assertEquals("user:" + newTime.toString(), result);
	}
	
	@Test()
	public void replaceParameters_time_daylight() {
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");
		String result = DataHelper.replaceParameters("<@_TIME_27+1w;setDay:Monday;setTime:09:00:00;ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-09-21T16:00:00.000Z", result);
		
		result = DataHelper.replaceParameters("<@_TIME_27+1w;FORMAT:yyyy-MM-dd'T'HH:mm:ss.SSS;setDay:Monday;setTime:09:00:00;ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-09-21T16:00:00.000", result);
	
	}
	
	@Test()
	public void replaceParameters_time_Default() {
		
		Config.putValue(DateHelper.CONFIG_DATE_FORMAT_DEFAULT, StringUtils.EMPTY);
		Config.putValue(DateHelper.CONFIG_DATE_ZONE_INPUT_DEFAULT, StringUtils.EMPTY);
		Config.putValue(DateHelper.CONFIG_DATE_ZONE_OUTPUT_DEFAULT, StringUtils.EMPTY);

		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");
		String result = DataHelper.replaceParameters("<@_TIME_30;FORMAT:EEEE, dd MMMM, yyyy;ZONE:Canada/Pacific;LOCALE:france>");
		Helper.assertEquals("mercredi, 16 septembre, 2020", result);
		
		result = DataHelper.replaceParameters("<@_TIME>");
		Helper.assertEquals("2020-09-16T04:37:30.804", result);
		
		result = DataHelper.replaceParameters("<@_TIME_30;FORMAT:EEEE, dd MMMM>");
		Helper.assertEquals("Wednesday, 16 September", result);
		
		result = DataHelper.replaceParameters("<@_TIME_30;FORMAT:EEEE, dd MMMM, yyyy;LOCALE:france>");
		Helper.assertEquals("mercredi, 16 septembre, 2020", result);
		
		result = DataHelper.replaceParameters("<@_TIME_55;FORMAT:dd-M-yyyy hh:mm:ss;ZONE:Asia/Kolkata;LOCALE:germany>");
		Helper.assertEquals("15-9-2020 11:07:30", result);
	}
	
	@Test()
	public void replaceParameters_time_Default_modified() {
		
		Config.putValue(DateHelper.CONFIG_DATE_FORMAT_DEFAULT, "dd-M-yyyy hh:mm:ss");
		Config.putValue(DateHelper.CONFIG_DATE_ZONE_INPUT_DEFAULT, "Canada/Pacific");
		Config.putValue(DateHelper.CONFIG_DATE_ZONE_OUTPUT_DEFAULT, "Canada/Pacific");

		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");
		String result = DataHelper.replaceParameters("<@_TIME_30;FORMAT:EEEE, dd MMMM, yyyy;ZONE:Canada/Pacific;LOCALE:france>");
		Helper.assertEquals("mercredi, 16 septembre, 2020", result);
		
		result = DataHelper.replaceParameters("<@_TIME>");
		Helper.assertEquals("16-9-2020 04:37:30", result);
		
		result = DataHelper.replaceParameters("<@_TIME_30;FORMAT:EEEE, dd MMMM>");
		Helper.assertEquals("Wednesday, 16 September", result);
		
		result = DataHelper.replaceParameters("<@_TIME_30;FORMAT:EEEE, dd MMMM, yyyy;LOCALE:france>");
		Helper.assertEquals("mercredi, 16 septembre, 2020", result);
		
		result = DataHelper.replaceParameters("<@_TIME_55;FORMAT:dd-M-yyyy hh:mm:ss;ZONE:Asia/Kolkata;LOCALE:germany>");
		Helper.assertEquals("15-9-2020 04:07:30", result);
	}
	
	@Test()
	public void replaceParameters_time_locale() {
		
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");
		String result = DataHelper.replaceParameters("<@_TIME_30;FORMAT:EEEE, dd MMMM, yyyy;ZONE:Canada/Pacific;LOCALE:france>");
		Helper.assertEquals("mercredi, 16 septembre, 2020", result);
		
		result = DataHelper.replaceParameters("<@_TIME_30;FORMAT:EEEE, dd MMMM, yyyy;LOCALE:france>");
		Helper.assertEquals("mercredi, 16 septembre, 2020", result);
		
		result = DataHelper.replaceParameters("<@_TIME_55;FORMAT:dd-M-yyyy hh:mm:ss;ZONE:Asia/Kolkata;LOCALE:germany>");
		Helper.assertEquals("15-9-2020 11:07:30", result);
	}

	@Test()
	public void replaceParameters_time_zone() {
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");

		String result = DataHelper.replaceParameters("user:<@_TIME_55;FORMAT:dd-M-yyyy hh:mm:ss;ZONE:Asia/Kolkata>");
		Helper.assertEquals("user:15-9-2020 11:07:30", result);
		
		result = DataHelper.replaceParameters("<@_TIME_55;ZONE:Asia/Kolkata;FORMAT:dd-M-yyyy hh:mm:ss>");
		Helper.assertEquals("15-9-2020 11:07:30" + "", result);
		
		result = DataHelper.replaceParameters("<@_TIME_55;ZONE:Asia/Kolkata;FORMAT:ccyy-MM-dd hh:mm:ss>");
		Helper.assertEquals("2120-09-15 11:07:30" + "", result);

		
		result = DataHelper.replaceParameters("<@_TIME_55+1w;FORMAT:ccyy-MM-dd hh:mm:ss>");
		Helper.assertEquals("2120-09-23 04:37:30" + "", result);
		
		result = DataHelper.replaceParameters("<@_TIME_55+1d;FORMAT:ccyy-MM-dd hh:mm:ss>");
		Helper.assertEquals("2120-09-17 04:37:30" + "", result);
		
		Config.putValue(TestObject.START_TIME_STRING,"2020-03-25T03:21:18.052Z");
		
	    result = DataHelper.replaceParameters("<@_TIME_MS_23+1w;setDay:Monday;setTime:09:00:00;ZONE:Canada/Pacific>");
		Helper.assertEquals("1585584000000", result);
		
		result = DataHelper.replaceParameters("<@_TIME_MS_23+1w;ZONE:Canada/Pacific;setDay:Monday;setTime:09:00:00>");
		Helper.assertEquals("1585584000000", result);
		
		result = DataHelper.replaceParameters("<@_TIME_MS_23+1w;setDay:Monday;ZONE:Canada/Pacific;setTime:09:00:00>");
		Helper.assertEquals("1585584000000", result);
		
		result = DataHelper.replaceParameters("<@_TIME_23+1w;FORMAT:ccyy-MM-dd hh:mm:ss;setDay:Monday;ZONE:Canada/Pacific;setTime:09:00:00>");
		Helper.assertEquals("2120-03-30 04:00:00", result);
	}
	
	@Test()
	public void replaceParameters_time_outputZone() throws ParseException {
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");

		// time should be UTC by default
		String result = DataHelper.replaceParameters("<@_TIME_55>");
		Helper.assertEquals("2020-09-16T04:37:30.804Z", result);
			
		Config.putValue(TestObject.START_TIME_STRING,"2020-08-03T12:00:00.000Z");
		
		result = DataHelper.replaceParameters("<@_TIME_55;ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-08-03T19:00:00.000Z", result);

		result = DataHelper.replaceParameters("<@_TIME_55;FORMAT:yyyy-MM-dd'T'HH:mm:ss.SSSXXX;ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-08-03T19:00:00.000Z", result);
		
		result = DataHelper.replaceParameters("<@_TIME_55;FORMAT:yyyy-MM-dd'T'HH:mm:ss.SSSXXX;ZONE:Canada/Pacific;OUTPUT_ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-08-03T12:00:00.000-07:00", result);
		
		result = DataHelper.replaceParameters("<@_TIME_55;FORMAT:yyyy-MM-dd'T'HH:mm:ss.SSSXXX;OUTPUT_ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-08-03T05:00:00.000-07:00", result);
	}
	
	@Test()
	public void dateOfweekTest() {
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");

		// this tuesday test
		String result = DataHelper.replaceParameters("<@_TIME_ISO_35;setDay:Tuesday>");
		Helper.assertEquals("2020-09-15T04:37:30.804Z", result);
		
		// next week
		result = DataHelper.replaceParameters("<@_TIME_ISO_35+2w;setDay:Tuesday>");
	
		Helper.assertEquals("2020-09-29T04:37:30.804Z", result);
	}
	
	@Test()
	public void replaceParameters_uuid() {
		
		// values should be different
		String result = DataHelper.replaceParameters("<@_RANDUUID>");
		String result2 = DataHelper.replaceParameters("<@_RANDUUID>");
		Helper.assertTrue("values should be differnet", !result.equals(result2));
		
		Helper.assertTrue("should be uuid", Helper.isUUID(result));
		
		// values should be the same
		result = DataHelper.replaceParameters("<@_UUID_STATIC>");
		result2 = DataHelper.replaceParameters("<@_UUID_STATIC>");
		Helper.assertTrue("values should be differnet", result.equals(result2));
		
		Helper.assertTrue("should be uuid", Helper.isUUID(result));
	}
	
	@Test()
	public void replaceParameters_format_cc() {
		Config.putValue(TestObject.START_TIME_STRING,"2020-08-03T12:00:00.000Z");
		
		String result = DataHelper.replaceParameters("<@_TIME;FORMAT:ccyy-MM-dd hh:mm:ss>");
		Helper.assertEquals("2120-08-03 12:00:00", result);
		
		result = DataHelper.replaceParameters("<@_TIME+1w;FORMAT:CCyy-MM-dd hh:mm:ss>");
		Helper.assertEquals("2120-08-10 12:00:00", result);
		
		result = DataHelper.replaceParameters("<@_TIME_+1w;FORMAT:CCyy-MM-dd hh:mm:ss>");
		Helper.assertEquals("2120-08-10 12:00:00", result);
	}
	
	@Test()
	public void replaceParameters_time_set_time() {
		Config.putValue(TestObject.START_TIME_STRING,"2020-10-01T12:00:00.000Z");

		TestLog.Then("I verify setting fixed time value");
		String result = DataHelper.replaceParameters("<@_TIME24+72h;setTime:14:23:33>");
		Helper.assertEquals("2020-10-04T14:23:33.000Z", result);
		
		result = DataHelper.replaceParameters("<@_TIME24+72h;setTime:08:11:00>");
		Helper.assertEquals("2020-10-04T08:11:00.000Z", result);
		
		result = DataHelper.replaceParameters("<@_TIME24+72h;setTime:8:1:0>");
		Helper.assertEquals("2020-10-04T08:01:00.000Z", result);
	}
	
	@Test()
	public void replaceParameters_time_multiple_modifications() {
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-25T12:00:00.000Z");

		String result = DataHelper.replaceParameters("<@_TIME+1h+1w;setDay:Monday;setTime:09:00:00;ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-09-28T17:00:00.000Z", result);
		
		result = DataHelper.replaceParameters("<@_TIME+1h+1w;setDay:Monday;ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-09-28T20:00:00.000Z", result);
		
		result = DataHelper.replaceParameters("<@_TIME+1h+1w-1d;setDay:Monday;setTime:09:00:00;ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-09-27T17:00:00.000Z", result);
		
		result = DataHelper.replaceParameters("<@_TIME+1h+1w-1d+1mo;setDay:Monday;setTime:09:00:00;ZONE:Canada/Pacific>");
		Helper.assertEquals("2020-10-27T17:00:00.000Z", result);

	}
	
	@Test()
	public void replaceParameters_time_set_day() {
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");

		// this tuesday test
		String result = DataHelper.replaceParameters("<@_TIME_ISO_35;setDay:Tuesday>");
		Helper.assertEquals("2020-09-15T04:37:30.804Z", result);
		
		// next week
		result = DataHelper.replaceParameters("<@_TIME_ISO_35+2w;setDay:Tuesday>");
		Helper.assertEquals("2020-09-29T04:37:30.804Z", result);
	}
	
	@Test()
	public void replaceParameters_time_set_month() {
		
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");

		// this january
		String result = DataHelper.replaceParameters("<@_TIME_ISO_35;setMonth:January>");
		Helper.assertEquals("2020-01-16T04:37:30.804Z", result);
		
		// november
		result = DataHelper.replaceParameters("<@_TIME_ISO_35+6w;setMonth:November>");
		Helper.assertEquals("2020-12-28T04:37:30.804Z", result);
	}
	
	@Test()
	public void replaceParameters_time_modify_iso() {
		Config.putValue(TestObject.START_TIME_STRING,"2020-09-16T04:37:30.804Z");

		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@_TIME_ISO+72h>");
		
		Helper.assertEquals("user:2020-09-19T04:37:30.804Z" , result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_35-72h>");
		Helper.assertEquals("user:2020-09-13T04:37:30.804Z" , result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_35+72m>");
		Helper.assertEquals("user:2020-09-16T05:49:30.804Z", result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_35-72m>");
		Helper.assertEquals("user:2020-09-16T03:25:30.804Z" , result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO-2d>");		
		Helper.assertEquals("user:2020-09-14T04:37:30.804Z" , result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_35+2d>");
		Helper.assertEquals("user:2020-09-18T04:37:30.804Z" , result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO+2w>");
		Helper.assertEquals("user:2020-09-30T04:37:30.804Z", result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO+2mo>");	
		Helper.assertEquals("user:2020-11-16T04:37:30.804Z" , result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_35+2y>");	
		Helper.assertEquals("user:2022-09-16T04:37:30.804Z" , result);
	}
	
	@Test()
	public void replaceParameters_time_modify() {
		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@_TIME_STRING_17+72h>");
		Instant time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		Instant newTime = time.plus(72, ChronoUnit.HOURS);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
				.withZone(ZoneId.of("UTC"));
		String value = formatter.format(newTime);
		Helper.assertEquals("user:" + value, result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_STRING_17-72h>");
		newTime = time.minus(72, ChronoUnit.HOURS);
		formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
				.withZone(ZoneId.of("UTC"));
		value = formatter.format(newTime);
		Helper.assertEquals("user:" + value, result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_STRING_17+72m>");
		newTime = time.plus(72, ChronoUnit.MINUTES);
		formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
				.withZone(ZoneId.of("UTC"));
		value = formatter.format(newTime);
		Helper.assertEquals("user:" + value, result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_STRING_17-72m>");
		newTime = time.minus(72, ChronoUnit.MINUTES);
		formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
				.withZone(ZoneId.of("UTC"));
		value = formatter.format(newTime);
		Helper.assertEquals("user:" + value, result);
	}
	
	@Test()
	public void replaceParameters_time_string_modify() {
		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@_TIME_STRING_17+72h>");
		Instant time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		Instant newTime = time.plus(72, ChronoUnit.HOURS);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
				.withZone(ZoneId.of("UTC"));
		String value = formatter.format(newTime);
		Helper.assertEquals("user:" + value, result);
		result = DataHelper.replaceParameters("user:<@_TIME_STRING_17-72h>");
		newTime = time.minus(72, ChronoUnit.HOURS);
		formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
				.withZone(ZoneId.of("UTC"));
		value = formatter.format(newTime);
		Helper.assertEquals("user:" + value, result);
		result = DataHelper.replaceParameters("user:<@_TIME_STRING_17+72m>");
		newTime = time.plus(72, ChronoUnit.MINUTES);
		formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
				.withZone(ZoneId.of("UTC"));
		value = formatter.format(newTime);
		Helper.assertEquals("user:" + value, result);
		result = DataHelper.replaceParameters("user:<@_TIME_STRING_17-72m>");
		newTime = time.minus(72, ChronoUnit.MINUTES);
		formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
				.withZone(ZoneId.of("UTC"));
		value = formatter.format(newTime);
		Helper.assertEquals("user:" + value, result);
	}
	
	@Test()
	public void replaceParameters_time_epoch_modify() {
		TestLog.Then("I verify date replacement");
		
		String result = DataHelper.replaceParameters("<@_TIME_MS_23+72h>");
		Instant time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		Instant newTime = time.plus(72, ChronoUnit.HOURS);
		String value = String.valueOf(newTime.toEpochMilli());
		Helper.assertEquals(value, result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_MS_13-72h>");	
		newTime = time.minus(72, ChronoUnit.HOURS);
		value = String.valueOf(newTime.toEpochMilli());
		Helper.assertEquals("user:" + value, result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_MS_13+72m>");
		newTime = time.plus(72, ChronoUnit.MINUTES);
		value = String.valueOf(newTime.toEpochMilli());
		Helper.assertEquals("user:" + value, result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_MS_13-72m>");
		newTime = time.minus(72, ChronoUnit.MINUTES);
		value = String.valueOf(newTime.toEpochMilli());
		Helper.assertEquals("user:" + value, result);
		
		result = DataHelper.replaceParameters("<@_TIME_S_13-72m>");
		newTime = time.minus(72, ChronoUnit.MINUTES);
		value = String.valueOf(newTime.getEpochSecond());
		Helper.assertEquals(value, result);
	}
	
	
	@Test()
	public void replaceParameters_multiple_colon() {

		TestLog.Then("I verify date replacement");
		Config.putValue("userid", "user:value:id:1");

		String result = DataHelper.replaceParameters("user:<@userid>:T11:00:00.000Z");
		Helper.assertEquals("user:user:value:id:1:T11:00:00.000Z", result);
	}
	
	@Test()
	public void replaceParameters_integer() {

		Config.putValue("userid", 5);
		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@userid>");
		Helper.assertEquals("user:5", result);
	}
	
	@Test()
	public void replaceParameters_email() {

		Config.putValue("email", "bob.jones@gmail.com");
		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@email>");
		Helper.assertEquals("user:bob.jones@gmail.com", result);
	}
	
	@Test()
	public void replaceParameters_boolean() {

		Config.putValue("isAvailable", true);
		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@isAvailable>");
		Helper.assertEquals("user:true", result);
	}
	
	@Test()
	public void replaceParameters_string() {

		Config.putValue("availability", "always");
		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@availability>");
		Helper.assertEquals("user:always", result);
	}
	
	@Test()
	public void replaceParameters_object() {

		Object object1 = "always";
		Config.putValue("availability", object1 );
		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@availability>");
		Helper.assertEquals("user:always", result);
	}
	
	@Test()
	public void replaceParameters_random() {

		TestLog.Then("I verify random replacement");
		String result = DataHelper.replaceParameters("user:<@_RAND4>");
		Helper.assertEquals(9, result.length());
	}
	
	@Test()
	public void replaceParameters_invalid() {
		TestLog.Then("I verify key, value combination");
		String result = DataHelper.replaceParameters("user:<@user1>");
		Helper.assertEquals("user:<@user1>", result);
	}
	
	@Test()
	public void replaceParameters_invalidParameter() {
		TestLog.Then("I verify key, value combination");
		String result = DataHelper.replaceParameters("user:<$user1>");
		Helper.assertEquals("user:<$user1>", result);
		
		ConfigVariable.setValue("user1", "bob");
		result = DataHelper.replaceParameters("user:@user1");
		Helper.assertEquals("user:@user1", result);
	}
	
	@Test()
	public void validateCommand_integerEqual() {
		
		TestLog.Then("I verify getting json value from path");
		String responseString = JsonHelper.getJsonValue(jsonBookStore, "store.bicycle.price");

		String errors = DataHelper.validateCommand("integerEqual", responseString, "19.95");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerEqual", responseString, "[19.95]");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerEqual", responseString, "[1995]");
		Helper.assertTrue("errors caught: " + errors, !errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerEqual", responseString, " ");
		Helper.assertTrue("errors caught: " + errors, !errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerEqual", responseString, "text");
		Helper.assertTrue("errors caught: " + errors, !errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerEqual", responseString, "text19.95");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
	}
	
	@Test()
	public void validateCommand_integerGreaterThan() {
		
		TestLog.Then("I verify getting json value from path");
		String responseString = JsonHelper.getJsonValue(jsonBookStore, "store.bicycle.price");

		String errors = DataHelper.validateCommand("integerGreaterThan", responseString, "19.94");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerGreaterThan", responseString, "[19]");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerGreaterThan", responseString, "[19.96]");
		Helper.assertTrue("errors caught: " + errors, !errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerGreaterThan", responseString, "[20]");
		Helper.assertTrue("errors caught: " + errors, !errors.isEmpty());
	}
	
	@Test()
	public void validateCommand_integerLessThan() {
		
		TestLog.Then("I verify getting json value from path");
		String responseString = JsonHelper.getJsonValue(jsonBookStore, "store.bicycle.price");

		String errors = DataHelper.validateCommand("integerLessThan", responseString, "19.94");
		Helper.assertTrue("errors caught: " + errors, !errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerLessThan", responseString, "[19]");
		Helper.assertTrue("errors caught: " + errors, !errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerLessThan", responseString, "[19.96]");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerLessThan", responseString, "[20]");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
	}
	
	@Test()
	public void validateCommand_integerNotEqual() {
		
		TestLog.Then("I verify getting json value from path");
		String responseString = JsonHelper.getJsonValue(jsonBookStore, "store.bicycle.price");

		String errors = DataHelper.validateCommand("integerNotEqual", responseString, "19.95");
		Helper.assertTrue("errors caught: " + errors, !errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerNotEqual", responseString, "[19.95]");
		Helper.assertTrue("errors caught: " + errors, !errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerNotEqual", responseString, "[1995]");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerNotEqual", responseString, "19.96");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		errors = DataHelper.validateCommand("integerNotEqual", responseString, "");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
	}
	
	@Test()
	public void validateCommand_response_multiple_colon() {
		
		String result = DataHelper.replaceParameters("<@_TIME10+24h_>T11:00:00.000Z");

		String errors = DataHelper.validateCommand("equalTo", result, result, "1", false);
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		String dateString  = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );	
		errors = DataHelper.validateCommand("equalTo", dateString, dateString, "1", false);
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		Config.putValue("date", dateString);
		result = DataHelper.replaceParameters("<@date>");
		errors = DataHelper.validateCommand("equalTo", result, dateString, "1", false);
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
	}
	
	@Test()
	public void getValidationMap_keyValue_date() {
		
		TestLog.Then("I verify key, value combination");
		List<KeyValue> keywords = DataHelper.getValidationMap("soi:EquipmentID:1:notEqualTo(2019110423T11:00:00.000Z)");
		Helper.assertEquals("soi:EquipmentID", keywords.get(0).key);
		Helper.assertEquals("1", keywords.get(0).position);
		Helper.assertEquals("notEqualTo(2019110423T11:00:00.000Z)", keywords.get(0).value.toString());
		
		keywords = DataHelper.getValidationMap("soi:EquipmentID:notEqualTo(2019110423T11:00:00.000Z)");
		Helper.assertEquals("soi:EquipmentID", keywords.get(0).key);
		Helper.assertEquals("", keywords.get(0).position);
		Helper.assertEquals("notEqualTo(2019110423T11:00:00.000Z)", keywords.get(0).value.toString());
	
		keywords = DataHelper.getValidationMap("soi:EquipmentID:1:2019-05-10 03:50:41.123Z");
		Helper.assertEquals("soi:EquipmentID", keywords.get(0).key);
		Helper.assertEquals("1", keywords.get(0).position);
		Helper.assertEquals("2019-05-10 03:50:41.123Z", keywords.get(0).value.toString());
		
		keywords = DataHelper.getValidationMap("..raisedDateTime:2019-05-10 03:50:41.123Z");
		Helper.assertEquals("..raisedDateTime", keywords.get(0).key);
		Helper.assertEquals("", keywords.get(0).position);
		Helper.assertEquals("2019-05-10 03:50:41.123Z", keywords.get(0).value.toString());
		
		keywords = DataHelper.getValidationMap("$.store.book[?(@.price < 10)]:equip_2019-12-04T05:01:51");
		Helper.assertEquals("$.store.book[?(@.price < 10)]", keywords.get(0).key);
		Helper.assertEquals("", keywords.get(0).position);
		Helper.assertEquals("equip_2019-12-04T05:01:51", keywords.get(0).value.toString());
		
		keywords = DataHelper.getValidationMap("$.store.book[?(@.price < 10)]:3:2019-12-04T05:01:51");
		Helper.assertEquals("$.store.book[?(@.price < 10)]", keywords.get(0).key);
		Helper.assertEquals("3", keywords.get(0).position);
		Helper.assertEquals("2019-12-04T05:01:51", keywords.get(0).value.toString());

	}
	
	@Test()
	public void getValidationMap_valid() {
		
		TestLog.When("I verify key, position, value combination");
		List<KeyValue> keywords = DataHelper.getValidationMap("soi:EquipmentID:1:equip_1");
		Helper.assertEquals("soi:EquipmentID", keywords.get(0).key);
		Helper.assertEquals("1", keywords.get(0).position);
		Helper.assertEquals("equip_1", keywords.get(0).value.toString());
		
		TestLog.Then("I verify key, value combination");
	    keywords = DataHelper.getValidationMap("EquipmentID:3:equip_1");
		Helper.assertEquals("EquipmentID", keywords.get(0).key);
		Helper.assertEquals("3", keywords.get(0).position);
		Helper.assertEquals("equip_1", keywords.get(0).value.toString());
	}
	
	@Test()
	public void getValidationMap_keyValue_isEmpty() {
		
		TestLog.Then("I verify key, value combination");
		List<KeyValue> keywords = DataHelper.getValidationMap("soi:EquipmentID:1:isEmpty");
		Helper.assertEquals("soi:EquipmentID", keywords.get(0).key);
		Helper.assertEquals("1", keywords.get(0).position);
		Helper.assertEquals("isEmpty", keywords.get(0).value.toString());
	}
	
	@Test()
	public void getRequestBodyIncludingTemplate_xml_valid_multi_line() throws Exception {
		
		TestLog.When("I replace the tag at different positions in the xml file");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Defects.xml")
				.withRequestBody("soi:EquipmentID:1:test33; \n soi:EquipmentID:2:test34;");
		
		String xmlString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("xml string is empty", !xmlString.isEmpty());

		TestLog.Then("I verify tag values have been updated");		
		String tagValue = XmlHelper.getXmlTagValue(xmlString, "soi:EquipmentID");
		Helper.assertEquals("test33", tagValue);
		
		tagValue = XmlHelper.getXmlTagValue(xmlString, "soi:EquipmentID", 2);
		Helper.assertEquals("test34", tagValue);
	}
	
	@Test()
	public void getRequestBodyIncludingTemplate_xml_date() throws Exception {
		
		TestLog.When("I replace the tag at different positions in the xml file");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Defects.xml")
				.withRequestBody("soi:EquipmentID:1:wo_<@_TIME_ISO_35>");
		
		String xmlString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("xml string is empty", !xmlString.isEmpty());

		TestLog.Then("I verify tag values have been updated");		
		String tagValue = XmlHelper.getXmlTagValue(xmlString, "soi:EquipmentID");
		String result = DataHelper.replaceParameters("wo_<@_TIME_ISO_35>");
		Helper.assertEquals(result, tagValue);
	}
	
	@Test()
	public void getRequestBodyIncludingTemplate_json_valid() throws Exception {
		
		TestLog.When("I replace the tag at different positions in the json file");
		Config.putValue("quizItem", "quiz2");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Quiz.json")
				.withRequestBody("quiz.sport.q1.options:2:value_<@quizItem>");
		
		String jsonString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		jsonString = DataHelper.replaceParameters(jsonString);
		Helper.assertTrue("json string is empty", !jsonString.isEmpty());
		
		String updatedValue = JsonHelper.getJsonValue(jsonString, "quiz.sport.q1.options");
		
		Helper.assertEquals("value_quiz2", updatedValue);
	}
	
	@Test()
	public void getRequestBodyIncludingTemplate_json_date() throws Exception {
		
		TestLog.When("I replace the tag at different positions in the json file");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Quiz.json")
				.withRequestBody("quiz.sport.q1.options:2:value_<@_TIME_ISO_35>");
		
		String jsonString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("json string is empty", !jsonString.isEmpty());
		
		String updatedValue = JsonHelper.getJsonValue(jsonString, "quiz.sport.q1.options");
		String result = DataHelper.replaceParameters("value_<@_TIME_ISO_35>");

		Helper.assertEquals(result, updatedValue);
	}
	
	@Test()
	public void getRequestBodyIncludingTemplate_txt_valid() throws Exception {
		
		TestLog.When("I use a text file as template");
		Config.putValue("dataReplace", "correct");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("data.txt");
		
		String textString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("text string is empty", !textString.isEmpty());
		
		Helper.assertEquals("Data template example for request body: correct", textString);
	}
	
	@Test()
	public void getRequestBodyIncludingTemplate_no_template() throws Exception {
		
		TestLog.When("I use request body without template");
		Config.putValue("dataReplace", "correct");
		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("this is the content: <@dataReplace>");
		
		String textString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("text string is empty", !textString.isEmpty());
		
		Helper.assertEquals("this is the content: correct", textString);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void getRequestBodyIncludingTemplate_invalid_wrong_template() throws Exception {
		
		TestLog.When("I use a non existant template");
		ServiceObject serviceObject = new ServiceObject().withTemplateFile("invalid.json");
		
		 DataHelper.getRequestBodyIncludingTemplate(serviceObject);
	}
	
	@Test()
	public void getRequestBodyIncludingTemplate_update_request_json() throws Exception {
		
		String expectedResponse = "[{\"category1\":\"action\",\"author1\":\"Nigel Rees\",\"title1\":\"title1\",\"price1\":8.95},{\"category2\":\"scifi\",\"author2\":\"Herman Melville\",\"title2\":\"Moby Dick\",\"isbn2\":\"H/APPR\",\"price2\":8.99}]";
		
		String json = "[\n" + 
				"   {\n" + 
				"      \"category1\" : \"reference\",\n" + 
				"      \"author1\" : \"Nigel Rees\",\n" + 
				"      \"title1\" : \"title1\",\n" + 
				"      \"price1\" : 8.95\n" + 
				"   },\n" + 
				"   {\n" + 
				"      \"category2\" : \"fiction\",\n" + 
				"      \"author2\" : \"Herman Melville\",\n" + 
				"      \"title2\" : \"Moby Dick\",\n" + 
				"      \"isbn2\" : \"H/APPR\",\n" + 
				"      \"price2\" : 8.99\n" + 
				"   }\n" + 
				"]";
		
		Config.putValue("variable", json);
		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("<@variable>\n" + 
						"&&\n" + 
						"_UPDATE_REQUEST_\n" + 
						".category1:action;\n" + 
						".category2:scifi");
		
		String textString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("text string is empty", !textString.isEmpty());
		
		Helper.assertEquals(expectedResponse, textString);
	}
	
	@Test()
	public void getRequestBodyIncludingTemplate_update_request_xml() throws Exception {
	
		String expectedResponse = "<note>\n" + 
				"       <to>dave</to>\n" + 
				"       <from>Jani</from>\n" + 
				"       <heading>Reminder</heading>\n" + 
				"       <body>you forgot</body>\n" + 
				"   </note>";
		
		String xml = "<note>\n" + 
				"       <to>Tove</to>\n" + 
				"       <from>Jani</from>\n" + 
				"       <heading>Reminder</heading>\n" + 
				"       <body>Don't forget me this weekend!</body>\n" + 
				"   </note>";
		
		Config.putValue("variable", xml);
		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("<@variable>\n" + 
						"&&\n" + 
						"_UPDATE_REQUEST_\n" + 
						"to:dave;\n" + 
						"body:you forgot");
		
		String textString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("text string is empty", !textString.isEmpty());
		
		Helper.assertEquals(expectedResponse, textString);
	}
	
	@Test()
	public void validateExpectedValues_empty_response() throws Exception {
		
		TestLog.When("I receive empty responses");
		
		 List<String> responses = new ArrayList<String>();
		 responses.add("");
		 responses.add(" ");
		 responses.add(" ");
		 
		 TestLog.Then("then error should be returned");

		 List<String> errors =  DataHelper.validateExpectedValues(responses, "expected");
		 Helper.assertTrue("errors was not returned", !errors.isEmpty());
	}
	
	@Test()
	public void validateExpectedValues_empty_expected_response() throws Exception {
		
		TestLog.When("I receive empty expected");
		
		 List<String> responses = new ArrayList<String>();
		 responses.add("response");
		 
		 TestLog.Then("then error should be returned");

		 List<String> errors =  DataHelper.validateExpectedValues(responses, "");
		 Helper.assertTrue("errors was returned", errors.isEmpty());
		 
		 errors =  DataHelper.validateExpectedValues(responses, " ");
		 Helper.assertTrue("errors was returned", errors.isEmpty());
	}
	
	@Test()
	public void validateExpectedValues_multiple_and() throws Exception {

		TestLog.When("I receive empty expected");
		
		String jsonResponse = "{\n" + 
				"  \"total\": 36,\n" + 
				"  \"results\": [\n" + 
				"    {\n" + 
				"      \"parentWorkOrder\": {\n" + 
				"        \"workOrderUUID\": \"cfd96554-1e1b-40bc-8a9d-dc1f9e2de56f\"\n" + 
				"      },\n" + 
				"      \"userStatus\": \"Pending\",\n" + 
				"      \"assetWork\": false,\n" + 
				"      \"createdTimestamp\": \"2020-06-15T17:41:40.393Z\",\n" + 
				"      \"description\": \"Create WorkOrder SG TEST 1\",\n" + 
				"      \"workOrderUUID\": \"45870867-9bf2-436e-b0c2-16135523168d\",\n" + 
				"      \"priority\": 50,\n" + 
				"      \"raisedDateTime\": \"2019-10-19T17:01:43Z\"\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";

		 List<String> responses = new ArrayList<String>();
		 responses.add(jsonResponse);

		 TestLog.Then("then error should be returned");

		 List<String> errors =  DataHelper.validateExpectedValues(responses, "_VERIFY_JSON_PART_ $..[?(@.priority && !@.responsibleOrg)].workOrderUUID:isEmpty");
		 Helper.assertTrue("errors was returned", !errors.isEmpty());
		 Helper.assertTrue("errors was returned", errors.get(0).equals("value is not empty"));
		 
		 errors =  DataHelper.validateExpectedValues(responses, "_VERIFY_JSON_PART_ $..[?(@.priority && !@.responsibleOrg)].workOrderUUID:isEmpty && _VERIFY_JSON_PART_ .workOrderUUID:isEmpty");
		 Helper.assertTrue("errors was returned", !errors.isEmpty());
		 Helper.assertTrue("errors was returned", errors.get(0).equals("value is not empty"));
		 
		 errors =  DataHelper.validateExpectedValues(responses, "_VERIFY_JSON_PART_ $..[?(@.priority || !@.responsibleOrg && !@.responsibleOrg)].workOrderUUID:isEmpty && _VERIFY_JSON_PART_ .workOrderUUID:isEmpty");
		 Helper.assertTrue("errors was returned", !errors.isEmpty());
		 Helper.assertTrue("errors was returned", errors.get(0).equals("value is not empty"));
		 
		 errors =  DataHelper.validateExpectedValues(responses, "_VERIFY_JSON_PART_ $..[?(@.priority || @.responsibleOrg && !@.responsibleOrg)].workOrderUUID:isEmpty && _VERIFY_JSON_PART_ .workOrderUUID:isEmpty");
		 Helper.assertTrue("errors was returned", !errors.isEmpty());
		 Helper.assertTrue("errors was returned", errors.get(0).equals("value is not empty"));
	}
	
	@Test()
	public void validateExpectedValues_multiple_expectations() throws Exception {
		
		TestLog.When("I have multiple expectations, with expection 1 having an error: zzz_updatetoknzjjl0708karbinvalid");
		
		 List<String> responses = new ArrayList<String>();
		 responses.add("[{\"id\":6473,\"username\":\"zzz_updatetoknzjjl0708karb\",\"email\":\"testUpdate+2020011307105959@gmail.com\",\"provider\":\"local\",\"confirmed\":true,\"blocked\":null,\"role\":{\"id\":2,\"name\":\"Authenticated\",\"description\":\"Default role given to authenticated user.\",\"type\":\"authenticated\"}}]");
		 String expected = "{\"id\":6473,\n" + 
		 		"\"username\":\"zzz_updatetoknzjjl0708karbinvalid\",\n" + 
		 		"\"email\":\"testUpdate+2020011307105959@gmail.com\",\n" + 
		 		"\"provider\":\"local\",\n" + 
		 		"\"confirmed\":true,\n" + 
		 		"\"blocked\":null,\n" + 
		 		"\"role\":\n" + 
		 		"      {\n" + 
		 		"       \"name\":\"Authenticated\",\n" + 
		 		"        \"description\":\"Default role given to authenticated user.\",\n" + 
		 		"        \"type\":\"authenticated\"\n" + 
		 		"      }\n" + 
		 		"}\n" + 
		 		"&&\n" + 
		 		"_VERIFY.JSON.PART_\n" + 
		 		".role:jsonbody([{\n" + 
		 		"       \"name\":\"Authenticated\",\n" + 
		 		"        \"description\":\"Default role given to authenticated user.\",\n" + 
		 		"        \"type\":\"authenticated\"\n" + 
		 		"      }])";
		 TestLog.Then("then error should be returned");

		 List<String> errors =  DataHelper.validateExpectedValues(responses, expected);
		 Helper.assertTrue("errors was not returned", !errors.isEmpty());
	}
	
	@Test()
	public void saveDataToConfig_valid() {
		
		DataHelper.saveDataToConfig("testvalue1:<$value1>; testvalue2:<$value2>");
		Helper.assertEquals("testvalue1", Config.getValue("value1"));
		Helper.assertEquals("testvalue2", Config.getValue("value2"));
		
		DataHelper.saveDataToConfig("testvalue1:<@value3>");
		Helper.assertEquals("", Config.getValue("value3"));
		
		DataHelper.saveDataToConfig("<@_RAND13>:<$value4>");
		String random = Config.getValue(TestObject.RANDOM_STRING).substring(0, 13);
		Helper.assertEquals(random, Config.getValue("value4"));
	}
	
	@Test()
	public void getLocalDateTime_valid() {
		LocalDateTime date = Helper.date.getLocalDateTime("2020-04-21T01:03:47.034");
		Helper.assertTrue("", date != null);
	}
	
	@Test()
	public void validateExpectedValues_xml_ignoreNameSpace() throws Exception {
		Config.putValue(DataHelper.IS_IGNORE_XML_NAMESPACE, "true");
		Config.setGlobalValue(DataHelper.IS_IGNORE_XML_NAMESPACE, "true");
		
		 String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
			 		"<soapenv:Envelope\n" + 
			 		"        xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" + 
			 		"        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" + 
			 		"        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
			 		"  <soapenv:Header>\n" + 
			 		"    <ns1:RequestHeader\n" + 
			 		"         soapenv:actor=\"http://schemas.xmlsoap.org/soap/actor/next\"\n" + 
			 		"         soapenv:mustUnderstand=\"0\"\n" + 
			 		"         xmlns:ns1=\"https://www.google.com/apis/ads/publisher/v202002\">\n" + 
			 		"      <ns1:networkCode>123456</ns1:networkCode>\n" + 
			 		"      <ns1:applicationName>DfpApi-Java-2.1.0-dfp_test</ns1:applicationName>\n" + 
			 		"    </ns1:RequestHeader>\n" + 
			 		"  </soapenv:Header>\n" + 
			 		"  <soapenv:Body>\n" + 
			 		"    <getAdUnitsByStatement xmlns=\"https://www.google.com/apis/ads/publisher/v202002\">\n" + 
			 		"      <filterStatement>\n" + 
			 		"        <query>WHERE parentId IS NULL LIMIT 500</query>\n" + 
			 		"      </filterStatement>\n" + 
			 		"    </getAdUnitsByStatement>\n" + 
			 		"  </soapenv:Body>\n" + 
			 		"</soapenv:Envelope>";
		 
		 String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		 		"<soapenv:Envelope\n" + 
		 		"        xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\n" + 
		 		"        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" + 
		 		"        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
		 		"  <soapenv:Header>\n" + 
		 		"    <ns1:RequestHeader\n" + 
		 		"         soapenv:actor=\"http://schemas.xmlsoap.org/soap/actor/next\"\n" + 
		 		"         soapenv:mustUnderstand=\"0\"\n" + 
		 		"         xmlns:ns1=\"https://www.google.com/apis/ads/publisher/v202002\">\n" + 
		 		"      <ns1:networkCode>123456</ns1:networkCode>\n" + 
		 		"    </ns1:RequestHeader>\n" + 
		 		"  </soapenv:Header>\n" + 
		 		"</soapenv:Envelope>";
		
		 List<String> responses = new ArrayList<String>();
		 responses.add(xmlString);
		 
		 List<String> errors =  DataHelper.validateExpectedValues(responses, expected);
		 Helper.assertTrue("errors was not returned", errors.isEmpty());
	}
	
	@Test()
	public void validateExpectedValues_xml_ignoreNameSpace_enabled() throws Exception {
		Config.putValue(DataHelper.IS_IGNORE_XML_NAMESPACE, "true");
		Config.setGlobalValue(DataHelper.IS_IGNORE_XML_NAMESPACE, "true");
		
		 String xmlString = "<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n" + 
			 		"<cont:contact xmlns:cont = \"www.tutorialspoint.com/profile\">\n" + 
			 		"   <cont:name>Tanmay Patil</cont:name>\n" + 
			 		"   <cont:company>TutorialsPoint</cont:company>\n" + 
			 		"   <cont:phone>(011) 123-4567</cont:phone>\n" + 
			 		"</cont:contact>";
		 
		 String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + 
		 		"<contact>\n" + 
		 		"   <name>Tanmay Patil</name>\n" + 
		 		"   <company>TutorialsPoint</company>\n" + 
		 		"</contact>";
		 List<String> responses = new ArrayList<String>();
		 responses.add(xmlString);
		 
		 List<String> errors =  DataHelper.validateExpectedValues(responses, expected);
		 Helper.assertTrue("errors was not returned", errors.isEmpty());
	}
	
	@Test()
	public void validateExpectedValues2_xml_ignoreNameSpace_disabled() throws Exception {
		Config.putValue(DataHelper.IS_IGNORE_XML_NAMESPACE, "false");
		Config.setGlobalValue(DataHelper.IS_IGNORE_XML_NAMESPACE, "false");
		
		 String xmlString = "<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n" + 
			 		"<cont:contact xmlns:cont = \"www.tutorialspoint.com/profile\">\n" + 
			 		"   <cont:name>Tanmay Patil</cont:name>\n" + 
			 		"   <cont:company>TutorialsPoint</cont:company>\n" + 
			 		"   <cont:phone>(011) 123-4567</cont:phone>\n" + 
			 		"</cont:contact>";
		 
		 String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + 
		 		"<contact>\n" + 
		 		"   <name>Tanmay Patil</name>\n" + 
		 		"   <company>TutorialsPoint</company>\n" + 
		 		"</contact>";
		
		 List<String> responses = new ArrayList<String>();
		 responses.add(xmlString);
		 
		 List<String> errors =  DataHelper.validateExpectedValues(responses, expected);
		 Helper.assertTrue("errors was returned", !errors.isEmpty());
	}
}