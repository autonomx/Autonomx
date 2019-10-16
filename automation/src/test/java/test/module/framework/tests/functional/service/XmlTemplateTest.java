package test.module.framework.tests.functional.service;


import java.nio.file.Path;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.XmlHelper;
import core.helpers.Helper;
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
		String tagValue = XmlHelper.getTagValue(xmlString, tag);
		Helper.assertEquals("test33", tagValue);
		
		tagValue = XmlHelper.getTagValue(xmlString, tag, 2);
		Helper.assertEquals("test44", tagValue);
		
		tagValue = XmlHelper.getTagValue(xmlString, tag, 3);
		Helper.assertEquals("test55", tagValue);
		
		TestLog.Then("I verify xml string is valid");		
		Document doc = XmlHelper.convertXmlStringToDocument(xmlString);
		String convertedXml = XmlHelper.convertDocumentToString(doc);
		tagValue = XmlHelper.getTagValue(convertedXml, tag, 3);
		Helper.assertEquals("test55", tagValue);
	}
	
	@Test()
	public void updateXmlTemplateFileFromRequest() throws Exception {
		
		TestLog.When("I replace the tag at different positions in the xml file");
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Defects.xml")
				.withRequestBody("soi:EquipmentID:1:equip_<@_TIME_19>; soi:EquipmentID:2:equip_313");
		
		String xmlString = XmlHelper.getRequestBodyFromXmlTemplate(serviceObject);
		Helper.assertTrue("xml string is empty", !xmlString.isEmpty());

		String tag = "soi:EquipmentID";
		xmlString = XmlHelper.replaceRequestTagValues(serviceObject);
		
		TestLog.Then("I verify tag values have been updated");		
		String tagValue = XmlHelper.getTagValue(xmlString, tag, 1);
		Helper.assertEquals("equip_"+ TestObject.getTestInfo().getTestStartTime(), tagValue);
		
	    tagValue = XmlHelper.getTagValue(xmlString, tag, 2);
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
		
	    String tagValue = XmlHelper.getTagValue(xmlString, tag, 1);
		Helper.assertEquals("R5578", tagValue);
	}
}