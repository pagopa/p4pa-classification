package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.config.json.JsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class PaymentsReportingTest {

  @Test
  void testSemanticIdGeneration() {
    // Given
    PaymentsReporting p = new PaymentsReporting();

    Assertions.assertNull(p.getPaymentsReportingId());

    p.setIuf("IUF");
    assertSemanticId("IUF_null_null_null_null", p);

    p.setIuv("IUV");
    assertSemanticId("IUF_IUV_null_null_null", p);

    p.setTransferIndex(1);
    assertSemanticId("IUF_IUV_1_null_null", p);

    p.setOrganizationId(0L);
    assertSemanticId("IUF_IUV_1_0_null", p);

    p.setIur("IUR");
    assertSemanticId("IUF_IUV_1_0_IUR", p);
  }

  private static void assertSemanticId(String expected, PaymentsReporting p) {
    Assertions.assertEquals(expected, p.getPaymentsReportingId());
  }
  
  @Test
  void testDeserialization() {
    // Given
    String json = """
      {
        "iuf": "IUF",
        "iuv": "IUV",
        "transferIndex": 1,
        "organizationId": 0,
        "iur": "IUR"
      }
      """;

    JsonMapper mapper = new JsonConfig().objectMapperJackson3();

    // When
    PaymentsReporting result = mapper.readValue(json, PaymentsReporting.class);

    // Then
    Assertions.assertEquals("IUF_IUV_1_0_IUR", result.getPaymentsReportingId());
  }

}
