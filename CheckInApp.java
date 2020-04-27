package com.ram.jms.pointtopoint;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class CheckInApp {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueuersv");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext context = cf.createContext();) {
			JMSProducer createProducer = context.createProducer();
			Passenger passenger = new Passenger();
			for (int i = 0; i < 100; i++) {
				passenger.setId(i);
				passenger.setFirstName("Ram" + i);
				passenger.setLastName("Ram" + i);
				passenger.setEmail("trmreddy" + i + "@gmail.com");
				passenger.setPhone("9900732" + i);
				createProducer.send(requestQueue, passenger);
			}
			System.out.println("Posted the object");
			Queue responseQueue = (Queue) initialContext.lookup("queue/responseQueuersv");
			JMSConsumer createConsumer = context.createConsumer(responseQueue);
			TextMessage receive = null;

			for (int i = 0; i < 100; i++) {
				receive = (TextMessage) createConsumer.receive(3000);
				System.out.println(receive.getText());
			}

		}
	}

}
