package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class DebtPositionTypeOrgClient {
  private final DebtPositionApisHolder debtPositionApisHolder;

  public DebtPositionTypeOrgClient(DebtPositionApisHolder debtPositionApisHolder) {
    this.debtPositionApisHolder = debtPositionApisHolder;
  }

  public DebtPositionTypeOrg getDebtPositionTypeOrgByInstallmentId(Long installmentId, String accessToken) {
    try {
      return debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsGetDebtPositionTypeOrgByInstallmentId(installmentId);
    } catch (HttpClientErrorException.NotFound e) {
      log.info("Cannot find DeptPositionTypeOrg from installment id {}", installmentId);
      return null;
    }
  }

  public List<DebtPositionTypeOrg> findDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, Boolean flagActive, String accessToken) {
      return Objects.requireNonNull(debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
          .crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(String.valueOf(organizationId), operatorExternalUserId, flagActive)
          .getEmbedded())
        .getDebtPositionTypeOrgs();
  }

  public DebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeOrgId(Long organizationId, Long debtPositionTypeOrgId, String accessToken) {
    return debtPositionApisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
      .crudDebtPositionTypeOrgsFindByOrganizationIdAndDebtPositionTypeOrgId(organizationId, debtPositionTypeOrgId);
  }
}
