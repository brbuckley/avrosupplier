package avrosupplier.api.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import avrosupplier.SupplierResponseUtil;
import avrosupplier.service.AdapterService;
import avrosupplier.util.Config;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = {Config.class})
public class RabbitControllerIntegrationTest {

  private RabbitController underTest;

  private static final String LISTENER_CONTAINER_ID = "adapter";

  @Autowired private RabbitListenerTestHarness harness;
  @Autowired private TestRabbitTemplate testRabbitTemplate;

  @Mock(answer = RETURNS_DEEP_STUBS)
  private Message message;

  @MockBean private AdapterService adapterService;

  @BeforeEach
  void setUp() {
    underTest = harness.getSpy(LISTENER_CONTAINER_ID);
    assertNotNull(underTest);
  }

  @AfterEach
  void tearDown() {
    reset(underTest);
  }

  @Test
  void givenBlankApplicationEvent_handle_throwsMessageConversionExceptionToAvoidRequeue()
      throws IOException {
    given(adapterService.parseOrder("correlation", SupplierResponseUtil.defaultPayload()))
        .willReturn(SupplierResponseUtil.defaultPurchaseOrder());

    MessageProperties mock = Mockito.mock(MessageProperties.class);
    when(message.getMessageProperties()).thenReturn(mock);
    when(mock.getHeader("X-Correlation-Id")).thenReturn("correlation");
    when(message.getBody()).thenReturn(SupplierResponseUtil.defaultPayload());

    testRabbitTemplate.convertAndSend("adapter-supplierc", "adapter-supplierc", message);

    verify(adapterService)
        .sendToAdapter("correlation", SupplierResponseUtil.defaultPurchaseOrder());
  }
}
