package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrgBalanceCostType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class DebtPositionTypeOrgBalanceCostClient {
  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgBalanceCostClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public CollectionModelDebtPositionTypeOrgBalanceCost getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(Long debtPositionTypeOrgId, String opYear, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgBalanceCostsGetByDebtPositionTypeOrgIdAndOperatingYear(debtPositionTypeOrgId, opYear);
  }

  public DebtPositionTypeOrgBalanceCost getByInstallmentIdAndTypeAndOperatingYear(Long installmentId, DebtPositionTypeOrgBalanceCostType debtPositionTypeOrgBalanceCostType, String operatingYear, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgBalanceCostsGetByInstallmentIdAndTypeAndOperatingYear(installmentId, debtPositionTypeOrgBalanceCostType, operatingYear);
    } catch (HttpClientErrorException.NotFound e) {
      log.info("Cannot find DebtPositionTypeOrgBalanceCost from installment id {} cost type {} and operating year {}", installmentId, debtPositionTypeOrgBalanceCostType, operatingYear);
      return null;
    }
  }
}
