package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentsRegistryApi;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsRegistryService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity<Void> createAssessmentsRegistryByDebtPositionDTOAndIud(CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request) {
    log.debug("Create Assessment Registry with request params {}",request);
    String accessToken = SecurityUtils.getAccessToken();
    assessmentsRegistryService.createAssessmentsRegistryByDebtPositionDTOAndIud(request, accessToken);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Override
  public ResponseEntity<AssessmentsRegistry> createAssessmentsRegistry(AssessmentsRegistry assessmentsRegistry) {
    log.debug("Create Assessment Registry with organizationId {}, debtPositionTypeOrgCode {} and operatingYear {}",assessmentsRegistry.getOrganizationId(),assessmentsRegistry.getDebtPositionTypeOrgCode(),assessmentsRegistry.getOperatingYear());
    return new ResponseEntity<>(assessmentsRegistryService.createAssessmentsRegistry(assessmentsRegistry),HttpStatus.CREATED);
  }
}
