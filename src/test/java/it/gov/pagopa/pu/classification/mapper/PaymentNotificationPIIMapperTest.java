package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.citizen.service.DataCipherService;
import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.PaymentNotification;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationNoPIIDTO;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.Person;
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
import static it.gov.pagopa.pu.classification.util.faker.PersonFaker.buildPerson;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentNotificationPIIMapperTest {

  private PaymentNotificationPIIMapper mapper;

  @Mock
  private DataCipherService dataCipherServiceMock;

  @Mock
  private PersonalDataService personalDataServiceMock;

  @Mock
  private PersonMapper personMapperMock;

  @BeforeEach
  void init() {
    mapper = new PaymentNotificationPIIMapper(personalDataServiceMock, dataCipherServiceMock, personMapperMock);
  }

  @AfterEach
  void verifyNotMoreInvocation() {
    Mockito.verifyNoMoreInteractions(dataCipherServiceMock);
  }

  @Test
  void mapToModel_success() {
    PaymentNotificationDTO paymentNotificationDTO = buildPaymentNotificationDTO();

    Person debtor = buildPerson();

    when(personMapperMock.mapToModel(paymentNotificationDTO.getDebtor())).thenReturn(debtor);

    PaymentNotification result = mapper.mapToModel(paymentNotificationDTO);

    reflectionEqualsByName(paymentNotificationDTO, result);
    Mockito.verify(personMapperMock, Mockito.times(1)).mapToModel(paymentNotificationDTO.getDebtor());

  }

  @Test
  void map_success() {
    PaymentNotificationNoPII paymentNotificationNoPIIExpected =buildPaymentNotificationNoPII();
    PaymentNotificationPIIDTO paymentNotificationPIIDTOExpected = buildPaymentNotificationPIIDTO();

    PaymentNotification paymentNotification = buildPaymentNotification();
    byte[] expectedHashedCF = "debtorFiscalCodeHash".getBytes();
    byte[] expectedHashedRemInfo = "remittanceInformationHash".getBytes();
    Mockito.when(dataCipherServiceMock.hash(paymentNotification.getDebtor().getFiscalCode())).thenReturn(expectedHashedCF);
    Mockito.when(dataCipherServiceMock.hash(paymentNotification.getRemittanceInformation())).thenReturn(expectedHashedRemInfo);

    Pair<PaymentNotificationNoPII, PaymentNotificationPIIDTO> result = mapper.map(paymentNotification);

    reflectionEqualsByName(paymentNotificationNoPIIExpected, result.getFirst());
    reflectionEqualsByName(paymentNotificationPIIDTOExpected, result.getSecond());
    checkNotNullFields(result.getFirst(), "personalDataId", "debtorFiscalCodeHash","remittanceInformationHash");
    checkNotNullFields(result.getSecond());
  }




  @Test
  void mapToNoPiiDTO_success() {
    //given
    PaymentNotification paymentNotification = buildPaymentNotification();
    byte[] expectedHashedCF = "debtorFiscalCodeHash".getBytes();
    byte[] expectedHashedRemInfo = "remittanceInformationHash".getBytes();
    Mockito.when(dataCipherServiceMock.hash(paymentNotification.getDebtor().getFiscalCode())).thenReturn(expectedHashedCF);
    Mockito.when(dataCipherServiceMock.hash(paymentNotification.getRemittanceInformation())).thenReturn(expectedHashedRemInfo);

    //when
    PaymentNotificationNoPIIDTO result = mapper.mapToNoPiiDTO(paymentNotification);

    //verify
    TestUtils.reflectionEqualsByName(paymentNotification, result, "debtor");
    TestUtils.checkNotNullFields(result, "personalDataId");
  }


}
