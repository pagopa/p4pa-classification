package it.gov.pagopa.pu.classification.service;


import it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO;

public interface PaymentNotificationService {

  PaymentNotificationDTO createPaymentNotification(String accessToken, PaymentNotificationDTO paymentNotificationDTO);

}
