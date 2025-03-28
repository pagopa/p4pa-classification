package it.gov.pagopa.pu.classification.util;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UtilitiesTest {

  @Test
  void testBigDecimalEuroToLongCentsAmount(){
    // Given
    BigDecimal amount = BigDecimal.valueOf(123.45);
    // When
    long result = Utilities.bigDecimalEuroToLongCentsAmount(amount);
    // Then
    assertEquals(12345, result);
  }
  @Test
  void testBigDecimalEuroToLongCentsAmountNull(){
    // When
    Long result = Utilities.bigDecimalEuroToLongCentsAmount(null);
    // Then
    assertNull(result);
  }

}
