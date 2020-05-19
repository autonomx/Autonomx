package test.module.framework.tests.functional.service;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.apiCore.interfaces.RabbitMqInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class RabbitMqInterfaceTest extends TestBase {

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() throws Exception {
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "realExchange");
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "realQueue");
	}

	// @Ignore // requires real rabbitmq connection
	@Test(description = "verify kafka interface with text message")
	public void evaluateRabbitMqInterface_text_message() throws Exception {

		// reset values
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "");

		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_HOST, "localhost");
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "hello");

		String random = Helper.generateRandomString(3);
		ServiceObject serviceObject = new ServiceObject().withRequestBody("test from autonomx" + random)
				.withExpectedResponse(
						"EXPECTED_MESSAGE_COUNT:1;" + " && _VERIFY_RESPONSE_BODY_ contains(" + random + ")");

		RabbitMqInterface.connectRabbitMq(serviceObject);
		RabbitMqInterface.channel.queueDeclare("hello", true, false, false, null);

		RabbitMqInterface.testRabbitMqInterface(serviceObject);
	}

	@Test(description = "verify rabbitmq interface with json message")
	public void evaluateRabbitMqInterface_json_message() throws Exception {
		// reset values
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "");

		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_HOST, "localhost");
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "hello");

		String random = Helper.generateRandomString(3);
		String message = "{\n" + "    \"event\": {\n"
				+ "        \"breadcrumbId\": \"BREADCRU-SING-PART-INDI-AAAA11111111\",\n"
				+ "        \"profiles\": [\"de.group.ingestion-1.0.0.json\"],\n" + "        \"data\": {\n"
				+ "            \"groups\": {\n" + "                \"event\": {\n"
				+ "                    \"breadcrumbId\": \"BREADCRU-SING-PART-INDI-AAAA11111111\",\n"
				+ "                    \"dataFabricId\": \"AAAAAAAA-AAAA-AAAA-AAAA-AAAA11111111\",\n"
				+ "                    \"domain\": \"PARTY\",\n"
				+ "                    \"profile\": \"de.group-1.0.0.json\",\n" + "                    \"data\": {\n"
				+ "                        \"groupUUID\": \"uuid" + random + "\",\n"
				+ "                        \"groupType\": \"individual\",\n"
				+ "                        \"individualId\": \"AAAAAAAA-AAAA-1111\",\n"
				+ "                        \"familyName\": \"AAAA1\",\n"
				+ "                        \"groupStatus\": \"inactive\",\n"
				+ "                        \"communication\": []\n" + "                    }\n" + "                }\n"
				+ "            }\n" + "        }\n" + "    }\n" + "}";

		ServiceObject serviceObject = new ServiceObject().withRequestBody(message)
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY.JSON.PART_ event.data.groups..data.groupUUID:contains(uuid" + random + ")");

		RabbitMqInterface.connectRabbitMq(serviceObject);
		RabbitMqInterface.channel.queueDeclare("hello", true, false, false, null);
		RabbitMqInterface.testRabbitMqInterface(serviceObject);
	}

	@Test(description = "verify rabbitmq interface with json message")
	public void evaluateRabbitMqInterface_json_data_message() throws Exception {
		// reset values
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "");

		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_HOST, "localhost");
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "hello");

		String random = Helper.generateRandomString(3);

		ServiceObject serviceObject = new ServiceObject().withTemplateFile("Group.json")
				.withRequestBody("event.data.groups..data.groupUUID:uuid-" + random + ";event.data.groups..data.groupTest:313" )
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY.JSON.PART_ event.data.groups..data.groupUUID:contains(uuid-" + random + ")");

		RabbitMqInterface.connectRabbitMq(serviceObject);
		RabbitMqInterface.channel.queueDeclare("hello", true, false, false, null);

		RabbitMqInterface.testRabbitMqInterface(serviceObject);
	}

	@Test(description = "verify rabbitmq interface with json message")
	public void evaluateRabbitMqInterface_json_validate_only() throws Exception {
		// reset values
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "");

		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_HOST, "localhost");
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "hello");

		String random = Helper.generateRandomString(3);

		ServiceObject serviceObject = new ServiceObject().withTemplateFile("Group.json")
				.withRequestBody("event.data.groups..data.groupUUID:uuid-" + random);

		RabbitMqInterface.connectRabbitMq(serviceObject);
		RabbitMqInterface.channel.queueDeclare("hello", true, false, false, null);
		RabbitMqInterface.testRabbitMqInterface(serviceObject);
		
		serviceObject = new ServiceObject()
				.withOption("response_identifier:" + random)
				.withExpectedResponse("_VERIFY.JSON.PART_ event.data.groups..data.groupUUID:contains(uuid-" + random + ")");

		RabbitMqInterface.connectRabbitMq(serviceObject);
		//RabbitMqInterface.channel.queueDeclare("hello", true, false, false, null);

		RabbitMqInterface.testRabbitMqInterface(serviceObject);
	}

	@Test(description = "verify rabbitmq interface with xml message")
	public void evaluateRabbitMqInterface_xml_message() throws Exception {

		// reset values
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "");

		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_HOST, "localhost");
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "hello");

		String random = Helper.generateRandomString(5);
		String expectedResponse = "<msg:Message xmlns:hdr=\"urn:soi.sample.com:header:V1_3\" xmlns:msg=\"urn:soi.sample.com:message:V1_3\" xmlns:soi=\"urn:soi.sample.com:payload:V1_3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
				+ "<msg:Header>\n" + "	<hdr:ServiceName>Defect</hdr:ServiceName>\n" + "	<hdr:OriginatingEvent>\n"
				+ "	<hdr:EventOriginator></hdr:EventOriginator>\n"
				+ "	<hdr:EventCorrelation>Defect</hdr:EventCorrelation>\n" + "	<hdr:EventTime></hdr:EventTime>\n"
				+ "	</hdr:OriginatingEvent>\n" + "</msg:Header>\n" + "<msg:Payload>\n" + "	<soi:Defect>\n"
				+ "		<soi:DefectRecord>\n" + "			<soi:DefectID>D-3</soi:DefectID>\n"
				+ "			<soi:EquipmentID>" + random + "</soi:EquipmentID>\n"
				+ "			<soi:EquipmentID>R5578</soi:EquipmentID>\n"
				+ "			<soi:EquipmentID>R5578</soi:EquipmentID>\n"
				+ "			<soi:Component>EGI_METER</soi:Component>	\n"
				+ "			<soi:Position longitude=\"-98.505845\" latitude=\"29.553513\">\n"
				+ "				<soi:PositionFeatureID>POS-D-2</soi:PositionFeatureID>\n"
				+ "				<soi:SpacialReference>SR-D-2</soi:SpacialReference>\n"
				+ "			</soi:Position>									\n" + "		</soi:DefectRecord>\n"
				+ "	</soi:Defect>\n" + "</msg:Payload>\n" + "</msg:Message>";

		ServiceObject serviceObject = new ServiceObject().withTemplateFile("Defects.xml")
				.withRequestBody("soi:EquipmentID:1:" + random)
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;" + " && " + expectedResponse);

		RabbitMqInterface.connectRabbitMq(serviceObject);
		RabbitMqInterface.channel.queueDeclare("hello", true, false, false, null);

		RabbitMqInterface.testRabbitMqInterface(serviceObject);
	}

	@Test(description = "verify exchange and queue values in options will override config values")
	public void evaluateOption_verifyExchangeAndQueueOverride() {
		Config.putValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "realExchange");
		Config.putValue(RabbitMqInterface.RABBIT_MQ_QUEUE, "realQueue");

		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "realExchange");
		TestObject.getGlobalTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "realQueue");

		ServiceObject serviceObject = new ServiceObject().withOption("EXCHANGE:fakeExchange; QUEUE:fakeQueue");

		RabbitMqInterface.evaluateOption(serviceObject);

		String exchange = Config.getValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE);
		String queue = Config.getValue(RabbitMqInterface.RABBIT_MQ_QUEUE);

		Helper.assertEquals("fakeExchange", exchange);
		Helper.assertEquals("fakeQueue", queue);
	}

	@Test(description = "verify exchange and queue values will reset to config value if options are empty")
	public void evaluateOption_verifyExchangeAndQueueResetToDefault() {

		evaluateOption_verifyExchangeAndQueueOverride();

		ServiceObject serviceObject = new ServiceObject().withOption("");

		RabbitMqInterface.evaluateOption(serviceObject);

		String exchange = Config.getValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE);
		String queue = Config.getValue(RabbitMqInterface.RABBIT_MQ_QUEUE);

		Helper.assertEquals("realExchange", exchange);
		Helper.assertEquals("realQueue", queue);
	}

	@Test(description = "verify exchange values will reset to config value if options are empty")
	public void evaluateOption_verifyExchangeToDefault() {

		evaluateOption_verifyExchangeAndQueueOverride();

		ServiceObject serviceObject = new ServiceObject().withOption("QUEUE:fakeQueue");

		RabbitMqInterface.evaluateOption(serviceObject);

		String exchange = Config.getValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE);
		String queue = Config.getValue(RabbitMqInterface.RABBIT_MQ_QUEUE);

		Helper.assertEquals("realExchange", exchange);
		Helper.assertEquals("fakeQueue", queue);
	}

	@Test(description = "verify queue values will reset to config value if options are empty")
	public void evaluateOption_verifyQueueToDefault() {

		evaluateOption_verifyExchangeAndQueueOverride();

		ServiceObject serviceObject = new ServiceObject().withOption("EXCHANGE:fakeExchange");

		RabbitMqInterface.evaluateOption(serviceObject);

		String exchange = Config.getValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE);
		String queue = Config.getValue(RabbitMqInterface.RABBIT_MQ_QUEUE);

		Helper.assertEquals("fakeExchange", exchange);
		Helper.assertEquals("realQueue", queue);
	}

	@Test()
	public void evaluateRequestHeader() {
		ServiceObject serviceObject = new ServiceObject().withRequestHeaders("breadcrumbs:value; breadcrumbs2:value2");

		RabbitMqInterface.evaluateRequestHeaders(serviceObject);
	}
}