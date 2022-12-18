package emlakcepte.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmlakCepteEmailQueue {

	private String queueName = "emlakcepte.notification.email";

	private String exchange = "emlakcepte.notification.email";

	@Bean
	public Queue emailQueue() {
		return new Queue(queueName, false);
	}

	@Bean
	public DirectExchange emailExchange() {
		return new DirectExchange(exchange);
	}

	@Bean
	public Binding binding(Queue emailQueue, DirectExchange emailExchange) {
		return BindingBuilder.bind(emailQueue).to(emailExchange).with("");
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

}
