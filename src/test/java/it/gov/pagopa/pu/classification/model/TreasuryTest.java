package it.gov.pagopa.pu.classification.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TreasuryTest {

  @Test
  void testSemanticIdGeneration() {
    // Given
    Treasury t = new Treasury();

    Assertions.assertNull(t.getTreasuryId());

    t.setOrganizationId(1L);
    assertSemanticId("null-null-null-null-1", t);

    t.setBillCode("BILLCODE");
    assertSemanticId("BILLCODE-null-null-null-1", t);

    t.setBillYear("BILLYEAR");
    assertSemanticId("BILLCODE-BILLYEAR-null-null-1", t);

    t.setOrgIstatCode("ISTATCODE");
    assertSemanticId("BILLCODE-BILLYEAR-ISTATCODE-null-1", t);

    t.setOrgBtCode("BTCODE");
    assertSemanticId("BILLCODE-BILLYEAR-ISTATCODE-BTCODE-1", t);
  }

  private static void assertSemanticId(String expected, Treasury t) {
    Assertions.assertEquals(expected, t.getTreasuryId());
  }

}
