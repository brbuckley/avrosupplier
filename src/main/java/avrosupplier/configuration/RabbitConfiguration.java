package avrosupplier.configuration;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;

/** Rabbit Configuration and queue declarations. */
public class RabbitConfiguration {

  public static final String ADAPTER_QUEUE = "supplierc-adapter";
  public static final String SUPPLIER_QUEUE = "adapter-supplierc";

  @Bean
  public CachingConnectionFactory connectionFactory() {
    // ToDo: change to rabbitmq host at pipeline
    return new CachingConnectionFactory("localhost");
  }

  @Bean
  public RabbitAdmin amqpAdmin() {
    return new RabbitAdmin(connectionFactory());
  }

  @Bean
  public RabbitTemplate rabbitTemplate() {
    return new RabbitTemplate(connectionFactory());
  }

  @Bean
  Queue adapterQueue() {
    return new Queue(ADAPTER_QUEUE, true);
  }

  @Bean
  Queue supplierQueue() {
    return new Queue(SUPPLIER_QUEUE, true);
  }
}
