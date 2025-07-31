package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy
public class DebtPositionTypeOrgServiceImpl implements DebtPositionTypeOrgService{

  private final DebtPositionTypeOrgClient debtPositionTypeOrgClient;

  public DebtPositionTypeOrgServiceImpl(DebtPositionTypeOrgClient debtPositionTypeOrgClient) {
    this.debtPositionTypeOrgClient = debtPositionTypeOrgClient;
  }

  @Override
  public DebtPositionTypeOrg getDebtPositionTypeOrgByInstallmentId(Long installmentId, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrgByInstallmentId(installmentId,accessToken);
  }

  @Override
  public List<DebtPositionTypeOrg> findDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, Boolean flagActive, String accessToken) {
    return debtPositionTypeOrgClient.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, flagActive, accessToken);
  }

  @Override
  public DebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeOrgId(Long organizationId, Long debtPositionTypeOrgId, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrgByDebtPositionTypeOrgId(organizationId, debtPositionTypeOrgId, accessToken);
  }
}
