package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.citizen.service.DataCipherService;
import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import org.junit.jupiter.api.AfterEach;
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
class PaymentNotificationPIIMapperTest {

  private PaymentNotificationPIIMapper mapper;

  @Mock
  private DataCipherService dataCipherServiceMock;

  @Mock
  private PersonalDataService personalDataServiceMock;


  @BeforeEach
  void init() {
    mapper = new PaymentNotificationPIIMapper(personalDataServiceMock, dataCipherServiceMock);
  }

  @AfterEach
  void verifyNotMoreInvocation() {
    Mockito.verifyNoMoreInteractions(dataCipherServiceMock);
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

}
