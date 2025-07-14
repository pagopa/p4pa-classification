package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.BalanceApi;
import it.gov.pagopa.pu.classification.dto.generated.ValidateBalanceRequest;
import it.gov.pagopa.pu.classification.service.BalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController implements BalanceApi {

  private final BalanceService balanceService;

  public BalanceController(BalanceService balanceService) {
    this.balanceService = balanceService;
  }

  @Override
  public ResponseEntity<Boolean> validateBalance(ValidateBalanceRequest balanceRequest) {
    return ResponseEntity.ok(balanceService.isBalanceValid(balanceRequest.getBalance()));
  }

  @Override
  public ResponseEntity<String> getBalanceByAssessmentRegistry(Long organizationId, String debtPositionTypeOrgCode) {
    return ResponseEntity.ok(balanceService.getBalanceByAssessmentRegistry(organizationId, debtPositionTypeOrgCode));
  }
}
