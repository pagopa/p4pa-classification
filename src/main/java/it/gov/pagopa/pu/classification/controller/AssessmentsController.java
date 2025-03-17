package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.service.AssessmentsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/** Controller to host spring-data-rest directly not supported methods */
@RestController
public class AssessmentsController implements AssessmentEntityExtendedControllerApi {

  private final AssessmentsService assessmentsService;

  public AssessmentsController(AssessmentsService assessmentsService) {
    this.assessmentsService = assessmentsService;
  }


  @Override
  public ResponseEntity<Void> createAssessmentByReceiptId(Long receiptId) {
    assessmentsService.getInstallmentsByReceiptId(receiptId);
    return ResponseEntity.ok().build();
  }

}
