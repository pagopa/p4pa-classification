package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;

import java.util.List;

public interface DebtPositionTypeOrgBalanceCostService {
  List<DebtPositionTypeOrgBalanceCost> getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(Long debtPositionTypeOrgId, String opYear, String accessToken);
  DebtPositionTypeOrgBalanceCost getDptoBalanceCostByInstallmentIdAndTypeAndOperatingYear(Long installmentId, DebtPositionTypeOrgBalanceCostType debtPositionTypeOrgBalanceCostType, String operatingYear, String accessToken);
}
