package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.service.AssessmentsService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller to host spring-data-rest directly not supported methods
 */
@RestController
public class AssessmentsController implements AssessmentEntityExtendedControllerApi {

  private final AssessmentsService assessmentsService;

  public AssessmentsController(AssessmentsService assessmentsService) {
    this.assessmentsService = assessmentsService;
  }


  @Override
  public ResponseEntity<List<Assessments>> createAssessmentByReceiptId(Long receiptId) {
    String accessToken = SecurityUtils.getAccessToken();

    return ResponseEntity.ok(assessmentsService.createAssesment(receiptId, accessToken));
  }

}
