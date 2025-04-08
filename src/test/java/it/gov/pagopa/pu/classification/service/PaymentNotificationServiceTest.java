package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.repository.PaymentNotificationPIIRepository;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static it.gov.pagopa.pu.classification.util.faker.PaymentNotificationFaker.buildPaymentNotification;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentNotificationServiceTest {

  @Mock
  private PaymentNotificationPIIRepository paymentNotificationPIIRepositoryMock;

  private PaymentNotificationService paymentNotificationService;

  @BeforeEach
  void setUp() {
    paymentNotificationService = new PaymentNotificationServiceImpl(paymentNotificationPIIRepositoryMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      paymentNotificationPIIRepositoryMock);
  }

  @Test
  void createPaymentNotification_success() {
    PaymentNotificationDTO paymentNotificationDTO = buildPaymentNotification();
    paymentNotificationDTO.setPaymentNotificationId(null);
    PaymentNotificationDTO experctedResult = buildPaymentNotification();

    when(paymentNotificationPIIRepositoryMock.save(paymentNotificationDTO)).thenReturn(experctedResult);

    PaymentNotificationDTO result = paymentNotificationService.createPaymentNotification("accessToken", paymentNotificationDTO);

    TestUtils.reflectionEqualsByName(paymentNotificationDTO, experctedResult, "paymentNotificationId");
    Assertions.assertEquals(result.getPaymentNotificationId(), paymentNotificationDTO.getIud()+"_"+paymentNotificationDTO.getOrganizationId());

  }
}
