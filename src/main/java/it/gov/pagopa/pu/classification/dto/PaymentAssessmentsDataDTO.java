package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentAssessmentsDataDTO {
  // Assessments
  private Long assessmentId;
  private Long organizationId;
  private String debtPositionTypeOrgCode;
  private AssessmentStatus status;
  private String assessmentName;
  private boolean printed;
  private boolean flagManualGeneration;
  private String operatorExternalUserId;
  private String iuv;
  private String iud;
  private String iur;

  // AssessmentsDetail
  private List<AssessmentsDetail> assessmentsDetailList;

}
