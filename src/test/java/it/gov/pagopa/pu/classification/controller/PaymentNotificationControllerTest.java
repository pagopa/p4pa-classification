package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationDTO;
import it.gov.pagopa.pu.classification.dto.generated.PaymentNotificationNoPIIDTO;
import it.gov.pagopa.pu.classification.service.PaymentNotificationService;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentNotificationControllerTest {

  @Mock
  private PaymentNotificationService serviceMock;

  private PaymentNotificationController controller;

  private final PaymentNotificationDTO paymentNotificationDTO = new PaymentNotificationDTO();
  private final PaymentNotificationNoPIIDTO paymentNotificationNoPIIDTO = new PaymentNotificationNoPIIDTO();

  @BeforeEach
  void init() {
    controller =  new PaymentNotificationController(serviceMock);
  }

  @AfterEach
  void clear(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void createPaymentNotification_success() {
    String accessToken = "testAccessToken";
    when(serviceMock.createPaymentNotification(accessToken, paymentNotificationDTO))
      .thenReturn(paymentNotificationNoPIIDTO);
    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");

    ResponseEntity<PaymentNotificationNoPIIDTO> response = controller.createPaymentNotification(paymentNotificationDTO);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(paymentNotificationNoPIIDTO, response.getBody());
    Mockito.verify(serviceMock).createPaymentNotification(accessToken,paymentNotificationDTO);
  }

}
