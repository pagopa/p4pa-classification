package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentsEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AssessmentsEntityExtendedController implements AssessmentsEntityExtendedControllerApi {

  private final AssessmentsRepository assessmentsRepository;

    public AssessmentsEntityExtendedController(AssessmentsRepository assessmentsRepository) {
        this.assessmentsRepository = assessmentsRepository;
    }

  @Override
  public ResponseEntity<Void> updateStatus(Long assessmentId, Long organizationId, AssessmentStatus status) {
      assessmentsRepository.updateStatus(status,assessmentId,organizationId);
      return ResponseEntity.ok().build();
  }
}
