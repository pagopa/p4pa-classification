package it.gov.pagopa.pu.classification.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PaymentsReportingTest {

  @Test
  void testSemanticIdGeneration() {
    // Given
    PaymentsReporting p = new PaymentsReporting();

    Assertions.assertNull(p.getPaymentsReportingId());

    p.setIuf("IUF");
    assertSemanticId("IUF_null_null_null", p);

    p.setIuv("IUV");
    assertSemanticId("IUF_IUV_null_null", p);

    p.setTransferIndex(1);
    assertSemanticId("IUF_IUV_1_null", p);

    p.setOrganizationId(0L);
    assertSemanticId("IUF_IUV_1_0", p);
  }

  private static void assertSemanticId(String expected, PaymentsReporting p) {
    Assertions.assertEquals(expected, p.getPaymentsReportingId());
  }

}
