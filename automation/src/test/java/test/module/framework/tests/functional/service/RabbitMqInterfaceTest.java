package test.module.framework.tests.functional.service;


import org.testng.annotations.BeforeClass;
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
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
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

	
	
}