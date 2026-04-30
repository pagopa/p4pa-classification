package it.gov.pagopa.pu.classification.util.faker;

import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;

public class AssessmentsFaker {

  public static Assessments buildAssessments(){
    return Assessments.builder()
      .assessmentId(1L)
      .organizationId(2L)
      .debtPositionTypeOrgCode("code123")
      .status(AssessmentStatus.ACTIVE)
      .assessmentName("assessmentName123")
      .printed(false)
      .flagManualGeneration(false)
      .build();
  }

}
