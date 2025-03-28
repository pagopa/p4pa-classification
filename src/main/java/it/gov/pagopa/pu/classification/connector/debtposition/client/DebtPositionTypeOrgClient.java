package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Lazy
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
}
