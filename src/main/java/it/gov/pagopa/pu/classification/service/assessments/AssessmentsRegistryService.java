package it.gov.pagopa.pu.classification.service.assessments;


import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;

public interface AssessmentsRegistryService {
  void createAssessmentsRegistryByDebtPositionDTOAndIud(
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request, String accessToken);
}
