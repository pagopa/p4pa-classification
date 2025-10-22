package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.dto.generated.ValidateBalanceRequest;
import it.gov.pagopa.pu.classification.service.BalanceService;
import it.gov.pagopa.pu.classification.service.BalanceTemplateResolverService;
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
  @Mock
  private BalanceTemplateResolverService balanceTemplateResolverServiceMock;

  private BalanceController balanceController;

  @BeforeEach
  void init() {
    balanceController = new BalanceController(balanceServiceMock, balanceTemplateResolverServiceMock);
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

  @Test
  void givenAssessmentRegistryThenReturnBalance() {
    String debtPositionTypeOrgCode = "DPTO_CODE";
    Long orgId = 1L;
    String balance = "balance";

    Mockito.when(balanceServiceMock.getBalanceByAssessmentRegistry(orgId, debtPositionTypeOrgCode)).thenReturn(balance);

    ResponseEntity<String> response = balanceController.getBalanceByAssessmentRegistry(orgId, debtPositionTypeOrgCode);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(balance, response.getBody());
  }

  @Test
  void givenCalculateAmountBalanceRequestWhenCalculateThenReturnBalance(){
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance("balance")
      .amountCents(100L)
      .remittanceInformation("remittanceInformation")
      .build();
    String balanceExpected = "balanceCalculated";

    Mockito.when(balanceTemplateResolverServiceMock.calculateAmountBalance(request)).thenReturn(balanceExpected);

    ResponseEntity<String> response = balanceController.calculateAmountBalance(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(balanceExpected, response.getBody());
  }
}
