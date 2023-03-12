package avrosupplier.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;

import avrosupplier.SupplierResponseUtil;
import avrosupplier.avromapper.AvroMapper;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class AdapterServiceTest {

  AdapterService service;

  @Test
  public void testParseOrder_whenValid_thenReturnRequest() throws IOException {
    service = new AdapterService(null, new AvroMapper());
    assertEquals(
        "PUR0000001",
        service
            .parseOrder("correlation", SupplierResponseUtil.defaultPayload())
            .getId()
            .toString());
  }

  @Test
  public void testParseOrder_whenInvalid_thenThrow() {
    service = new AdapterService(null, new AvroMapper());
    byte[] payload = null;
    assertThrows(
        AmqpRejectAndDontRequeueException.class, () -> service.parseOrder("correlation", payload));
  }

  @Test
  public void testSendToAdapter_whenValid_thenReturnResponse() throws IOException {
    // Mocks
    RabbitTemplate mockRabbit = Mockito.spy(RabbitTemplate.class);
    doNothing().when(mockRabbit).send(any(), anyString(), any(), any());

    service = new AdapterService(mockRabbit, new AvroMapper());
    assertEquals(
        "SUC0000001",
        service.sendToAdapter("correlation", SupplierResponseUtil.defaultPurchaseOrder()).getId());
  }
}
