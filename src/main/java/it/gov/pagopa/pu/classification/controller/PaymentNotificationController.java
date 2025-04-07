package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.PaymentNotificationApi;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationNoPIIDTO;
import it.gov.pagopa.pu.classification.service.PaymentNotificationService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to host spring-data-rest directly not supported methods
 */
@RestController
public class PaymentNotificationController implements PaymentNotificationApi {

  private final PaymentNotificationService paymentNotificationService;

  public PaymentNotificationController(PaymentNotificationService paymentNotificationService) {
    this.paymentNotificationService = paymentNotificationService;
  }


  @Override
  public  ResponseEntity<PaymentNotificationNoPIIDTO> createPaymentNotification(PaymentNotificationDTO paymentNotificationDTO){
    String accessToken = SecurityUtils.getAccessToken();
    PaymentNotificationNoPIIDTO body = paymentNotificationService.createPaymentNotification(accessToken, paymentNotificationDTO);
    return new ResponseEntity<>(body, HttpStatus.OK);
  }

}
