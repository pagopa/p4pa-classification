package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.BalanceApi;
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
  public ResponseEntity<Boolean> validateBalance(String balance) {
    return ResponseEntity.ok(balanceService.isBalanceValid(balance));
  }
}
