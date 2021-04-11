package test.module.framework.tests.functional.service;


import java.nio.file.Path;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

import core.apiCore.helpers.DataHelper;
import core.apiCore.helpers.XmlHelper;
import core.helpers.Helper;
import core.support.logger.TestLog;
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
	public void getXmlTagValue_all() {
		String xml = "<note>\n" + 
				"       <to>Tove</to>\n" + 
				"       <from>Jani</from>\n" + 
				"       <heading>Reminder</heading>\n" + 
				"       <body>Don't forget me this weekend!</body>\n" + 
				"   </note>";
		
		String value = XmlHelper.getXmlTagValue(xml, ".");
		Helper.assertEquals(xml, value);
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
	
	@Test()
	public void removeXmlNameSpace_soap() {
	 String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
	 		"<soapenv:Envelope\n" + 
	 		"        xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope2/\"\n" + 
	 		"        xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" + 
	 		"        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
	 		"  <soapenv:Header action=\"Created\">\n" + 
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
	 
	 String xmlNoNameSpace = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Envelope>\n"
	 		+ "  <Header action=\"Created\">\n"
	 		+ "    <RequestHeader>\n"
	 		+ "      <networkCode>123456</networkCode>\n"
	 		+ "      <applicationName>DfpApi-Java-2.1.0-dfp_test</applicationName>\n"
	 		+ "    </RequestHeader>\n"
	 		+ "  </Header>\n"
	 		+ "  <Body>\n"
	 		+ "    <getAdUnitsByStatement>\n"
	 		+ "      <filterStatement>\n"
	 		+ "        <query>WHERE parentId IS NULL LIMIT 500</query>\n"
	 		+ "      </filterStatement>\n"
	 		+ "    </getAdUnitsByStatement>\n"
	 		+ "  </Body>\n"
	 		+ "</Envelope>\n";
	 
	 	String xmlStr = XmlHelper.removeXmlNameSpace(xmlString);

	 	TestLog.ConsoleLog(xmlStr);
	 	Helper.assertTrue("xml string is empty", !xmlStr.isEmpty());
	 	Helper.assertEquals(Helper.stringNormalize(xmlNoNameSpace), Helper.stringNormalize(xmlStr));
	}
	
	@Test()
	public void removeXmlNameSpace_soap2() {
	 String xmlString = "<?xml version = \"1.0\"?>\n" + 
	 		"<SOAP-ENV:Envelope\n" + 
	 		"   xmlns:SOAP-ENV = \"http://www.w3.org/2001/12/soap-envelope\"\n" + 
	 		"   SOAP-ENV:encodingStyle = \"http://www.w3.org/2001/12/soap-encoding\">\n" + 
	 		"\n" + 
	 		"   <SOAP-ENV:Body xmlns:m = \"http://www.xyz.org/quotation\">\n" + 
	 		"      <m:GetQuotationResponse>\n" + 
	 		"         <m:Quotation>Here is the quotation</m:Quotation>\n" + 
	 		"      </m:GetQuotationResponse>\n" + 
	 		"   </SOAP-ENV:Body>\n" + 
	 		"</SOAP-ENV:Envelope>";
	 
	 String xmlNoNameSpace = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Envelope>\n"
	 		+ "\n"
	 		+ "   <Body>\n"
	 		+ "      <GetQuotationResponse>\n"
	 		+ "         <Quotation>Here is the quotation</Quotation>\n"
	 		+ "      </GetQuotationResponse>\n"
	 		+ "   </Body>\n"
	 		+ "</Envelope>\n";
	 
	 	String xmlStr = XmlHelper.removeXmlNameSpace(xmlString);

	 	TestLog.ConsoleLog(xmlStr);
	 	Helper.assertTrue("xml string is empty", !xmlStr.isEmpty());
	 	Helper.assertEquals(Helper.stringNormalize(xmlNoNameSpace), Helper.stringNormalize(xmlStr));
	}
	
	@Test()
	public void removeXmlNameSpace2() {
	 String xmlString = "<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n" + 
	 		"<cont:contact xmlns:cont = \"www.tutorialspoint.com/profile\">\n" + 
	 		"   <cont:name>Tanmay Patil</cont:name>\n" + 
	 		"   <cont:company>TutorialsPoint</cont:company>\n" + 
	 		"   <cont:phone>(011) 123-4567</cont:phone>\n" + 
	 		"</cont:contact>";
	 
	 String xmlNoNameSpace = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><contact>\n"
	 		+ "   <name>Tanmay Patil</name>\n"
	 		+ "   <company>TutorialsPoint</company>\n"
	 		+ "   <phone>(011) 123-4567</phone>\n"
	 		+ "</contact>";
	 
	 	String xmlStr = XmlHelper.removeXmlNameSpace(xmlString);

	 	TestLog.ConsoleLog(xmlStr);
	 	Helper.assertTrue("xml string is empty", !xmlStr.isEmpty());
	 	Helper.assertEquals(Helper.stringNormalize(xmlNoNameSpace), Helper.stringNormalize(xmlStr));
	}
	
	@Test()
	public void removeXmlNameSpace3_different_namespace_tag() {
	 String xmlString = "<h:html xmlns:xdc=\"http://www.xml.com/books\"\n" + 
	 		"        xmlns:h=\"http://www.w3.org/HTML/1998/html4\">\n" + 
	 		" <h:head><h:title>Book Review</h:title></h:head>\n" + 
	 		" <h:body>\n" + 
	 		"  <xdc:bookreview>\n" + 
	 		"   <xdc:title h:style=\"font-family: sans-serif;\">\n" + 
	 		"     XML: A Primer</xdc:title>\n" + 
	 		"   <h:table>\n" + 
	 		"    <h:tr align=\"center\">\n" + 
	 		"     <h:td>Author</h:td><h:td>Price</h:td>\n" + 
	 		"     <h:td>Pages</h:td><h:td>Date</h:td></h:tr>\n" + 
	 		"    <h:tr align=\"left\">\n" + 
	 		"     <h:td><xdc:author>Simon St. Laurent</xdc:author></h:td>\n" + 
	 		"     <h:td><xdc:price>31.98</xdc:price></h:td>\n" + 
	 		"     <h:td><xdc:pages>352</xdc:pages></h:td>\n" + 
	 		"     <h:td><xdc:date>1998/01</xdc:date></h:td>\n" + 
	 		"    </h:tr>\n" + 
	 		"   </h:table>\n" + 
	 		"  </xdc:bookreview>\n" + 
	 		" </h:body>\n" + 
	 		"</h:html>";
	 
	 String xmlNoNameSpace = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><html>\n"
	 		+ " <head>\n"
	 		+ "<title>Book Review</title>\n"
	 		+ "</head>\n"
	 		+ " <body>\n"
	 		+ "  <bookreview>\n"
	 		+ "   <title>\n"
	 		+ "     XML: A Primer</title>\n"
	 		+ "   <table>\n"
	 		+ "    <tr align=\"center\">\n"
	 		+ "     <td>Author</td>\n"
	 		+ "<td>Price</td>\n"
	 		+ "     <td>Pages</td>\n"
	 		+ "<td>Date</td>\n"
	 		+ "</tr>\n"
	 		+ "    <tr align=\"left\">\n"
	 		+ "     <td>\n"
	 		+ "<author>Simon St. Laurent</author>\n"
	 		+ "</td>\n"
	 		+ "     <td>\n"
	 		+ "<price>31.98</price>\n"
	 		+ "</td>\n"
	 		+ "     <td>\n"
	 		+ "<pages>352</pages>\n"
	 		+ "</td>\n"
	 		+ "     <td>\n"
	 		+ "<date>1998/01</date>\n"
	 		+ "</td>\n"
	 		+ "    </tr>\n"
	 		+ "   </table>\n"
	 		+ "  </bookreview>\n"
	 		+ " </body>\n"
	 		+ "</html>\n"
	 		+ "";
	 
	 	String xmlStr = XmlHelper.removeXmlNameSpace(xmlString);

	 	TestLog.ConsoleLog(xmlStr);
	 	Helper.assertTrue("xml string is empty", !xmlStr.isEmpty());
	 	Helper.assertEquals(Helper.stringNormalize(xmlNoNameSpace), Helper.stringNormalize(xmlStr));
	}
}