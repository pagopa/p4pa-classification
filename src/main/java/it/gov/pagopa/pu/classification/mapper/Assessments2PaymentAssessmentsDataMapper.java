package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.PaymentAssessmentsDataDTO;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class Assessments2PaymentAssessmentsDataMapper {
  public PaymentAssessmentsDataDTO map(Assessments assessments, List<AssessmentsDetail> assessmentsDetail) {
    return PaymentAssessmentsDataDTO.builder()
      .assessmentId(assessments.getAssessmentId())
      .organizationId(assessments.getOrganizationId())
      .debtPositionTypeOrgCode(assessments.getDebtPositionTypeOrgCode())
      .status(assessments.getStatus())
      .assessmentName(assessments.getAssessmentName())
      .printed(assessments.isPrinted())
      .flagManualGeneration(assessments.isFlagManualGeneration())
      .operatorExternalUserId(assessments.getOperatorExternalUserId())
      .iuv(assessmentsDetail.getFirst().getIuv())
      .iud(assessmentsDetail.getFirst().getIud())
      .iur(assessmentsDetail.getFirst().getIur())
      //assessments detail
      .assessmentsDetailList(assessmentsDetail)
      .build();
  }
}
