package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing assessments.
 */
public interface AssessmentsService {

  /**
   * Creates assessments based on the given receipt ID and access token.
   *
   * @param receiptId the ID of the receipt
   * @param accessToken the access token for authentication
   * @return a list of created assessments
   */
  List<Assessments> createAssessment(Long receiptId, String accessToken);

  /**
   * Retrieves a paginated view of assessments based on the provided filters.
   *
   * @param assessmentName the name of the assessment to filter by; if {@code null}, this filter is ignored
   * @param updateDateTimeIntervalFilter the date-time interval filter to apply on the update date; if {@code null}, no date filtering is applied
   * @param iuv the IUV (Unique Payment Identifier) to filter by; if {@code null}, this filter is ignored
   * @param debtPositionTypeOrgCodes list of organization code of the debt position type to filter by; if {@code null}, this filter is ignored
   * @param status the status of the assessment to filter by; if {@code null}, this filter is ignored
   * @param pageable the pagination and sorting information
   * @return a paginated view of assessments matching the specified filters
   */

  PagedAssessmentsView getPagedAssessmentsView(String assessmentName, LocalDateTimeIntervalFilter updateDateTimeIntervalFilter, String iuv, List<String> debtPositionTypeOrgCodes, AssessmentStatus status, Pageable pageable);


  /**
   * Creates assessment based on the given assessmentName, debtPositionTypeOrgCode and access token.
   *
   * @param assessmentName the name of assessment
   * @param debtPositionTypeOrgCode debtPositionTypeOrgCode
   */
  Assessments createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode, String operatorExternalUserId);
}
