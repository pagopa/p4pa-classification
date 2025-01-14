package it.gov.pagopa.pu.classification.config.semanticids;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PaymentsReportingSemanticIdGeneratorStrategyTest {

  private final PaymentsReportingSemanticIdGenerator.PaymentsReportingSemanticIdGeneratorStrategy strategy = new PaymentsReportingSemanticIdGenerator.PaymentsReportingSemanticIdGeneratorStrategy();

  @Test
  void givenNoPaymentsReportingEntityWhenGenerateThenIllegalStateException() {
    Assertions.assertThrows(IllegalStateException.class, () -> strategy.generate(null, "", null, null));
  }

  @Test
  void givenPaymentsReportingWithIdWhenGenerateThenReturnPreviousId() {
    // Given
    PaymentsReporting t = PaymentsReporting.builder().paymentsReportingId("ID").build();

    // When
    Object result = strategy.generate(null, t, null, null);

    // Then
    Assertions.assertSame(t.getPaymentsReportingId(), result);
  }

  @Test
  void givenPaymentsReportingWithoutIdWhenGenerateThenReturnNewId() {
    // Given
    PaymentsReporting t = PaymentsReporting.builder()
      .iuf("IUF")
      .iuv("IUV")
      .transferIndex(1)
      .organizationId(0L)
      .build();

    // When
    Object result = strategy.generate(null, t, null, null);

    // Then
    Assertions.assertEquals("IUF/IUV/1/0", result);
  }
}
