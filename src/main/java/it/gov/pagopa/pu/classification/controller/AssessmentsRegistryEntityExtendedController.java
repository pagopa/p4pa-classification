package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentsRegistryEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** Controller to host spring-data-rest directly not supported methods */
@RestController
public class AssessmentsRegistryEntityExtendedController implements AssessmentsRegistryEntityExtendedControllerApi {

  private final AssessmentsRegistryRepository repository;

  public AssessmentsRegistryEntityExtendedController(AssessmentsRegistryRepository repository) {
    this.repository = repository;
  }

  @Override
  public ResponseEntity<Void> updateStatus(AssessmentsRegistryStatus status, String debtPositionTypeOrgCode, String operatingYear) {
    repository.updateStatus(status,debtPositionTypeOrgCode,operatingYear);
    return ResponseEntity.ok().build();
  }
}
