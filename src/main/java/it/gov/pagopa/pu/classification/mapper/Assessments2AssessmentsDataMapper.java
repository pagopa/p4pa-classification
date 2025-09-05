package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.AssessmentsDataDTO;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Assessments2AssessmentsDataMapper {

  public AssessmentsDataDTO map(Assessments assessments, AssessmentsDetail assessmentsDetail) {
    return AssessmentsDataDTO.builder()
      .assessmentId(assessments.getAssessmentId())
      .organizationId(assessments.getOrganizationId())
      .debtPositionTypeOrgCode(assessments.getDebtPositionTypeOrgCode())
      .status(assessments.getStatus())
      .assessmentName(assessments.getAssessmentName())
      .printed(assessments.isPrinted())
      .flagManualGeneration(assessments.isFlagManualGeneration())
      .operatorExternalUserId(assessments.getOperatorExternalUserId())
      //assessments detail
      .assessmentDetailId(assessmentsDetail.getAssessmentDetailId())
      .iuv(assessmentsDetail.getIuv())
      .iud(assessmentsDetail.getIud())
      .iur(assessmentsDetail.getIur())
      .debtorFiscalCodeHash(assessmentsDetail.getDebtorFiscalCodeHash())
      .paymentDateTime(assessmentsDetail.getPaymentDateTime())
      .officeCode(assessmentsDetail.getOfficeCode())
      .sectionCode(assessmentsDetail.getSectionCode())
      .assessmentCode(assessmentsDetail.getAssessmentCode())
      .amountCents(assessmentsDetail.getAmountCents())
      .amountSubmitted(assessmentsDetail.isAmountSubmitted())
      .receiptId(assessmentsDetail.getReceiptId())
      .build();
  }
}
