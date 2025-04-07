package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.PaymentNotification;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationNoPIIDTO;
import it.gov.pagopa.pu.classification.mapper.PaymentNotificationPIIMapper;
import it.gov.pagopa.pu.classification.repository.PaymentNotificationPIIRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentNotificationServiceImpl implements PaymentNotificationService {

  private final PaymentNotificationPIIRepository paymentNotificationPIIRepository;
  private final PaymentNotificationPIIMapper paymentNotificationPIIMapper;

  public PaymentNotificationServiceImpl(PaymentNotificationPIIRepository paymentNotificationPIIRepository, PaymentNotificationPIIMapper paymentNotificationPIIMapper) {
    this.paymentNotificationPIIRepository = paymentNotificationPIIRepository;
    this.paymentNotificationPIIMapper = paymentNotificationPIIMapper;
  }

  @Override
  public PaymentNotificationNoPIIDTO createPaymentNotification(String accessToken, PaymentNotificationDTO paymentNotificationDTO) {
    PaymentNotification paymentNotification = paymentNotificationPIIMapper.mapToModel(paymentNotificationDTO);
    paymentNotification = paymentNotificationPIIRepository.save(paymentNotification);
    return paymentNotificationPIIMapper.mapToNoPiiDTO(paymentNotification);
  }
}
