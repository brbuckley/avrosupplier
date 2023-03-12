package avrosupplier.service;

import avrosupplier.avromapper.AvroMapper;
import avrosupplier.configuration.RabbitConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.liquorstore.purchase.PurchaseOrder;
import com.liquorstore.supplier.SupplierOrder;
import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/** Adapter services. */
@AllArgsConstructor
@Service
@Slf4j
public class AdapterService {

  private RabbitTemplate rabbitTemplate;
  private AvroMapper avroMapper;

  /**
   * Parses the message received into a PurchaseOrder Request.
   *
   * @param correlation Correlation id.
   * @param message Message.
   * @return PurchaseOrder Request.
   */
  public PurchaseOrder parseOrder(String correlation, byte[] message) {
    log.info("Message received with Correlation: {} | Body: {}", correlation, message);
    try {
      PurchaseOrder purchaseOrder = avroMapper.byteArrayToPurchaseOrder(message);
      log.info("Successfully parsed the message into the object: {}", purchaseOrder);
      return purchaseOrder;
    } catch (Exception e) {
      log.error("Poisoned message! error: {}", e.getMessage());
      throw new AmqpRejectAndDontRequeueException("Poisoned Message");
    }
  }

  /**
   * Send a message containing a Supplier Response to the adapter.
   *
   * @param correlationId Correlation id.
   * @param purchase PurchaseOrder Request.
   * @return SupplierOrder Response.
   * @throws JsonProcessingException Json Processing Exception.
   */
  public SupplierOrder sendToAdapter(String correlationId, PurchaseOrder purchase)
      throws IOException {
    SupplierOrder supplierOrder =
        new SupplierOrder("SUC" + purchase.getId().subSequence(3, 10), purchase.getId());
    GenericRecord record = (GenericRecord) supplierOrder;
    byte[] data = avroMapper.objectToByteArray(record);
    log.info("Sending message with id: {} & body: {}", correlationId, data);
    Message message =
        MessageBuilder.withBody(data)
            .setHeader("X-Correlation-Id", correlationId)
            .setHeader("Avro-Schema", record.getSchema().toString())
            .build();
    rabbitTemplate.send(RabbitConfiguration.ADAPTER_QUEUE, message);
    return supplierOrder;
  }
}
