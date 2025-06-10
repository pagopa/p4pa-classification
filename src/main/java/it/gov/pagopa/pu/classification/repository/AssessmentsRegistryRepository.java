package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "assessments-registries")
public interface AssessmentsRegistryRepository extends JpaRepository<AssessmentsRegistry,Long> {

  @Query(value = """
    WITH input_values AS (
      SELECT NEXTVAL('assessment_registry_id_seq') AS assessment_registry_id, :sectionCode AS section_code, :sectionDescription AS section_description,
      :officeCode AS office_code, :officeDescription AS office_description, :assessmentCode AS assessment_code, :assessmentDescription AS assessment_description,
      :debtPositionTypeOrgCode AS debt_position_type_org_code, :organizationId AS organization_id, :operatingYear AS operating_year,
      :userExternalId AS update_operator_external_id
     ), ins AS (
      INSERT INTO assessments_registry (assessment_registry_id, section_code, section_description, office_code, office_description,
       assessment_code, assessment_description, debt_position_type_org_code, organization_id, operating_year, status, update_operator_external_id)
      SELECT
        i.assessment_registry_id, i.section_code, i.section_description, i.office_code, i.office_description, i.assessment_code, i.assessment_description,
        i.debt_position_type_org_code, i.organization_id, i.operating_year,
        CASE WHEN NOT EXISTS (SELECT 1 FROM assessments_registry a
                               WHERE a.debt_position_type_org_code = i.debt_position_type_org_code
                               AND a.organization_id = i.organization_id
                               AND a.operating_year = i.operating_year AND a.status = 'ACTIVE')
             THEN 'ACTIVE' ELSE 'INACTIVE' END,
        i.update_operator_external_id
      FROM input_values i
      WHERE NOT EXISTS (SELECT 1 FROM assessments_registry a2
                          WHERE a2.organization_id = i.organization_id
                          AND a2.debt_position_type_org_code = i.debt_position_type_org_code
                          AND a2.section_code = i.section_code
                          AND ((a2.office_code IS NULL AND i.office_code IS NULL) OR (a2.office_code = i.office_code))
                          AND ((a2.assessment_code IS NULL AND i.assessment_code IS NULL) OR (a2.assessment_code = i.assessment_code))
                          AND a2.operating_year = i.operating_year)
      RETURNING assessment_registry_id
     ) SELECT assessment_registry_id FROM ins
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
    @Param("userExternalId") String userExternalId
  );
}
