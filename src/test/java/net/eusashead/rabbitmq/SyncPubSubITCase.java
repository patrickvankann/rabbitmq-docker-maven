package net.eusashead.rabbitmq;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class SyncPubSubITCase {

	// Address of RabbitMQ docker host
	private String brokerHostAddress; 
	
	private static final String CLIENT_ID = "test-mqtt-client";
	private static final String TOPIC = "test-mqtt-topic";
	
	@Before
	public void before() {
		// Get broker host address from system properties
		this.brokerHostAddress = System.getProperty("brokerHostAddress", 
				"192.168.99.100");
	}

	@Test
	public void itCanPubAndSubToWebsocketBroker() throws Exception {
		itCanPubAndSubToBroker("ws://" + brokerHostAddress + ":15675/ws");
	}
	
	@Test
	public void itCanPubAndSubToTcpBroker() throws Exception {
		itCanPubAndSubToBroker("tcp://" + brokerHostAddress + ":1883");
	}
	
	private void itCanPubAndSubToBroker(final String brokerUrl) throws MqttPersistenceException, MqttException, InterruptedException {

		// Create synch MQTT clients
		final MqttClient pubClient = new MqttClient(brokerUrl, CLIENT_ID + "-pub");
		pubClient.connect();

		final MqttClient subClient = new MqttClient(brokerUrl, CLIENT_ID + "-sub");
		subClient.connect();

		final CountDownLatch latch = new CountDownLatch(1);

		final AtomicReference<MqttMessage> msg = new AtomicReference<MqttMessage>();

		// Subscribe and set callback
		subClient.subscribe(TOPIC);
		subClient.setCallback(new MqttCallback() {

			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				msg.set(message);
				latch.countDown();
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken token) {
				latch.countDown();
			}

			@Override
			public void connectionLost(Throwable cause) {
				latch.countDown();
			}
		});

		// Publish the test data
		final MqttTopic pubTopic = pubClient.getTopic(TOPIC);
		final byte[] expectedPayload = new byte[]{'a', 'b', 'c'};
		pubTopic.publish(new MqttMessage(expectedPayload));

		// Await message receipt
		latch.await();

		// Get the message received by the callback
		final MqttMessage receivedMessage = msg.get();
		Assert.assertNotNull(receivedMessage);
		Assert.assertNotNull(receivedMessage.getPayload());
		Assert.assertArrayEquals(expectedPayload, receivedMessage.getPayload());

		// Close the clients 
		pubClient.disconnect();
		subClient.disconnect();

	}

}
