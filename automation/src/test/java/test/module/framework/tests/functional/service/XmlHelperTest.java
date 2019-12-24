package test.module.framework.tests.functional.service;


import java.nio.file.Path;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.XmlHelper;
import core.helpers.Helper;
import core.support.objects.ServiceObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class XmlHelperTest extends TestBase {
		
	@Test()
	public void getXmlTagValue_valid() {
		String xml = "<note>\n" + 
				"       <to>Tove</to>\n" + 
				"       <from>Jani</from>\n" + 
				"       <heading>Reminder</heading>\n" + 
				"       <body>Don't forget me this weekend!</body>\n" + 
				"   </note>";
		
		String value = XmlHelper.getXmlTagValue(xml, "from");
		Helper.assertEquals("Jani", value);
	}
	
	@Test()
	public void getXmlTagValue_valid_array() {
		String xml = "<book>\n" + 
				"<note>\n" + 
				"       <to>Tove</to>\n" + 
				"       <from>Jani</from>\n" + 
				"       <heading>Reminder</heading>\n" + 
				"       <body>Don't forget me this weekend!</body>\n" + 
				"   </note>\n" + 
				"  <note>\n" + 
				"       <to>Henry</to>\n" + 
				"       <from>bob</from>\n" + 
				"       <heading>forgot</heading>\n" + 
				"       <body>you forgot</body>\n" + 
				"   </note>\n" + 
				"</book>";
		
		String value1 = XmlHelper.getXmlTagValue(xml, "from", 1);
		String value2 = XmlHelper.getXmlTagValue(xml, "from", 2);

		Helper.assertEquals("Jani", value1);
		Helper.assertEquals("bob", value2);

	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void getXmlTagValue_invalid() {
		String xml = "<note>\n" + 
				"       <to>Tove</to>\n" + 
				"       <from>Jani</from>\n" + 
				"       <heading>Reminder</heading>\n" + 
				"       <body>Don't forget me this weekend!</body>\n" + 
				"   </note>";
		
		String value = XmlHelper.getXmlTagValue(xml, "from2");
		Helper.assertEquals("Jani", value);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void getXmlTagValue_invalid_position() {
		String xml = "<book>\n" + 
				"<note>\n" + 
				"       <to>Tove</to>\n" + 
				"       <from>Jani</from>\n" + 
				"       <heading>Reminder</heading>\n" + 
				"       <body>Don't forget me this weekend!</body>\n" + 
				"   </note>\n" + 
				"  <note>\n" + 
				"       <to>Henry</to>\n" + 
				"       <from>bob</from>\n" + 
				"       <heading>forgot</heading>\n" + 
				"       <body>you forgot</body>\n" + 
				"   </note>\n" + 
				"</book>";
		
		String value = XmlHelper.getXmlTagValue(xml, "from", 0);
		Helper.assertEquals("bob", value);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void getXmlTagValue_invalid_position_out_of_bounds() {
		String xml = "<book>\n" + 
				"<note>\n" + 
				"       <to>Tove</to>\n" + 
				"       <from>Jani</from>\n" + 
				"       <heading>Reminder</heading>\n" + 
				"       <body>Don't forget me this weekend!</body>\n" + 
				"   </note>\n" + 
				"  <note>\n" + 
				"       <to>Henry</to>\n" + 
				"       <from>bob</from>\n" + 
				"       <heading>forgot</heading>\n" + 
				"       <body>you forgot</body>\n" + 
				"   </note>\n" + 
				"</book>";
		
		String value = XmlHelper.getXmlTagValue(xml, "from", 3);
		Helper.assertEquals("bob", value);
	}
	
	@Test(description = "verify xml from file is converted to xml string and validate tag value from string")
	public void convertXmlFileToString_valid() {
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Defects.xml");
		
		Path templatePath = DataHelper.getTemplateFilePath(serviceObject.getTemplateFile());

		String xmlString = XmlHelper.convertXmlFileToString(templatePath);
		String value = XmlHelper.getXmlTagValue(xmlString, "soi:EquipmentID", 1);
		Helper.assertEquals("R5578", value);
	}
	
	@Test()
	public void getXpathFromXml_valid() {
		String xmlString = "<bookstore>\n" + 
				"<book>\n" + 
				"  <title>Harry Potter</title>\n" + 
				"  <price>29.99</price>\n" + 
				"</book>\n" + 
				"<book>\n" + 
				"  <title>Learning XML</title>\n" + 
				"  <price>39.95</price>\n" + 
				"</book>\n" + 
				"</bookstore>";
		
		String value = XmlHelper.getXpathFromXml(xmlString, "/bookstore/book[price>35.00]/title");
		Helper.assertEquals("Learning XML", value);
	}
	
	@Test()
	public void getXpathFromXml_valid_multiple() {
		String xmlString = "<bookstore>\n" + 
				"<book>\n" + 
				"  <title>Harry Potter</title>\n" + 
				"  <price>29.99</price>\n" + 
				"</book>\n" + 
				"<book>\n" + 
				"  <title>Learning XML</title>\n" + 
				"  <price>39.95</price>\n" + 
				"</book>\n" + 
				"</bookstore>";
		
		String value = XmlHelper.getXpathFromXml(xmlString, "/bookstore/book[price>25.00]/title");
		Helper.assertEquals("Harry Potter,Learning XML", value);
	}
	
	@Test()
	public void getXpathFromXml_invalid_xpath() {
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<bookstore>\n" + 
				"<book>\n" + 
				"  <title lang=\"en\">Harry Potter</title>\n" + 
				"  <price>29.99</price>\n" + 
				"</book>\n" + 
				"<book>\n" + 
				"  <title lang=\"en\">Learning XML</title>\n" + 
				"  <price>39.95</price>\n" + 
				"</book>\n" + 
				"</bookstore>";
		
		String value = XmlHelper.getXpathFromXml(xmlString, "/bookstore2/book[price>35.00]/title");
		Helper.assertEquals("", value);
	}
	
	@Test()
	public void convertXmlStringToDocument_valid() {
		String xmlString = "<bookstore>\n" + 
				"<book>\n" + 
				"  <title>Harry Potter</title>\n" + 
				"  <price>29.99</price>\n" + 
				"</book>\n" + 
				"<book>\n" + 
				"  <title>Learning XML</title>\n" + 
				"  <price>39.95</price>\n" + 
				"</book>\n" + 
				"</bookstore>";
		
		Document value = XmlHelper.convertXmlStringToDocument(xmlString);
		Helper.assertTrue("xml string was not converted to document", value != null);
	}
	
	@Test()
	public void convertXmlStringToDocument_valid_xml_header() {
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<bookstore>\n" + 
				"<book>\n" + 
				"  <title lang=\"en\">Harry Potter</title>\n" + 
				"  <price>29.99</price>\n" + 
				"</book>\n" + 
				"<book>\n" + 
				"  <title lang=\"en\">Learning XML</title>\n" + 
				"  <price>39.95</price>\n" + 
				"</book>\n" + 
				"</bookstore>";
		
		Document value = XmlHelper.convertXmlStringToDocument(xmlString);
		Helper.assertTrue("xml string was not converted to document", value != null);
	}
	
	@Test()
	public void isValidXmlString() {
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<bookstore>\n" + 
				"<book>\n" + 
				"  <title lang=\"en\">Harry Potter</title>\n" + 
				"  <price>29.99</price>\n" + 
				"</book>\n" + 
				"<book>\n" + 
				"  <title lang=\"en\">Learning XML</title>\n" + 
				"  <price>39.95</price>\n" + 
				"</book>\n" + 
				"</bookstore>";
		
		boolean isValidXml= XmlHelper.isValidXmlString(xmlString);
		Helper.assertTrue("xml is not valid", isValidXml);
	}
	
	@Test()
	public void isValidXmlString_invalid() {
		String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<bookstore>\n" + 
				"<book>\n" + 
				"  <title lang=\"en\">Harry Potter</title>\n" + 
				"  <price>29.99</price>\n" + 
				"</book>\n" + 
				"<book>\n" + 
				"  <title lang=\"en\">Learning XML</title>\n" + 
				"  <price>39.95</price>\n" + 
				"</bookstore>";
		
		boolean isValidXml= XmlHelper.isValidXmlString(xmlString);
		Helper.assertTrue("xml is valid", !isValidXml);
	}
	
}