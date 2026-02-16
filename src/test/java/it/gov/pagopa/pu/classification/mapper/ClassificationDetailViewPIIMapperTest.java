package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.common.pii.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.ClassificationDetailViewNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationDetailViewPIIMapperTest {
  @Mock
  private PersonalDataService personalDataServiceMock;

  private ClassificationDetailViewPIIMapper mapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new ClassificationDetailViewPIIMapper(personalDataServiceMock);
  }

  @Test
  void testMapperWithAllPersonalData() {
    ClassificationDetailViewNoPII classificationDetailViewNoPII = podamFactory.manufacturePojo(ClassificationDetailViewNoPII.class);
    ReceiptPIIDTO receiptPIIDTO = podamFactory.manufacturePojo(ReceiptPIIDTO.class);
    PaymentNotificationPIIDTO paymentNotificationPIIDTO = podamFactory.manufacturePojo(PaymentNotificationPIIDTO.class);

    when(personalDataServiceMock.get(classificationDetailViewNoPII.getReceiptPersonalDataId(), ReceiptPIIDTO.class))
      .thenReturn(receiptPIIDTO);
    when(personalDataServiceMock.get(classificationDetailViewNoPII.getPaymentNotificationPersonalDataId(), PaymentNotificationPIIDTO.class))
      .thenReturn(paymentNotificationPIIDTO);

    ClassificationDetailViewDTO result = mapper.map(classificationDetailViewNoPII);

    assertNotNull(result);
    TestUtils.reflectionEqualsByName(classificationDetailViewNoPII, result);
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void testMapperWithNullPaymentNotificationPersonalDataId() {
    ClassificationDetailViewNoPII classificationDetailViewNoPII = podamFactory.manufacturePojo(ClassificationDetailViewNoPII.class);
    classificationDetailViewNoPII.setPaymentNotificationPersonalDataId(null);
    ReceiptPIIDTO receiptPIIDTO = podamFactory.manufacturePojo(ReceiptPIIDTO.class);

    when(personalDataServiceMock.get(classificationDetailViewNoPII.getReceiptPersonalDataId(), ReceiptPIIDTO.class))
      .thenReturn(receiptPIIDTO);

    ClassificationDetailViewDTO result = mapper.map(classificationDetailViewNoPII);

    assertNotNull(result);
    assertEquals(receiptPIIDTO.getDebtor(), result.getReceiptDebtor());
    assertEquals(receiptPIIDTO.getPayer(), result.getReceiptPayer());
    assertNull(result.getPaymentNotificationDebtor());
  }

  @Test
  void testMapperWithNullReceiptPersonalDataId() {
    ClassificationDetailViewNoPII classificationDetailViewNoPII = podamFactory.manufacturePojo(ClassificationDetailViewNoPII.class);
    classificationDetailViewNoPII.setReceiptPersonalDataId(null);
    PaymentNotificationPIIDTO paymentNotificationPIIDTO = podamFactory.manufacturePojo(PaymentNotificationPIIDTO.class);

    when(personalDataServiceMock.get(classificationDetailViewNoPII.getPaymentNotificationPersonalDataId(), PaymentNotificationPIIDTO.class))
      .thenReturn(paymentNotificationPIIDTO);

    ClassificationDetailViewDTO result = mapper.map(classificationDetailViewNoPII);

    assertNotNull(result);
    assertNull(result.getReceiptDebtor());
    assertNull(result.getReceiptPayer());
    assertEquals(paymentNotificationPIIDTO.getDebtor(), result.getPaymentNotificationDebtor());
  }
}
