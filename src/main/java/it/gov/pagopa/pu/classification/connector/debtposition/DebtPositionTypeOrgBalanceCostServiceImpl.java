package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.DebtPositionTypeOrgBalanceCostClient;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrgBalanceCost;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtPositionTypeOrgBalanceCostServiceImpl implements DebtPositionTypeOrgBalanceCostService {
  private final DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient;

  public DebtPositionTypeOrgBalanceCostServiceImpl(DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient) {
    this.debtPositionTypeOrgBalanceCostClient = debtPositionTypeOrgBalanceCostClient;
  }

  @Override
  public List<DebtPositionTypeOrgBalanceCost> getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(Long debtPositionTypeOrgId, String opYear, String accessToken) {
    return debtPositionTypeOrgBalanceCostClient
      .getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(debtPositionTypeOrgId, opYear, accessToken)
      .getEmbedded()
      .getDebtPositionTypeOrgBalanceCosts();
  }
}
