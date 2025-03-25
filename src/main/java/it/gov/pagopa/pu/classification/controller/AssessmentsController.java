package it.gov.pagopa.pu.classification.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.gov.pagopa.pu.classification.controller.generated.AssessmentsControllerApi;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller to host spring-data-rest directly not supported methods
 */
@RestController
public class AssessmentsController implements AssessmentsControllerApi {

  private final AssessmentsService assessmentsService;

  public AssessmentsController(AssessmentsService assessmentsService) {
    this.assessmentsService = assessmentsService;
  }


  @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(ref = "EntityModelAssessments"))))
  @Override
  public ResponseEntity<List<Assessments>> createAssessmentByReceiptId(Long receiptId) {
    String accessToken = SecurityUtils.getAccessToken();

    return ResponseEntity.ok(assessmentsService.createAssesment(receiptId, accessToken));
  }

}
