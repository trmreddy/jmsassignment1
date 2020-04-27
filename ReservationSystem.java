package com.ram.jms.pointtopoint;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class ReservationSystem {

	public static void main(String[] args) throws NamingException, JMSException {
		InitialContext initialContext = new InitialContext();
		Queue requestQueue = (Queue) initialContext.lookup("queue/requestQueuersv");
		try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory(); JMSContext context = cf.createContext();) {
			JMSConsumer jmsConsumer = context.createConsumer(requestQueue);
			JMSConsumer jmsConsumerTwo = context.createConsumer(requestQueue);
			Passenger p1 = null;
			Passenger p2 = null;
			ObjectMessage obmsg1 = null;
			ObjectMessage obmsg2 = null;
			Queue responseQueue = (Queue) initialContext.lookup("queue/responseQueuersv");
			JMSProducer producer = context.createProducer();
			for (int i = 0; i <= 100; i += 2) {
				obmsg1 = (ObjectMessage) jmsConsumer.receive();
				p1 = (Passenger) obmsg1.getObject();
				obmsg2 = (ObjectMessage) jmsConsumerTwo.receive();
				p2 = (Passenger) obmsg2.getObject();
				System.out.println(p1.getFirstName());
				producer.send(responseQueue, "Reservation completed for " + p1.getFirstName());
				System.out.println(p2.getFirstName());
				producer.send(responseQueue, "Reservation not completed for " + p2.getFirstName());
			}

		}

	}

}
