package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.Assessments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "assessments")
public interface AssessmentsRepository extends JpaRepository<Assessments,Long> {

  Assessments getByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(Long organizationId, String debtPositionTypeOrgCode, String assessmentName);

}
