package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;

import java.util.List;

/**
 * Service for handling DebtPositionTypeOrg operations.
 */
public interface DebtPositionTypeOrgService {

  /**
   * Retrieves a DebtPositionTypeOrg by the given installment ID.
   *
   * @param installmentId the ID of the installment
   * @param accessToken   the access token for authentication
   * @return the DebtPositionTypeOrg associated with the given installment ID
   */
  DebtPositionTypeOrg getDebtPositionTypeOrgByInstallmentId(Long installmentId, String accessToken);

  /**
   * Retrieves a list of DebtPositionTypeOrg for the given organization ID.
   *
   * @param organizationId       the ID of the organization
   * @param operatorExternalUserId the external user ID of the operator
   * @param flagActive boolean value to filter by flagActive
   * @param accessToken          the access token for authentication
   * @return a list of DebtPositionTypeOrg associated with the given organization ID
   */
  List<DebtPositionTypeOrg> findDebtPositionTypeOrgs(Long organizationId, String operatorExternalUserId, Boolean flagActive, String accessToken);

  /**
   * Retrieves a DebtPositionTypeOrg by the given organizationId and debtPositionTypeOrgId.
   *
   * @param organizationId       the ID of the organization
   * @param debtPositionTypeOrgId the debtPositionTypeOrgId
   * @param accessToken          the access token for authentication
   * @return the DebtPositionTypeOrg associated with the given organizationId and debtPositionTypeOrgId
   */
  DebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeOrgId(Long organizationId, Long debtPositionTypeOrgId, String accessToken);

  /**
   * Retrieves a DebtPositionTypeOrg by the given organization ID and debt position type organization code.
   *
   * @param organizationId the ID of the organization
   * @param debtPositionTypeOrgCode the code of the debt position type organization
   * @param accessToken the access token for authentication
   * @return the DebtPositionTypeOrg associated with the given organization ID and debt position type organization code
   */
  DebtPositionTypeOrg getDebtPositionTypeOrgByDebtPositionTypeOrgCode(Long organizationId, String debtPositionTypeOrgCode, String accessToken);
}
