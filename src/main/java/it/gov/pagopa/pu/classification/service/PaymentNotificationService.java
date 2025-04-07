package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationNoPIIDTO;

public interface PaymentNotificationService {

  PaymentNotificationNoPIIDTO createPaymentNotification(String accessToken, PaymentNotificationDTO paymentNotificationDTO);

}
