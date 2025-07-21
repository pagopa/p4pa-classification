package it.gov.pagopa.pu.classification.connector.organization.service;

import it.gov.pagopa.pu.classification.connector.organization.client.OrganizationSearchClient;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@CacheConfig(cacheNames = it.gov.pagopa.pu.classification.config.CacheConfig.Fields.organization)
public class OrganizationServiceImpl implements OrganizationService {

  private final OrganizationSearchClient organizationSearchClient;

  public OrganizationServiceImpl(OrganizationSearchClient organizationSearchClient) {
    this.organizationSearchClient = organizationSearchClient;
  }

  @Override
  @Cacheable(key = "'fiscalCode-' + #orgFiscalCode", unless = "#result == null")
  public Optional<Organization> getOrganizationByFiscalCode(String orgFiscalCode, String accessToken) {
    return Optional.ofNullable(
      organizationSearchClient.findByOrgFiscalCode(orgFiscalCode, accessToken)
    );
  }

}
