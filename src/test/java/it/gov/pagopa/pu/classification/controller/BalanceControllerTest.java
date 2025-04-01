package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.generated.ValidateBalanceRequest;
import it.gov.pagopa.pu.classification.service.BalanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BalanceControllerTest {

  @Mock
  private BalanceService balanceServiceMock;

  private BalanceController balanceController;

  @BeforeEach
  void init() {
    balanceController = new BalanceController(balanceServiceMock);
  }

  @Test
  void givenValidBalanceWhenValidateThenSuccess() {
    ValidateBalanceRequest balanceRequest = ValidateBalanceRequest.builder().balance("balance").build();
    Mockito.when(balanceServiceMock.isBalanceValid("balance"))
      .thenReturn(Boolean.TRUE);

    ResponseEntity<Boolean> response = balanceController.validateBalance(balanceRequest);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(Boolean.TRUE, response.getBody());
  }
}
