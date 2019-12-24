package test.module.framework.tests.functional.service;


import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.JsonHelper;
import core.apiCore.helpers.XmlHelper;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class XmlTemplateTest extends TestBase {
	
	@Test()
	public void updateXmlTemplateFile() throws Exception {
		
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Defects.xml");
		
		Path templatePath = DataHelper.getTemplateFilePath(serviceObject.getTemplateFile());
		String xmlString = XmlHelper.convertXmlFileToString(templatePath);
		Helper.assertTrue("xml string is empty", !xmlString.isEmpty());
		
		TestLog.When("I replace the tag at different positions in the xml file");
		String tag = "soi:EquipmentID";
		xmlString = XmlHelper.replaceTagValue(xmlString, tag, "test33");
		xmlString = XmlHelper.replaceTagValue(xmlString, tag, "test44", 2);
		xmlString = XmlHelper.replaceTagValue(xmlString, tag, "test55", 3);


		TestLog.Then("I verify tag values have been updated");		
		String tagValue = XmlHelper.getXmlTagValue(xmlString, tag);
		Helper.assertEquals("test33", tagValue);
		
		tagValue = XmlHelper.getXmlTagValue(xmlString, tag, 2);
		Helper.assertEquals("test44", tagValue);
		
		tagValue = XmlHelper.getXmlTagValue(xmlString, tag, 3);
		Helper.assertEquals("test55", tagValue);
		
		TestLog.Then("I verify xml string is valid");		
		Document doc = XmlHelper.convertXmlStringToDocument(xmlString);
		String convertedXml = XmlHelper.convertDocumentToString(doc);
		tagValue = XmlHelper.getXmlTagValue(convertedXml, tag, 3);
		Helper.assertEquals("test55", tagValue);
	}
	
	@Test()
	public void updateXmlTemplateFileFromRequest() throws Exception {
		
		TestLog.When("I replace the tag at different positions in the xml file");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Defects.xml")
				.withRequestBody("soi:EquipmentID:1:equip_<@_TIME_19>; soi:EquipmentID:2:equip_313");
		
		String xmlString = DataHelper.getRequestBodyIncludingTemplate(serviceObject);
		Helper.assertTrue("xml string is empty", !xmlString.isEmpty());

		String tag = "soi:EquipmentID";
		
		TestLog.Then("I verify tag values have been updated");		
		String tagValue = XmlHelper.getXmlTagValue(xmlString, tag, 1);
		Instant time = Instant.parse(Config.getValue(TestObject.START_TIME_STRING_MS));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")
				.withZone(ZoneId.of("UTC"));
		String value = formatter.format(time);
		Helper.assertEquals("equip_"+ value, tagValue);
		
	    tagValue = XmlHelper.getXmlTagValue(xmlString, tag, 2);
		Helper.assertEquals("equip_313", tagValue);
	}
	
	@Test()
	public void updateXmlTemplateFileFromRequestWithoutRequest() throws Exception {
		
		TestLog.When("I replace the tag at different positions in the xml file");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Defects.xml")
				.withRequestBody("");
		
		String xmlString = XmlHelper.getRequestBodyFromXmlTemplate(serviceObject);
		Helper.assertTrue("xml string is empty", !xmlString.isEmpty());

		String tag = "soi:EquipmentID";
		xmlString = XmlHelper.replaceRequestTagValues(serviceObject);
		
		TestLog.Then("I verify tag values have been updated");		
		
	    String tagValue = XmlHelper.getXmlTagValue(xmlString, tag, 1);
		Helper.assertEquals("R5578", tagValue);
	}
	
	@Test(description = "convert xml file to json and validate against partial response")
	public void convertXmlTemplateFileToJsonAndValidate() throws Exception {
		
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Defects.xml");
		
		Path templatePath = DataHelper.getTemplateFilePath(serviceObject.getTemplateFile());
		String xmlString = XmlHelper.convertXmlFileToString(templatePath);
		Helper.assertTrue("xml string is empty", !xmlString.isEmpty());
		String json = JsonHelper.XMLToJson(xmlString);
		boolean isValidJson = JsonHelper.isJSONValid(json, false);
		Helper.assertTrue("json string is not valid", isValidJson);	
		
		String criteria = "<msg:Message xmlns:hdr=\"urn:soi.sample.com:header:V1_3\" xmlns:msg=\"urn:soi.sample.com:message:V1_3\" xmlns:soi=\"urn:soi.sample.com:payload:V1_3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"<msg:Header>\n" + 
				"	<hdr:ServiceName>Defect</hdr:ServiceName>\n" + 
				"	<hdr:OriginatingEvent>\n" + 
				"	<hdr:EventOriginator></hdr:EventOriginator>\n" + 
				"	<hdr:EventCorrelation>Defect</hdr:EventCorrelation>\n" + 
				"	<hdr:EventTime></hdr:EventTime>\n" + 
				"	</hdr:OriginatingEvent>\n" + 
				"</msg:Header>\n" + 
				"<msg:Payload>\n" + 
				"	<soi:Defect>\n" + 
				"		<soi:DefectRecord>\n" + 
				"			<soi:DefectID>D-3</soi:DefectID>\n" + 
				"			<soi:EquipmentID>R5578</soi:EquipmentID>\n" + 
				"			<soi:EquipmentID>R5578</soi:EquipmentID>\n" + 
				"			<soi:EquipmentID>R5578</soi:EquipmentID>\n" + 
				"			<soi:Component>EGI_METER</soi:Component>	\n" + 
				"			<soi:Position longitude=\"-98.505845\" latitude=\"29.553513\">\n" + 
				"				<soi:PositionFeatureID>POS-D-2</soi:PositionFeatureID>\n" + 
				"				<soi:SpacialReference>SR-D-2</soi:SpacialReference>\n" + 
				"			</soi:Position>									\n" + 
				"		</soi:DefectRecord>\n" + 
				"	</soi:Defect>\n" + 
				"</msg:Payload>\n" + 
				"</msg:Message>";
		
		boolean isValidXml = XmlHelper.isValidXmlString(criteria);
		Helper.assertTrue("json string is not valid", isValidXml);	

		String jsonExpected = JsonHelper.XMLToJson(criteria);
		isValidJson = JsonHelper.isJSONValid(json, false);
		Helper.assertTrue("json string is not valid", isValidJson);	
		
		String errors = JsonHelper.validateByJsonBody(jsonExpected, json);
		Helper.assertTrue("errors occured: " +  errors, errors.isEmpty());
	}
	

}