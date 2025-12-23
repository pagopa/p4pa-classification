package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.config.json.JsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class PaymentNotificationNoPIITest {

  @Test
  void testSemanticIdGeneration() {
    // Given
    PaymentNotificationNoPII p = new PaymentNotificationNoPII();

    Assertions.assertNull(p.getPaymentNotificationId());

    p.setOrganizationId(1L);
    assertSemanticId("null_1", p);

    p.setIud("IUD");
    assertSemanticId("IUD_1", p);
  }

  private static void assertSemanticId(String expected, PaymentNotificationNoPII t) {
    Assertions.assertEquals(expected, t.getPaymentNotificationId());
  }

  @Test
  void testDeserialization() {
    // Given
    String json = """
      {
        "organizationId": 1,
        "iud": "IUD"
      }
      """;

    JsonMapper mapper = new JsonConfig().objectMapperJackson3();

    // When
    PaymentNotificationNoPII result = mapper.readValue(json, PaymentNotificationNoPII.class);

    // Then
    Assertions.assertEquals("IUD_1", result.getPaymentNotificationId());
  }

}
