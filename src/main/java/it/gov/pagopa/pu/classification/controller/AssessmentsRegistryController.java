package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentsRegistryApi;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryRequest;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsRegistryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AssessmentsRegistryController implements AssessmentsRegistryApi {

  private final AssessmentsRegistryService assessmentsRegistryService;

  public AssessmentsRegistryController(
    AssessmentsRegistryService assessmentsRegistryService) {
    this.assessmentsRegistryService = assessmentsRegistryService;
  }

  @Override
  public ResponseEntity<Long> createAssessmentsRegistry(CreateAssessmentsRegistryRequest createAssessmentsRegistryRequest) {
    log.info("Request for create assessment registry if not exists");
    log.debug("Create Assessment Registry with request params {}",createAssessmentsRegistryRequest);
    return ResponseEntity.ok(assessmentsRegistryService.createAssessmentsRegistry(createAssessmentsRegistryRequest));
  }
}
