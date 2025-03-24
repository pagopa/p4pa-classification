package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;

public interface AssessmentsDetailService {
  void createAssessmentDetail(Assessments assessments, InstallmentNoPIIResponse installmentNoPIIResponse);
}
