package it.gov.pagopa.pu.classification.config.semanticids;

import it.gov.pagopa.pu.classification.model.Treasury;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TreasurySemanticIdGeneratorStrategyTest {

  private final TreasurySemanticIdGenerator.TreasurySemanticIdGeneratorStrategy strategy = new TreasurySemanticIdGenerator.TreasurySemanticIdGeneratorStrategy();

  @Test
  void givenNoTreasuryEntityWhenGenerateThenIllegalStateException() {
    Assertions.assertThrows(IllegalStateException.class, () -> strategy.generate(null, "", null, null));
  }

  @Test
  void givenTreasuryWithIdWhenGenerateThenReturnPreviousId() {
    // Given
    Treasury t = Treasury.builder().treasuryId("ID").build();

    // When
    Object result = strategy.generate(null, t, null, null);

    // Then
    Assertions.assertSame(t.getTreasuryId(), result);
  }

  @Test
  void givenTreasuryWithoutIdWhenGenerateThenReturnNewId() {
    // Given
    Treasury t = Treasury.builder()
      .organizationId(1L)
      .billCode("BILLCODE")
      .billYear("BILLYEAR")
      .build();

    // When
    Object result = strategy.generate(null, t, null, null);

    // Then
    Assertions.assertEquals("BILLCODE-BILLYEAR-1", result);
  }
}
