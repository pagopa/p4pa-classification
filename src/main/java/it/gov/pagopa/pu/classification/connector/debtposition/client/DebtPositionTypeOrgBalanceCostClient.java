package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
