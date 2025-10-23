package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.FullClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.FullClassificationViewNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FullClassificationViewPIIMapperTest {
  @Mock
  private PersonalDataService personalDataServiceMock;

  private FullClassificationViewPIIMapper mapper;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new FullClassificationViewPIIMapper(personalDataServiceMock);
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
