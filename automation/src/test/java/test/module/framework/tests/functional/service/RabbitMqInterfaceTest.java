package test.module.framework.tests.functional.service;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
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
		TestObject.getDefaultTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "realExchange");
		TestObject.getDefaultTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "realQueue");
	}
	
	
	//@Ignore // requires real rabbitmq connection
	@Test(description = "verify kafka interface with text message")
	public void evaluateRabbitMqInterface_text_message() throws Exception {	
		
		// reset values
		TestObject.getDefaultTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "");
		
		TestObject.getDefaultTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_HOST, "localhost");
		TestObject.getDefaultTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "hello");

		String random = Helper.generateRandomString(3);
				ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("test from autonomx" + random)
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY.RESPONSE.BODY_ contains("+ random + ")");
				
		RabbitMqInterface.connectRabbitMq(serviceObject);
		RabbitMqInterface.channel.queueDeclare("hello", true, false, false, null);
				
		RabbitMqInterface.testRabbitMqInterface(serviceObject);
	}
	
	@Test(description = "verify exchange and queue values in options will override config values")
	public void evaluateOption_verifyExchangeAndQueueOverride() {	
		Config.putValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "realExchange");
		Config.putValue(RabbitMqInterface.RABBIT_MQ_QUEUE, "realQueue");

		
		TestObject.getDefaultTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "realExchange");
		TestObject.getDefaultTestInfo().config.put(RabbitMqInterface.RABBIT_MQ_QUEUE, "realQueue");

		
		ServiceObject serviceObject = new ServiceObject()
				.withOption("EXCHANGE:fakeExchange; QUEUE:fakeQueue");
		
		RabbitMqInterface.evaluateOption(serviceObject);
		
		String exchange = Config.getValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE);
		String queue = Config.getValue(RabbitMqInterface.RABBIT_MQ_QUEUE);
		
		Helper.assertEquals("fakeExchange", exchange);
		Helper.assertEquals("fakeQueue", queue);
	}
	
	@Test(description = "verify exchange and queue values will reset to config value if options are empty")
	public void evaluateOption_verifyExchangeAndQueueResetToDefault() {	
		
		evaluateOption_verifyExchangeAndQueueOverride();
		
		ServiceObject serviceObject = new ServiceObject()
				.withOption("");
		
		RabbitMqInterface.evaluateOption(serviceObject);
		
		String exchange = Config.getValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE);
		String queue = Config.getValue(RabbitMqInterface.RABBIT_MQ_QUEUE);
		
		Helper.assertEquals("realExchange", exchange);
		Helper.assertEquals("realQueue", queue);
	}
	
	@Test(description = "verify exchange values will reset to config value if options are empty")
	public void evaluateOption_verifyExchangeToDefault() {	
		
		evaluateOption_verifyExchangeAndQueueOverride();
		
		ServiceObject serviceObject = new ServiceObject()
				.withOption("QUEUE:fakeQueue");
		
		RabbitMqInterface.evaluateOption(serviceObject);
		
		String exchange = Config.getValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE);
		String queue = Config.getValue(RabbitMqInterface.RABBIT_MQ_QUEUE);
		
		Helper.assertEquals("realExchange", exchange);
		Helper.assertEquals("fakeQueue", queue);
	}
	
	@Test(description = "verify queue values will reset to config value if options are empty")
	public void evaluateOption_verifyQueueToDefault() {	
		
		evaluateOption_verifyExchangeAndQueueOverride();
		
		ServiceObject serviceObject = new ServiceObject()
				.withOption("EXCHANGE:fakeExchange");
		
		RabbitMqInterface.evaluateOption(serviceObject);
		
		String exchange = Config.getValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE);
		String queue = Config.getValue(RabbitMqInterface.RABBIT_MQ_QUEUE);
		
		Helper.assertEquals("fakeExchange", exchange);
		Helper.assertEquals("realQueue", queue);
	}
	
	@Test()
	public void evaluateRequestHeader() {	
		ServiceObject serviceObject = new ServiceObject()
				.withRequestHeaders("breadcrumbs:value; breadcrumbs2:value2");
		
		RabbitMqInterface.evaluateRequestHeaders(serviceObject);
	}
	
	@Ignore @Test()
	public void rabbitMQ_connect() throws Exception {	
		Config.putValue(RabbitMqInterface.RABBIT_MQ_HOST, "dev-eastus.trafficmanager.net");
		Config.putValue(RabbitMqInterface.RABBIT_MQ_PORT, "15692");
		Config.putValue(RabbitMqInterface.RABBIT_MQ_USER, "admin");
		Config.putValue(RabbitMqInterface.RABBIT_MQ_PASS, "EYttTGnhFT4hjwTzQtV4p3Xj38Wiv4rp");	
		Config.putValue(RabbitMqInterface.RABBIT_MQ_EXCHANGE, "eam.exchange");
		Config.putValue(RabbitMqInterface.RABBIT_MQ_QUEUE, "eam.ingestion.assets");
		Config.putValue(RabbitMqInterface.RABBIT_MQ_VIRTUAL_HOST, "qa1b");
		ServiceObject serviceObject = new ServiceObject().withTemplateFile("Defects.xml");	
		RabbitMqInterface.testRabbitMqInterface(serviceObject);
	}
}