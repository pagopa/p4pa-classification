package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryRequest;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import it.gov.pagopa.pu.classification.util.Utilities;
import org.springframework.stereotype.Service;

@Service
public class AssessmentsRegistryServiceImpl implements AssessmentsRegistryService{

  private final AssessmentsRegistryRepository assessmentsRegistryRepository;

  public AssessmentsRegistryServiceImpl(
    AssessmentsRegistryRepository assessmentsRegistryRepository) {
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
  }

  @Override
  public Long createAssessmentsRegistry(CreateAssessmentsRegistryRequest request) {
    return assessmentsRegistryRepository.insertIfNotExists(
      request.getOrganizationId(),
      request.getDebtPositionTypeOrgCode(),
      request.getSectionCode(),
      request.getSectionDescription(),
      request.getOfficeCode(),
      request.getOfficeDescription(),
      request.getAssessmentCode(),
      request.getAssessmentDescription(),
      request.getOperatingYear(),
      SecurityUtils.getCurrentUserExternalId(),
      Utilities.getTraceId());
  }
}
