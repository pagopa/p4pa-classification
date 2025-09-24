package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;

import java.util.List;

public interface AssessmentsDetailService {
  void createAssessmentDetail(Assessments assessments, ReceiptNoPII receipt, InstallmentNoPII installmentNoPII);

  List<AssessmentsDetail> createAssessmentsDetail(Long organizationId, Long assessmentId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken);

  void deleteAssessmentDetail(String debtPositionTypeOrgCode, String iuv, String iud);
}
