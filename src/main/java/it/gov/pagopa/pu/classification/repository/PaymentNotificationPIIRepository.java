package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.dto.PaymentNotification;

public interface PaymentNotificationPIIRepository {

  PaymentNotification save (PaymentNotification paymentNotification);

}
