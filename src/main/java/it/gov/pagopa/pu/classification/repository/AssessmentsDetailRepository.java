package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;


@RepositoryRestResource(path = "assessments-details")
public interface AssessmentsDetailRepository extends JpaRepository<AssessmentsDetail, Long> {

  @RestResource(exported = false)
  AssessmentsDetail findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(String debtPositionTypeOrgCode, String iuv, String iud, String officeCode, String sectionCode, String assessmentCode);
}
