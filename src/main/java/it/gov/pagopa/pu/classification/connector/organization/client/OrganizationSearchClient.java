package it.gov.pagopa.pu.classification.connector.organization.client;

import it.gov.pagopa.pu.classification.connector.organization.config.OrganizationApisHolder;
import it.gov.pagopa.pu.classification.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrganizationSearchClient {

  private final OrganizationApisHolder organizationApisHolder;

  public OrganizationSearchClient(OrganizationApisHolder organizationApisHolder) {
    this.organizationApisHolder = organizationApisHolder;
  }


  public Organization findByOrgFiscalCode(String orgFiscalCode, String accessToken) {
    try{
      return organizationApisHolder.getOrganizationSearchControllerApi(accessToken)
        .crudOrganizationsFindByOrgFiscalCode(orgFiscalCode);
    } catch (RestInvokeNotFoundException e){
      log.info("Cannot find organization having fiscalCode {}", orgFiscalCode);
      return null;
    }
  }

}
