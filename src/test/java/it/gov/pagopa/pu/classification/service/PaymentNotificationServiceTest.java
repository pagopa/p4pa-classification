package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.PaymentNotification;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationNoPIIDTO;
import it.gov.pagopa.pu.classification.mapper.PaymentNotificationPIIMapper;
import it.gov.pagopa.pu.classification.repository.PaymentNotificationPIIRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentNotificationServiceTest {

  @Mock
  private PaymentNotificationPIIRepository paymentNotificationPIIRepositoryMock;

  @Mock
  private PaymentNotificationPIIMapper paymentNotificationPIIMapper;

  private PaymentNotificationService paymentNotificationService;

  @BeforeEach
  void setUp() {
    paymentNotificationService = new PaymentNotificationServiceImpl(paymentNotificationPIIRepositoryMock, paymentNotificationPIIMapper);
  }

  @Test
  void createPaymentNotification_success() {
    PaymentNotificationDTO paymentNotificationDTO = new PaymentNotificationDTO();
    PaymentNotification paymentNotification = new PaymentNotification();
    PaymentNotificationNoPIIDTO paymentNotificationNoPIIDTO = new PaymentNotificationNoPIIDTO();

    when(paymentNotificationPIIMapper.mapToModel(any(PaymentNotificationDTO.class))).thenReturn(paymentNotification);
    when(paymentNotificationPIIRepositoryMock.save(any(PaymentNotification.class))).thenReturn(paymentNotification);
    when(paymentNotificationPIIMapper.mapToNoPiiDTO(any(PaymentNotification.class))).thenReturn(paymentNotificationNoPIIDTO);

    PaymentNotificationNoPIIDTO result = paymentNotificationService.createPaymentNotification("accessToken", paymentNotificationDTO);

    assertEquals(paymentNotificationNoPIIDTO, result);
  }



  @Test
  void createPaymentNotification_emptyAccessToken() {
    PaymentNotificationDTO paymentNotificationDTO = new PaymentNotificationDTO();
    PaymentNotificationNoPIIDTO result = paymentNotificationService.createPaymentNotification("", paymentNotificationDTO);
    assertEquals(null, result);
  }
}
