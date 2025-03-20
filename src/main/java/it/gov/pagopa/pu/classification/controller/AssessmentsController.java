package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.classification.service.AssessmentsService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller to host spring-data-rest directly not supported methods
 */
@RestController
public class AssessmentsController implements AssessmentEntityExtendedControllerApi {

  private final AssessmentsService assessmentsService;
  private final AssessmentsRepository assessmentsRepository;

  public AssessmentsController(AssessmentsService assessmentsService, AssessmentsRepository assessmentsRepository) {
    this.assessmentsService = assessmentsService;
    this.assessmentsRepository = assessmentsRepository;
  }


  @Override
  public ResponseEntity<Void> createAssessmentByReceiptId(Long receiptId) {
    String accessToken = SecurityUtils.getAccessToken();
    List<InstallmentNoPIIResponse> installmentsList = assessmentsService.getInstallmentsByReceiptId(receiptId, accessToken);
    List<Assessments> assessmentsList = installmentsList.stream()
       .map(i ->
           assessmentsService.getAssessment(i, accessToken))
       .toList();
    assessmentsRepository.saveAll(assessmentsList);

    return ResponseEntity.ok().build();
  }

}
