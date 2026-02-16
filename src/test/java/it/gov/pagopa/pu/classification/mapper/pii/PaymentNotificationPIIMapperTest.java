package it.gov.pagopa.pu.classification.mapper.pii;

import it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import it.gov.pagopa.pu.common.pii.citizen.service.DataCipherService;
import it.gov.pagopa.pu.common.pii.mapper.BasePIIMapperTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import static it.gov.pagopa.pu.classification.util.TestUtils.checkNotNullFields;
import static it.gov.pagopa.pu.classification.util.TestUtils.reflectionEqualsByName;
import static it.gov.pagopa.pu.classification.util.faker.PaymentNotificationFaker.*;

@ExtendWith(MockitoExtension.class)
class PaymentNotificationPIIMapperTest extends BasePIIMapperTest<PaymentNotificationDTO, PaymentNotificationNoPII, PaymentNotificationPIIDTO> {

  @Mock
  private DataCipherService dataCipherServiceMock;

  private PaymentNotificationPIIMapper mapper;


  @BeforeEach
  void init() {
    mapper = new PaymentNotificationPIIMapper(personalDataServiceMock, dataCipherServiceMock);
  }

  @AfterEach
  void verifyNotMoreInvocation() {
    Mockito.verifyNoMoreInteractions(dataCipherServiceMock);
  }

  @Override
  public PaymentNotificationPIIMapper getMapper() {
    return mapper;
  }

  @Test
  void map_success() {
    PaymentNotificationNoPII paymentNotificationNoPIIExpected =buildPaymentNotificationNoPII();
    PaymentNotificationPIIDTO paymentNotificationPIIDTOExpected = buildPaymentNotificationPIIDTO();

    PaymentNotificationDTO paymentNotificationDTO = buildPaymentNotification();
    byte[] expectedHashedCF = "debtorFiscalCodeHash".getBytes();
    byte[] expectedHashedRemInfo = "remittanceInformationHash".getBytes();
    Mockito.when(dataCipherServiceMock.hash(paymentNotificationDTO.getDebtor().getFiscalCode())).thenReturn(expectedHashedCF);
    Mockito.when(dataCipherServiceMock.hash(paymentNotificationDTO.getRemittanceInformation())).thenReturn(expectedHashedRemInfo);

    Pair<PaymentNotificationNoPII, PaymentNotificationPIIDTO> result = mapper.map(paymentNotificationDTO);

    reflectionEqualsByName(paymentNotificationNoPIIExpected, result.getFirst());
    reflectionEqualsByName(paymentNotificationPIIDTOExpected, result.getSecond());
    checkNotNullFields(result.getFirst(), "personalDataId");
    checkNotNullFields(result.getSecond());
  }

  @Test
  void mapNoPii_success() {
    PaymentNotificationPIIDTO paymentNotification = buildPaymentNotificationPIIDTO();
    PaymentNotificationDTO paymentNotificationDTO = buildPaymentNotification();
    PaymentNotificationNoPII paymentNotificationNoPII = buildPaymentNotificationNoPII();
    Mockito.when(personalDataServiceMock.get(1L,PaymentNotificationPIIDTO.class)).thenReturn(paymentNotification);

    PaymentNotificationDTO result = mapper.map(paymentNotificationNoPII);

    reflectionEqualsByName(paymentNotificationDTO, result);
    checkNotNullFields(result);
  }

  @Test
  void extractNoPiiEntity_success() {
    // Given
    PaymentNotificationDTO paymentNotificationDTO = buildPaymentNotification();
    byte[] expectedHashedCF = "debtorFiscalCodeHash".getBytes();
    byte[] expectedHashedRemInfo = "remittanceInformationHash".getBytes();
    Mockito.when(dataCipherServiceMock.hash(paymentNotificationDTO.getDebtor().getFiscalCode())).thenReturn(expectedHashedCF);
    Mockito.when(dataCipherServiceMock.hash(paymentNotificationDTO.getRemittanceInformation())).thenReturn(expectedHashedRemInfo);

    // When
    PaymentNotificationNoPII result = mapper.extractNoPiiEntity(paymentNotificationDTO);

    // Then
    Assertions.assertEquals(expectedHashedCF, result.getDebtorFiscalCodeHash());
    Assertions.assertEquals(expectedHashedRemInfo, result.getRemittanceInformationHash());
    Assertions.assertEquals(paymentNotificationDTO.getOrganizationId(), result.getOrganizationId());
    checkNotNullFields(result,"personalDataId");
  }

  @Test
  void extractPiiDto_success() {
    // Given
    PaymentNotificationDTO paymentNotificationDTO = buildPaymentNotification();

    // When
    PaymentNotificationPIIDTO result = mapper.extractPiiDto(paymentNotificationDTO);

    // Then
    Assertions.assertEquals(paymentNotificationDTO.getDebtor(), result.getDebtor());
    checkNotNullFields(result);
  }
}
