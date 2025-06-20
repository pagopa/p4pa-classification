package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentsControllerApi;
import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsService;
import it.gov.pagopa.pu.classification.util.DateConversionUtils;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
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

  @Override
  public ResponseEntity<List<Assessments>> createAssessmentByReceiptId(Long receiptId) {
    String accessToken = SecurityUtils.getAccessToken();

    return ResponseEntity.ok(assessmentsService.createAssesment(receiptId, accessToken));
  }

  @Override
  public ResponseEntity<PagedAssessmentsView> getPagedAssessmentsList(String assessmentName, OffsetDateTime updateDateFrom, OffsetDateTime updateDateTo, String iuv, List<String> debtPositionTypeOrgCodes, AssessmentStatus status, Pageable pageable) {
    String accessToken = SecurityUtils.getAccessToken();
    LocalDateTimeIntervalFilter updateDateTimeIntervalFilter = new LocalDateTimeIntervalFilter(
      DateConversionUtils.offsetDateTime2LocalDateTime(updateDateFrom),
      DateConversionUtils.offsetDateTime2LocalDateTime(updateDateTo));

    return ResponseEntity.ok(assessmentsService.getPagedAssessmentsView(assessmentName, updateDateTimeIntervalFilter, iuv, debtPositionTypeOrgCodes, status, pageable, accessToken));
  }
}
