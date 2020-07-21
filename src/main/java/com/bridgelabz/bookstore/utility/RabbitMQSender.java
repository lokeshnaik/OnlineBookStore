package com.bridgelabz.bookstore.utility;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bridgelabz.bookstore.response.EmailObject;

@Component
public class RabbitMQSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("rmq.rube.exchange")
	private String exchange;

	@Value("rube.key")
	private String routingkey;

	public boolean send(EmailObject message) {
		rabbitTemplate.convertAndSend(exchange, routingkey, message);
		return true;
	}
}
