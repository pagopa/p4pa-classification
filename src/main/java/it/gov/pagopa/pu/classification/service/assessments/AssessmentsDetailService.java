package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;

public interface AssessmentsDetailService {
  void createAssessmentDetail(Assessments assessments, ReceiptNoPII receipt, InstallmentNoPII installmentNoPII);
}
