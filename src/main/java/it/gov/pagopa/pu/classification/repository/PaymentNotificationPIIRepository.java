package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.dto.PaymentNotificationDTO;

public interface PaymentNotificationPIIRepository {

  PaymentNotificationDTO save (PaymentNotificationDTO paymentNotificationDTO);
  PaymentNotificationDTO findBySemanticKey(Long organizationId, String iud);

}
