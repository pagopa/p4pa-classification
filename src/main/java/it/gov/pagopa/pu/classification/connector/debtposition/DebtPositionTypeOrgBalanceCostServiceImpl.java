package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.DebtPositionTypeOrgBalanceCostClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DebtPositionTypeOrgBalanceCostServiceImpl implements DebtPositionTypeOrgBalanceCostService {
  private final DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient;

  public DebtPositionTypeOrgBalanceCostServiceImpl(DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient) {
    this.debtPositionTypeOrgBalanceCostClient = debtPositionTypeOrgBalanceCostClient;
  }

  @Override
  public List<DebtPositionTypeOrgBalanceCost> getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(Long debtPositionTypeOrgId, String opYear, String accessToken) {
    return Objects.requireNonNull(debtPositionTypeOrgBalanceCostClient
        .getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(debtPositionTypeOrgId, opYear, accessToken)
        .getEmbedded())
      .getDebtPositionTypeOrgBalanceCosts();
  }

  @Override
  public DebtPositionTypeOrgBalanceCost getDptoBalanceCostByInstallmentIdAndTypeAndOperatingYear(Long installmentId, DebtPositionTypeOrgBalanceCostType debtPositionTypeOrgBalanceCostType, String operatingYear, String accessToken) {
    return debtPositionTypeOrgBalanceCostClient.getByInstallmentIdAndTypeAndOperatingYear(installmentId, debtPositionTypeOrgBalanceCostType, operatingYear, accessToken);
  }
}
