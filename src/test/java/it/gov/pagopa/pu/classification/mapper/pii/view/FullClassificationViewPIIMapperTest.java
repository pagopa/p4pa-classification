package it.gov.pagopa.pu.classification.mapper.pii.view;

import it.gov.pagopa.pu.classification.dto.FullClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.classification.FullClassificationViewNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.common.pii.mapper.Base2PIIMapperTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FullClassificationViewPIIMapperTest extends Base2PIIMapperTest<FullClassificationViewDTO, FullClassificationViewNoPII, ReceiptPIIDTO, PaymentNotificationPIIDTO> {

  private FullClassificationViewPIIMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new FullClassificationViewPIIMapper(personalDataServiceMock);
  }

  @Override
  public FullClassificationViewPIIMapper getMapper() {
    return mapper;
  }

  @Test
  void testMapper() {
    // Given
    FullClassificationViewNoPII fullClassificationViewNoPII = podamFactory.manufacturePojo(FullClassificationViewNoPII.class);
    ReceiptPIIDTO receiptPIIDTO = podamFactory.manufacturePojo(ReceiptPIIDTO.class);
    PaymentNotificationPIIDTO paymentNotificationPIIDTO = podamFactory.manufacturePojo(PaymentNotificationPIIDTO.class);

    when(personalDataServiceMock.get(fullClassificationViewNoPII.getReceiptPersonalDataId(), ReceiptPIIDTO.class))
      .thenReturn(receiptPIIDTO);
    when(personalDataServiceMock.get(fullClassificationViewNoPII.getPaymentNotificationPersonalDataId(), PaymentNotificationPIIDTO.class))
      .thenReturn(paymentNotificationPIIDTO);

    // When
    FullClassificationViewDTO result = mapper.map(fullClassificationViewNoPII);

    // Then
    assertNotNull(result);
    TestUtils.reflectionEqualsByName(fullClassificationViewNoPII, result);
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNullPersonalDataIdWhenMapThenOk() {
    FullClassificationViewNoPII fullClassificationViewNoPII = podamFactory.manufacturePojo(FullClassificationViewNoPII.class);
    PaymentNotificationPIIDTO paymentNotificationPIIDTO = podamFactory.manufacturePojo(PaymentNotificationPIIDTO.class);

    when(personalDataServiceMock.get(fullClassificationViewNoPII.getReceiptPersonalDataId(), ReceiptPIIDTO.class))
      .thenReturn(null);

    when(personalDataServiceMock.get(fullClassificationViewNoPII.getPaymentNotificationPersonalDataId(), PaymentNotificationPIIDTO.class))
      .thenReturn(paymentNotificationPIIDTO);

    FullClassificationViewDTO result = mapper.map(fullClassificationViewNoPII);

    assertNotNull(result);
    TestUtils.reflectionEqualsByName(fullClassificationViewNoPII, result);
    TestUtils.checkNotNullFields(result, "receiptDebtor", "receiptPayer");
  }
}
