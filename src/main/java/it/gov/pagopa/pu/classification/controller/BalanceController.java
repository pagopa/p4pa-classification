package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.BalanceApi;
import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.dto.generated.ValidateBalanceRequest;
import it.gov.pagopa.pu.classification.service.BalanceService;
import it.gov.pagopa.pu.classification.service.BalanceTemplateResolverService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BalanceController implements BalanceApi {

  private final BalanceService balanceService;
  private final BalanceTemplateResolverService balanceAmountService;

  public BalanceController(BalanceService balanceService, BalanceTemplateResolverService balanceAmountService) {
    this.balanceService = balanceService;
    this.balanceAmountService = balanceAmountService;
  }

  @Override
  public ResponseEntity<Boolean> validateBalance(ValidateBalanceRequest balanceRequest) {
    log.info("Validate formal structure of balance: {}", balanceRequest.getBalance());
    return ResponseEntity.ok(balanceService.isBalanceValid(balanceRequest.getBalance()));
  }

  @Override
  public ResponseEntity<String> getBalanceByAssessmentRegistry(Long organizationId, String debtPositionTypeOrgCode) {
    log.info("Retrieve balance by assessment registry for organization with id {} and debt position type org code  {}", organizationId, debtPositionTypeOrgCode);
    return ResponseEntity.ok(balanceService.getBalanceByAssessmentRegistry(organizationId, debtPositionTypeOrgCode));
  }

  @Override
  public ResponseEntity<String> calculateAmountBalance(CalculateAmountBalanceRequest calculateAmountBalanceRequest){
    log.info("Calculate amount of balance {} considering installment amount cents of {}", calculateAmountBalanceRequest.getBalance(), calculateAmountBalanceRequest.getAmountCents());
    return ResponseEntity.ok(balanceAmountService.calculateAmountBalance(calculateAmountBalanceRequest));
  }
}
