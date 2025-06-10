package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "assessments-registries")
public interface AssessmentsRegistryRepository extends JpaRepository<AssessmentsRegistry,Long> {

  @Query("SELECT a FROM AssessmentsRegistry a WHERE "
    + "(:debtPositionTypeOrgCode IS NULL OR a.debtPositionTypeOrgCode = :debtPositionTypeOrgCode) AND "
    + "(:organizationId IS NULL OR a.organizationId = :organizationId) AND "
    + "(:operatingYear IS NULL OR a.operatingYear = :operatingYear) AND "
    + "(:status IS NULL OR a.status = :status)")
  AssessmentsRegistry findAssessmentsRegistryByFilters(String debtPositionTypeOrgCode,
    Long organizationId,
    String operatingYear,
    String status);
}
