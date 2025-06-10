package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "assessments-registries")
public interface AssessmentsRegistryRepository extends JpaRepository<AssessmentsRegistry,Long> {

  @Query(value = """
    INSERT INTO assessments_registry (
        section_code, section_description, office_code, office_description,
        assessment_code, assessment_description, debt_position_type_org_code,
        organization_id, operating_year, status, update_operator_external_id, update_trace_id)
    SELECT
        :sectionCode, :sectionDescription, :officeCode, :officeDescription,
        :assessmentCode, :assessmentDescription, :debtPositionTypeOrgCode,
        :organizationId, :operatingYear,
        CASE WHEN NOT EXISTS (
            SELECT 1 FROM assessments_registry a
            WHERE a.debt_position_type_org_code = :debtPositionTypeOrgCode
              AND a.organization_id = :organizationId
              AND a.operating_year = :operatingYear
              AND a.status = 'ACTIVE'
        ) THEN 'ACTIVE' ELSE 'INACTIVE' END,
        :userExternalId, :traceId
    WHERE NOT EXISTS (
        SELECT 1 FROM assessments_registry a2
        WHERE a2.organization_id = :organizationId
          AND a2.debt_position_type_org_code = :debtPositionTypeOrgCode
          AND a2.section_code = :sectionCode
          AND ((a2.office_code IS NULL AND :officeCode IS NULL) OR (a2.office_code = :officeCode))
          AND ((a2.assessment_code IS NULL AND :assessmentCode IS NULL) OR (a2.assessment_code = :assessmentCode))
          AND a2.operating_year = :operatingYear
    )
    RETURNING assessment_registry_id
    """, nativeQuery = true)
  Long insertIfNotExists(
    @Param("organizationId") Long organizationId,
    @Param("debtPositionTypeOrgCode") String debtPositionTypeOrgCode,
    @Param("sectionCode") String sectionCode,
    @Param("sectionDescription") String sectionDescription,
    @Param("officeCode") String officeCode,
    @Param("officeDescription") String officeDescription,
    @Param("assessmentCode") String assessmentCode,
    @Param("assessmentDescription") String assessmentDescription,
    @Param("operatingYear") String operatingYear,
    @Param("userExternalId") String userExternalId,
    @Param("traceId") String traceId
  );
}
