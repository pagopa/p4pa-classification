package it.gov.pagopa.pu.classification.service.assessments;


import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;

public interface AssessmentsRegistryService {
  void createAssessmentsRegistryByDebtPositionDTOAndIud(
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request, String accessToken);

  AssessmentsRegistry createAssessmentsRegistry(AssessmentsRegistry assessmentsRegistry);
}
