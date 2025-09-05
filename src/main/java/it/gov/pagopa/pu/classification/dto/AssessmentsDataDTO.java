package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentsDataDTO {

  // Assessments
  private Long assessmentId;
  private Long organizationId;
  private String debtPositionTypeOrgCode;
  private AssessmentStatus status;
  private String assessmentName;
  private boolean printed;
  private boolean flagManualGeneration;
  private String operatorExternalUserId;

  // AssessmentsDetail
  private Long assessmentDetailId;
  private String iuv;
  private String iud;
  private String iur;
  private byte[] debtorFiscalCodeHash;
  private OffsetDateTime paymentDateTime;
  private String officeCode;
  private String sectionCode;
  private String assessmentCode;
  private Long amountCents;
  private boolean amountSubmitted;
  private Long receiptId;
}
