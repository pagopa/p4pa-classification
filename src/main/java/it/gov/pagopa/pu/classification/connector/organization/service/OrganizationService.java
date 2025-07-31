package it.gov.pagopa.pu.classification.connector.organization.service;

import it.gov.pagopa.pu.organization.dto.generated.Organization;

import java.util.Optional;

public interface OrganizationService {

  Optional<Organization> getOrganizationByFiscalCode(String orgFiscalCode, String accessToken);

}
