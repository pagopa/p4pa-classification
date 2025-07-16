package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.BalanceApi;
import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.dto.generated.ValidateBalanceRequest;
import it.gov.pagopa.pu.classification.service.BalanceAmountService;
import it.gov.pagopa.pu.classification.service.BalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController implements BalanceApi {

  private final BalanceService balanceService;
  private final BalanceAmountService balanceAmountService;

  public BalanceController(BalanceService balanceService, BalanceAmountService balanceAmountService) {
    this.balanceService = balanceService;
    this.balanceAmountService = balanceAmountService;
  }

  @Override
  public ResponseEntity<Boolean> validateBalance(ValidateBalanceRequest balanceRequest) {
    return ResponseEntity.ok(balanceService.isBalanceValid(balanceRequest.getBalance()));
  }

  @Override
  public ResponseEntity<String> getBalanceByAssessmentRegistry(Long organizationId, String debtPositionTypeOrgCode) {
    return ResponseEntity.ok(balanceService.getBalanceByAssessmentRegistry(organizationId, debtPositionTypeOrgCode));
  }

  @Override
  public ResponseEntity<String> calculateAmountBalance(CalculateAmountBalanceRequest calculateAmountBalanceRequest){
    return ResponseEntity.ok(balanceAmountService.calculateAmountBalance(calculateAmountBalanceRequest));
  }
}
