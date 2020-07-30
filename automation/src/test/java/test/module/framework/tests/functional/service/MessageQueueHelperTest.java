package test.module.framework.tests.functional.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.apiCore.ServiceManager;
import core.apiCore.helpers.MessageQueueHelper;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.MessageObject;
import core.support.objects.MessageObject.messageType;
import core.support.objects.ServiceObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class MessageQueueHelperTest extends TestBase {
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws Exception {
	}
	
	
	@Test(description = "")
	public void validateExpectedMessageCount_valid() throws Exception {	
		
		String request = "EXPECTED_MESSAGE_COUNT:1";
		List<String> filteredMessages = new ArrayList<String>();
		filteredMessages.add("message1");
		List<String> errorMessages = MessageQueueHelper.validateExpectedMessageCount(request, filteredMessages);
		Helper.assertTrue("errors caught", errorMessages.isEmpty());
	}
	
	@Test(description = "")
	public void validateExpectedMessageCount_Invalid_2Count() throws Exception {	
		
		String request = "EXPECTED_MESSAGE_COUNT:2";
		List<String> filteredMessages = new ArrayList<String>();
		filteredMessages.add("message1");
		List<String> errorMessages = MessageQueueHelper.validateExpectedMessageCount(request, filteredMessages);
		Helper.assertTrue("errors not caught", !errorMessages.isEmpty());
	}
	
	@Test(description = "")
	public void validateExpectedMessageCount_Invalid_2messages() throws Exception {	
		
		String request = "EXPECTED_MESSAGE_COUNT:1";
		List<String> filteredMessages = new ArrayList<String>();
		filteredMessages.add("message1");
		filteredMessages.add("message2");
		List<String> errorMessages = MessageQueueHelper.validateExpectedMessageCount(request, filteredMessages);
		Helper.assertTrue("errors not caught", !errorMessages.isEmpty());
	}
	
	@Test(description = "")
	public void validateExpectedMessageCount_valid_noRequest() throws Exception {	
		
		String request = "";
		List<String> filteredMessages = new ArrayList<String>();
		filteredMessages.add("message1");
		List<String> errorMessages = MessageQueueHelper.validateExpectedMessageCount(request, filteredMessages);
		Helper.assertTrue("errors caught", errorMessages.isEmpty());
	}
	
	@Test(description = "")
	public void validateExpectedMessageCount_valid_noRequest_multiple_messages() throws Exception {	
		
		String request = "";
		List<String> filteredMessages = new ArrayList<String>();
		filteredMessages.add("message1");
		filteredMessages.add("message2");
		List<String> errorMessages = MessageQueueHelper.validateExpectedMessageCount(request, filteredMessages);
		Helper.assertTrue("errors caught: " + Arrays.toString(errorMessages.toArray()) , errorMessages.isEmpty());
	}
	
	@Test(description = "")
	public void findMessagesBasedOnMessageId_valid() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);

		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random2);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, true);
		MessageObject.outboundMessages.put(message2, true);

		CopyOnWriteArrayList<MessageObject> filtered = MessageQueueHelper.findMessagesBasedOnMessageId("message" + random);
		Helper.assertEquals("test"+ random, filtered.get(0).message);
	}
	
	@Test(description = "")
	public void findMessagesBasedOnMessageId_invalid_not_available() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);

		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random2);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, false);
		MessageObject.outboundMessages.put(message2, true);

		CopyOnWriteArrayList<MessageObject> filtered = MessageQueueHelper.findMessagesBasedOnMessageId("message" + random);
		Helper.assertTrue("messages found", filtered.isEmpty());
	}
	
	@Test(description = "")
	public void findMessagesBasedOnMessageId_invalid_no_match() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);

		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random2);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message2, true);

		CopyOnWriteArrayList<MessageObject> filtered = MessageQueueHelper.findMessagesBasedOnMessageId("message" + random);
		Helper.assertTrue("messages found", filtered.isEmpty());
	}
	
	
	@Test(description = "")
	public void filterOutboundMessage_valid() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);

		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random2);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, true);
		MessageObject.outboundMessages.put(message2, true);

		CopyOnWriteArrayList<MessageObject> filtered = MessageQueueHelper.filterOutboundMessage("message" + random);
		Helper.assertEquals("test"+ random, filtered.get(0).message);
	}
	
	@Test(description = "")
	public void receiveAndValidateMessages_valid() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);

		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random2);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, true);
		MessageObject.outboundMessages.put(message2, true);

		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ random + ")");
		
		MessageQueueHelper.receiveAndValidateMessages(serviceObject, "message" + random,  messageType.TEST);
	}
	
	@Test(expectedExceptions = { AssertionError.class } , description = "")
	public void receiveAndValidateMessages_invalid_wrong_message_count() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);
		
		Config.putValue(ServiceManager.SERVICE_RESPONSE_TIMEOUT_SECONDS, 2);

		TestLog.When("I create 2 messages");
		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, true);
		MessageObject.outboundMessages.put(message2, true);

		TestLog.And("I validate with 1 expected message, and expect the test to fail");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ random + ")");
		
		MessageQueueHelper.receiveAndValidateMessages(serviceObject, "message" + random,  messageType.TEST);
	}
	
	@Test(expectedExceptions = { AssertionError.class } , description = "")
	public void receiveAndValidateMessages_invalid_wrong_message_correct_unavailable() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);
		
		Config.putValue(ServiceManager.SERVICE_RESPONSE_TIMEOUT_SECONDS, 1);

		TestLog.When("I create 2 messages");
		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, false);
		MessageObject.outboundMessages.put(message2, false);

		TestLog.And("I validate with 1 expected message, and expect the test to fail");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ random + ")");
		
		MessageQueueHelper.receiveAndValidateMessages(serviceObject, "message" + random,  messageType.TEST);	
	}
	
	@Test(expectedExceptions = { AssertionError.class } , description = "")
	public void receiveAndValidateMessages_invalid_wrong_message_both_unavailable() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);
		
		Config.putValue(ServiceManager.SERVICE_RESPONSE_TIMEOUT_SECONDS, 1);

		TestLog.When("I create 2 messages");
		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, false);
		MessageObject.outboundMessages.put(message2, false);

		TestLog.And("I validate with 1 expected message, and expect the test to fail");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ random + ")");
		
		MessageQueueHelper.receiveAndValidateMessages(serviceObject, "message" + random,  messageType.TEST);	
	}
	
	@Test(description = "")
	public void receiveAndValidateMessages_valid_multiple_response() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);
		
		Config.putValue(ServiceManager.SERVICE_RESPONSE_TIMEOUT_SECONDS, 1);

		TestLog.When("I create 2 messages");
		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, true);
		MessageObject.outboundMessages.put(message2, true);

		TestLog.And("I validate with 2 expected message, and expect the test to pass");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:2;"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ "test"+ random + ")"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ "test"+ random2 + ")");
		
		MessageQueueHelper.receiveAndValidateMessages(serviceObject, "message" + random,  messageType.TEST);	
	}
	
	@Test(expectedExceptions = { AssertionError.class } , description = "")
	public void receiveAndValidateMessages_valid_multiple_response_invalid() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);
		
		Config.putValue(ServiceManager.SERVICE_RESPONSE_TIMEOUT_SECONDS, 1);

		TestLog.When("I create 2 messages");
		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, true);
		MessageObject.outboundMessages.put(message2, true);

		TestLog.And("I validate with 2 expected message, and expect the test to pass");
		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:2;"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ "test"+ random + ")"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ "test"+ random2 + "3)");
		
		MessageQueueHelper.receiveAndValidateMessages(serviceObject, "message" + random,  messageType.TEST);	
	}	
	
	
	@Test(description = "")
	public void findMessagesBasedOnResponseIdentifier() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);
		String random3 = Helper.generateRandomString(12);

		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random );
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random2);
		message2.withMessage("test"+ random2 + " value" + random3);

		MessageObject.outboundMessages.put(message, true);
		MessageObject.outboundMessages.put(message2, true);
		
		Config.putValue(MessageQueueHelper.RESPONSE_IDENTIFIER, random2 + "," + random3);
		Config.setGlobalValue(MessageQueueHelper.RESPONSE_IDENTIFIER, random2 + "," + random3);
		
		CopyOnWriteArrayList<MessageObject> filteredMessages = MessageQueueHelper.findMessagesBasedOnResponseIdentifier();

		Helper.assertTrue("messages filtered are empty", !filteredMessages.isEmpty());
		Helper.assertEquals("test"+ random2 + " value" + random3, filteredMessages.get(0).message);
	}
	
	@Test(description = "")
	public void receiveAndValidateMessages_output() throws Exception {	
		String random = Helper.generateRandomString(10);
		String random2 = Helper.generateRandomString(11);

		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message" + random2);
		message2.withMessage("test"+ random2);

		MessageObject.outboundMessages.put(message, true);
		MessageObject.outboundMessages.put(message2, true);

		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:1;"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ random + ")")
				.withOutputParams("<$value1>");
		
		MessageQueueHelper.receiveAndValidateMessages(serviceObject, "message" + random,  messageType.TEST);
		String value = Config.getValue("value1");
		Helper.assertTrue("value is empty", !value.isEmpty());
		Helper.assertEquals("test"+ random, value);
	}
	
	@Test(description = "")
	public void receiveAndValidateMessages_output_multiple() throws Exception {	
		String random = Helper.generateRandomString(10);

		MessageObject message = new MessageObject().withMessageId("message" + random);
		message.withMessage("test"+ random);
		
		MessageObject message2 = new MessageObject().withMessageId("message2" + random);
		message2.withMessage("test2"+ random);

		MessageObject.outboundMessages.put(message, true);
		MessageObject.outboundMessages.put(message2, true);

		ServiceObject serviceObject = new ServiceObject()
				.withExpectedResponse("EXPECTED_MESSAGE_COUNT:2;"
						+ " && _VERIFY_RESPONSE_BODY_ contains("+ random + ")")
				.withOutputParams("<$value1>");
		
		MessageQueueHelper.receiveAndValidateMessages(serviceObject, random,  messageType.TEST);
		String value = Config.getValue("value1");
		Helper.assertTrue("value is empty", !value.isEmpty());
		Helper.assertContains(value, "test"+ random);
		Helper.assertContains(value, "test2"+ random);
	}
}