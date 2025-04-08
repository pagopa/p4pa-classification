package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.repository.PaymentNotificationPIIRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationServiceImpl implements PaymentNotificationService {

  private final PaymentNotificationPIIRepository paymentNotificationPIIRepository;

  public PaymentNotificationServiceImpl(PaymentNotificationPIIRepository paymentNotificationPIIRepository) {
    this.paymentNotificationPIIRepository = paymentNotificationPIIRepository;
  }

  @Override
  public PaymentNotificationDTO createPaymentNotification(String accessToken, PaymentNotificationDTO paymentNotificationDTO) {
    return paymentNotificationPIIRepository.save(paymentNotificationDTO);
  }
}
