package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = it.gov.pagopa.pu.classification.config.CacheConfig.Fields.debtPositionTypeOrg)
public class DebtPositionTypeOrgServiceImpl implements DebtPositionTypeOrgService{

  private final DebtPositionTypeOrgClient debtPositionTypeOrgClient;

  public DebtPositionTypeOrgServiceImpl(DebtPositionTypeOrgClient debtPositionTypeOrgClient) {
    this.debtPositionTypeOrgClient = debtPositionTypeOrgClient;
  }

  @Override
  @Cacheable(key = "'installmentId-' + #installmentId", unless = "#result == null")
  public DebtPositionTypeOrg getDebtPositionTypeOrgByInstallmentId(Long installmentId, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrgByInstallmentId(installmentId,accessToken);
  }

  @Override
  public List<DebtPositionTypeOrg> findDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, Boolean flagActive, String accessToken) {
    return debtPositionTypeOrgClient.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, flagActive, accessToken);
  }

  @Override
  @Cacheable(key = "'dpTypeOrgId-' + #debtPositionTypeOrgId", unless = "#result == null")
  public DebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeOrgId(Long organizationId, Long debtPositionTypeOrgId, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrgByDebtPositionTypeOrgId(organizationId, debtPositionTypeOrgId, accessToken);
  }

  @Override
  @Cacheable(key = "'orgId-' + #organizationId + ',code-' + #debtPositionTypeOrgCode", unless = "#result == null")
  public DebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeOrgCode(Long organizationId, String debtPositionTypeOrgCode, String accessToken) {
    return debtPositionTypeOrgClient.getDebtPositionTypeOrgByDebtPositionTypeOrgCode(organizationId, debtPositionTypeOrgCode, accessToken);
  }
}
