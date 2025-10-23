package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentsControllerApi;
import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsService;
import it.gov.pagopa.pu.classification.util.DateConversionUtils;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Controller to host spring-data-rest directly not supported methods
 */
@Slf4j
@RestController
public class AssessmentsController implements AssessmentsControllerApi {

  private final AssessmentsService assessmentsService;

  public AssessmentsController(AssessmentsService assessmentsService) {
    this.assessmentsService = assessmentsService;
  }

  @Override
  public ResponseEntity<List<Assessments>> createAssessmentByReceiptId(Long receiptId) {
    String accessToken = SecurityUtils.getAccessToken();

    return ResponseEntity.ok(assessmentsService.createAssessment(receiptId, SecurityUtils.getCurrentUserExternalId(), accessToken));
  }

  @Override
  public ResponseEntity<PagedAssessmentsView> getPagedAssessmentsList(Long organizationId, String assessmentName, OffsetDateTime updateDateFrom, OffsetDateTime updateDateTo, String iuv, List<String> debtPositionTypeOrgCodes, AssessmentStatus status, Pageable pageable) {
    LocalDateTimeIntervalFilter updateDateTimeIntervalFilter = new LocalDateTimeIntervalFilter(
      DateConversionUtils.offsetDateTime2LocalDateTime(updateDateFrom),
      DateConversionUtils.offsetDateTime2LocalDateTime(updateDateTo));

    return ResponseEntity.ok(assessmentsService.getPagedAssessmentsView(organizationId, assessmentName, updateDateTimeIntervalFilter, iuv, debtPositionTypeOrgCodes, status, pageable));
  }

  @Override
  public ResponseEntity<Assessments> createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode) {
    String accessToken = SecurityUtils.getAccessToken();
    log.info("User requested createAssessment with assessmentName {}, debtPositionTypeOrgCode {} for organization {}", assessmentName, debtPositionTypeOrgCode, organizationId);
    return ResponseEntity.ok(assessmentsService.createAssessment(organizationId, assessmentName, debtPositionTypeOrgCode, SecurityUtils.getCurrentUserExternalId(), accessToken));
  }

}
