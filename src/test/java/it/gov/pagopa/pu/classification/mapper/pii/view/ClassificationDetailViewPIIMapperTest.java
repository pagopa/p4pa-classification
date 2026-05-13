package it.gov.pagopa.pu.classification.mapper.pii.view;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationDetailViewNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.common.pii.mapper.Base2PIIMapperTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationDetailViewPIIMapperTest extends Base2PIIMapperTest<ClassificationDetailViewDTO, ClassificationDetailViewNoPII, ReceiptPIIDTO, PaymentNotificationPIIDTO> {

  private ClassificationDetailViewPIIMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ClassificationDetailViewPIIMapper(personalDataServiceMock);
  }

  @Override
  public ClassificationDetailViewPIIMapper getMapper() {
    return mapper;
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

  @Test
  void testMapperWithNullPaymentsReportingFields() {
    ClassificationDetailViewNoPII classificationDetailViewNoPII =
      podamFactory.manufacturePojo(ClassificationDetailViewNoPII.class);

    classificationDetailViewNoPII.setPspIdentifier(null);
    classificationDetailViewNoPII.setFlowDateTime(null);
    classificationDetailViewNoPII.setSenderPspType(null);
    classificationDetailViewNoPII.setSenderPspCode(null);
    classificationDetailViewNoPII.setSenderPspName(null);
    classificationDetailViewNoPII.setReceiverOrganizationType(null);
    classificationDetailViewNoPII.setReceiverOrganizationCode(null);
    classificationDetailViewNoPII.setReceiverOrganizationName(null);
    classificationDetailViewNoPII.setTotalPayments(null);
    classificationDetailViewNoPII.setTotalAmountCents(null);
    classificationDetailViewNoPII.setAmountPaidCents(null);
    classificationDetailViewNoPII.setPaymentOutcomeCode(null);
    classificationDetailViewNoPII.setAcquiringDate(null);
    classificationDetailViewNoPII.setBicCodePouringBank(null);

    ReceiptPIIDTO receiptPIIDTO = podamFactory.manufacturePojo(ReceiptPIIDTO.class);

    PaymentNotificationPIIDTO paymentNotificationPIIDTO =  podamFactory.manufacturePojo(PaymentNotificationPIIDTO.class);

    when(personalDataServiceMock.get(classificationDetailViewNoPII.getReceiptPersonalDataId(), ReceiptPIIDTO.class)).thenReturn(receiptPIIDTO);

    when(personalDataServiceMock.get(classificationDetailViewNoPII.getPaymentNotificationPersonalDataId(), PaymentNotificationPIIDTO.class)).thenReturn(paymentNotificationPIIDTO);

    ClassificationDetailViewDTO result = mapper.map(classificationDetailViewNoPII);

    assertNotNull(result);

    assertNull(result.getPspIdentifier());
    assertNull(result.getFlowDateTime());
    assertNull(result.getSenderPspType());
    assertNull(result.getSenderPspCode());
    assertNull(result.getSenderPspName());
    assertNull(result.getReceiverOrganizationType());
    assertNull(result.getReceiverOrganizationCode());
    assertNull(result.getReceiverOrganizationName());
    assertNull(result.getTotalPayments());
    assertNull(result.getTotalAmountCents());
    assertNull(result.getAmountPaidCents());
    assertNull(result.getPaymentOutcomeCode());
    assertNull(result.getAcquiringDate());
    assertNull(result.getBicCodePouringBank());
  }
}
