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
    assertSemanticId("IUF/null/null/null", p);

    p.setIuv("IUV");
    assertSemanticId("IUF/IUV/null/null", p);

    p.setTransferIndex(1);
    assertSemanticId("IUF/IUV/1/null", p);

    p.setOrganizationId(0L);
    assertSemanticId("IUF/IUV/1/0", p);
  }

  private static void assertSemanticId(String expected, PaymentsReporting p) {
    Assertions.assertEquals(expected, p.getPaymentsReportingId());
  }

}
