package avrosupplier.api.controller;

import avrosupplier.service.AdapterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.liquorstore.purchase.PurchaseOrder;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/** Rabbit Listeners, works like a controller. */
@AllArgsConstructor
@Component
@Slf4j
public class RabbitController {

  private AdapterService adapterService;

  /**
   * Listener method, works like a controller endpoint.
   *
   * @param message Message received.
   * @throws JsonProcessingException Json Processing Exception.
   */
  @RabbitListener(id = "adapter", queues = "adapter-supplierc")
  public void listen(Message message) throws IOException {
    String correlationId = message.getMessageProperties().getHeader("X-Correlation-Id");
    PurchaseOrder purchase = adapterService.parseOrder(correlationId, message.getBody());
    adapterService.sendToAdapter(correlationId, purchase);
  }
}
