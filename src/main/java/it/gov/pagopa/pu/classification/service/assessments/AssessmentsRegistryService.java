package it.gov.pagopa.pu.classification.service.assessments;


import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryRequest;

public interface AssessmentsRegistryService {
  Long createAssessmentsRegistry(CreateAssessmentsRegistryRequest assessmentsRegistryRequest);
}
