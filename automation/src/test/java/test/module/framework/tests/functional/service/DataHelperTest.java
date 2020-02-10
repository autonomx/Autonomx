package test.module.framework.tests.functional.service;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.Test;

import configManager.ConfigVariable;
import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.JsonHelper;
import core.apiCore.helpers.XmlHelper;
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
	public void replaceParameters_time() {
		
		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@_TIME17>");
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
		
		result = DataHelper.replaceParameters("user:<@_TIME_24+72h>");
		Instant time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		Instant newTime = time.plus(72, ChronoUnit.HOURS);
		Helper.assertEquals("user:" + newTime.toString(), result);
		
		result = DataHelper.replaceParameters("user:<@_TIME24+72h_>");
		time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		newTime = time.plus(72, ChronoUnit.HOURS);
		Helper.assertEquals("user:" + newTime.toString(), result);
	}
	
	@Test()
	public void replaceParameters_time_set_time() {
		
		TestLog.Then("I verify setting fixed time value");
		String result = DataHelper.replaceParameters("<@_TIME24+72h;setTime:14:23:33>");
		Instant time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		Instant newTime = time.plus(72, ChronoUnit.HOURS);
		newTime = newTime.atZone(ZoneOffset.UTC)
		        .withHour(14)
		        .withMinute(23)
		        .withSecond(33)
		        .withNano(0)
		        .toInstant();
		Helper.assertEquals(newTime.toString(), result);
		
		result = DataHelper.replaceParameters("<@_TIME24+72h;setTime:08:11:00>");
		time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		newTime = time.plus(72, ChronoUnit.HOURS);
		newTime = newTime.atZone(ZoneOffset.UTC)
		        .withHour(8)
		        .withMinute(11)
		        .withSecond(0)
		        .withNano(0)
		        .toInstant();
		Helper.assertEquals(newTime.toString(), result);
		
		result = DataHelper.replaceParameters("<@_TIME24+72h;setTime:8:1:0>");
		time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		newTime = time.plus(72, ChronoUnit.HOURS);
		newTime = newTime.atZone(ZoneOffset.UTC)
		        .withHour(8)
		        .withMinute(1)
		        .withSecond(0)
		        .withNano(0)
		        .toInstant();
		Helper.assertEquals(newTime.toString(), result);
	}
	
	@Test()
	public void replaceParameters_time_modify_iso() {

		TestLog.Then("I verify date replacement");
		String result = DataHelper.replaceParameters("user:<@_TIME_ISO_24+72h>");
		
		Instant time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		Instant newTime = time.plus(72, ChronoUnit.HOURS);
		Helper.assertEquals("user:" +newTime.toString(), result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_24-72h>");
		newTime = time.minus(72, ChronoUnit.HOURS);
		Helper.assertEquals("user:" + newTime.toString(), result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_24+72m>");
		newTime = time.plus(72, ChronoUnit.MINUTES);
		Helper.assertEquals("user:" + newTime.toString(), result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_24-72m>");
		newTime = time.minus(72, ChronoUnit.MINUTES);
		Helper.assertEquals("user:" + newTime.toString(), result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_24-2d>");
		newTime = time.minus(2, ChronoUnit.DAYS);
		Helper.assertEquals("user:" + newTime.toString(), result);
		
		result = DataHelper.replaceParameters("user:<@_TIME_ISO_24+2d>");
		newTime = time.plus(2, ChronoUnit.DAYS);
		Helper.assertEquals("user:" + newTime.toString(), result);
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
	public void replaceParameters_time_ms_modify() {
		TestLog.Then("I verify date replacement");
		
		String result = DataHelper.replaceParameters("user:<@_TIME_MS_13+72h>");
		Instant time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING));
		Instant newTime = time.plus(72, ChronoUnit.HOURS);
		String value = String.valueOf(newTime.toEpochMilli());
		Helper.assertEquals("user:" + value, result);
		
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

		String errors = DataHelper.validateCommand("equalTo", result, result, "1");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		String dateString  = ZonedDateTime.now( ZoneOffset.UTC ).format( DateTimeFormatter.ISO_INSTANT );	
		errors = DataHelper.validateCommand("equalTo", dateString, dateString, "1");
		Helper.assertTrue("errors caught: " + errors, errors.isEmpty());
		
		Config.putValue("date", dateString);
		result = DataHelper.replaceParameters("<@date>");
		errors = DataHelper.validateCommand("equalTo", result, dateString, "1");
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
				.withRequestBody("soi:EquipmentID:1:wo_<@_TIME_ISO_24>");
		
		String xmlString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("xml string is empty", !xmlString.isEmpty());

		TestLog.Then("I verify tag values have been updated");		
		String tagValue = XmlHelper.getXmlTagValue(xmlString, "soi:EquipmentID");
		String result = DataHelper.replaceParameters("wo_<@_TIME_ISO_24>");
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
				.withRequestBody("quiz.sport.q1.options:2:value_<@_TIME_ISO_24>");
		
		String jsonString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("json string is empty", !jsonString.isEmpty());
		
		String updatedValue = JsonHelper.getJsonValue(jsonString, "quiz.sport.q1.options");
		String result = DataHelper.replaceParameters("value_<@_TIME_ISO_24>");

		Helper.assertEquals("[\"" + result + "\"]", updatedValue);
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
	}
}