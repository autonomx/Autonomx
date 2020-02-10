package test.module.framework.tests.functional.service;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.interfaces.KafkaInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.objects.ServiceObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class KafkaInterfaceTest extends TestBase {
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
	}
	
	@Test(description = "verify kafka interface with text message")
	public void evaluateKafkaInterface_text_message() throws Exception {	
		Config.putValue(KafkaInterface.KAFKA_SERVER_URL, "localhost:9092");
		Config.putValue(KafkaInterface.KFAKA_TOPIC, "test");
		Config.setGlobalValue(KafkaInterface.KFAKA_TOPIC, "test");
		Config.putValue(KafkaInterface.KAFKA_GROUP_ID, "test-consumer-group");


		String random = Helper.generateRandomString(3);
		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("test from autonomx" + random)
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY_HEADER_PART_ isEmpty"
						+ " && _VERIFY_TOPIC_PART_ equalTo(test)"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ random +")");
				
		KafkaInterface.testKafkaInterface(serviceObject);
	}
	
	@Test(description = "verify kafka interface with json message")
	public void evaluateKafkaInterface_json_message() throws Exception {	
		Config.putValue(KafkaInterface.KAFKA_SERVER_URL, "localhost:9092");
		Config.putValue(KafkaInterface.KFAKA_TOPIC, "test");
		Config.setGlobalValue(KafkaInterface.KFAKA_TOPIC, "test");
		Config.putValue(KafkaInterface.KAFKA_GROUP_ID, "test-consumer-group");

		
		String random = Helper.generateRandomString(3);
		String message = "{\n" + 
				"    \"event\": {\n" + 
				"        \"breadcrumbId\": \"BREADCRU-SING-PART-INDI-AAAA11111111\",\n" + 
				"        \"profiles\": [\"de.party.ingestion-1.0.0.json\"],\n" + 
				"        \"data\": {\n" + 
				"            \"parties\": {\n" + 
				"                \"event\": {\n" + 
				"                    \"breadcrumbId\": \"BREADCRU-SING-PART-INDI-AAAA11111111\",\n" + 
				"                    \"dataFabricId\": \"AAAAAAAA-AAAA-AAAA-AAAA-AAAA11111111\",\n" + 
				"                    \"domain\": \"PARTY\",\n" + 
				"                    \"profile\": \"de.party-1.0.0.json\",\n" + 
				"                    \"data\": {\n" + 
				"                        \"partyUUID\": \"uuid"+ random + "\",\n" + 
				"                        \"partyType\": \"individual\",\n" + 
				"                        \"individualId\": \"AAAAAAAA-AAAA-1111\",\n" + 
				"                        \"familyName\": \"AAAA1\",\n" + 
				"                        \"partyStatus\": \"inactive\",\n" + 
				"                        \"communication\": []\n" + 
				"                    }\n" + 
				"                }\n" + 
				"            }\n" + 
				"        }\n" + 
				"    }\n" + 
				"}";

		
		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody(message)
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY_HEADER_PART_ isEmpty"
						+ " && _VERIFY_TOPIC_PART_ equalTo(test)"
						+ " && _VERIFY.JSON.PART_ event.data.parties..data.partyUUID:contains(uuid"+ random +")");
				
		KafkaInterface.testKafkaInterface(serviceObject);
	}
	
	@Test(description = "verify kafka interface with xml message")
	public void evaluateKafkaInterface_xml_message() throws Exception {	
		Config.putValue(KafkaInterface.KAFKA_SERVER_URL, "localhost:9092");
		Config.putValue(KafkaInterface.KFAKA_TOPIC, "test");
		Config.setGlobalValue(KafkaInterface.KFAKA_TOPIC, "test");
		Config.putValue(KafkaInterface.KAFKA_GROUP_ID, "test-consumer-group");

		
		String random = Helper.generateRandomString(5);
		String expectedResponse = "<msg:Message xmlns:hdr=\"urn:soi.sample.com:header:V1_3\" xmlns:msg=\"urn:soi.sample.com:message:V1_3\" xmlns:soi=\"urn:soi.sample.com:payload:V1_3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
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
				"			<soi:EquipmentID>"+random+"</soi:EquipmentID>\n" + 
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

		
		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Defects.xml")
				.withRequestBody("soi:EquipmentID:1:" + random)
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY_HEADER_PART_ isEmpty"
						+ " && _VERIFY_TOPIC_PART_ equalTo(test)"
						+ " && " + expectedResponse);
				
		KafkaInterface.testKafkaInterface(serviceObject);
	}
	
	@Test(description = "verify kafka interface with separate validation")
	public void evaluateKafkaInterface_json_validate_only() throws Exception {
		// reset values
		Config.putValue(KafkaInterface.KAFKA_SERVER_URL, "localhost:9092");
		Config.putValue(KafkaInterface.KFAKA_TOPIC, "test");
		Config.setGlobalValue(KafkaInterface.KFAKA_TOPIC, "test");
		Config.putValue(KafkaInterface.KAFKA_GROUP_ID, "test-consumer-group");

		String random = Helper.generateRandomString(3);

		ServiceObject serviceObject = new ServiceObject()
				.withTemplateFile("Group.json")
				.withRequestBody("event.data.groups..data.groupUUID:uuid-" + random);
				
		KafkaInterface.testKafkaInterface(serviceObject);

		serviceObject = new ServiceObject()
				.withOption("response_identifier:" + random)
				.withExpectedResponse("_VERIFY.JSON.PART_ event.data.groups..data.groupUUID:contains(uuid-" + random + ")");

		KafkaInterface.testKafkaInterface(serviceObject);
	}
}