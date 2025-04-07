package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.PaymentNotification;
import it.gov.pagopa.pu.classification.mapper.PaymentNotificationPIIMapper;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static it.gov.pagopa.pu.classification.util.faker.PaymentNotificationFaker.buildPaymentNotification;
import static it.gov.pagopa.pu.classification.util.faker.PaymentNotificationFaker.buildPaymentNotificationNoPII;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
class PaymentNotificationRepositoryImplTest {

  @Mock
  private PersonalDataService personalDataService;

  @Mock(answer = Answers.RETURNS_MOCKS)
  private PaymentNotificationNoPIIRepository paymentNotificationNoPIIRepositoryMock;

  @Mock
  private PaymentNotificationPIIMapper paymentNotificationPIIMapperMock;

  private PaymentNotificationPIIRepository repository;

  private final PaymentNotificationNoPII paymentNotificationNoPII = buildPaymentNotificationNoPII();
  private final PaymentNotification paymentNotification = buildPaymentNotification();

  @BeforeEach
  void init() {
    repository = new PaymentNotificationPIIRepositoryImpl(personalDataService, paymentNotificationNoPIIRepositoryMock, paymentNotificationPIIMapperMock);
  }

  @Test
  void findBySemanticKey_success() {
    Long organizationId = 1L;
    String iud = "iud";

    Mockito.when(paymentNotificationNoPIIRepositoryMock.findBySemanticKey(organizationId, iud)).thenReturn(paymentNotificationNoPII);
    Mockito.when(paymentNotificationPIIMapperMock.map(paymentNotificationNoPII)).thenReturn(paymentNotification);

    PaymentNotification result = repository.findBySemanticKey(organizationId, iud);

    Assertions.assertEquals(iud+"_"+organizationId, result.getPaymentNotificationId());
  }

  @Test
  void findBySemanticKey_notFound() {
    Long organizationId = 1L;
    String iud = "iud1";

    Mockito.when(paymentNotificationNoPIIRepositoryMock.findBySemanticKey(organizationId, iud)).thenReturn(null);

    PaymentNotification result = repository.findBySemanticKey(organizationId, iud);

    Assertions.assertEquals(null, result);
  }



}
