package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.config.json.JsonConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

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

  @Test
  void testDeserialization() {
    // Given
    String json = """
      {
        "organizationId": 1,
        "billCode": "BILLCODE",
        "billYear": "BILLYEAR",
        "orgIstatCode": "ISTATCODE",
        "orgBtCode": "BTCODE"
      }
      """;

    JsonMapper mapper = new JsonConfig().objectMapperJackson3();

    // When
    Treasury result = mapper.readValue(json, Treasury.class);

    // Then
    Assertions.assertEquals("BILLCODE-BILLYEAR-ISTATCODE-BTCODE-1", result.getTreasuryId());
  }

}
