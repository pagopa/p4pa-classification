package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "assessments-details")
public interface AssessmentsDetailRepository extends JpaRepository<AssessmentsDetail,Long> {

  AssessmentsDetail getByAssessmentIdAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(Long assessmentId, String iuv, String iud,String officeCode, String sectionCode, String assessmentCode);

}
