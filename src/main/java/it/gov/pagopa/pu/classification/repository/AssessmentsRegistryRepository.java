package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@RepositoryRestResource(path = "assessments-registries")
public interface AssessmentsRegistryRepository extends JpaRepository<AssessmentsRegistry,Long> {
    @SuppressWarnings("squid:S107")// Suppressing too many parameters warning: it's allowed in query methods
    @Query("""
            select a
            from AssessmentsRegistry a
            where
                a.organizationId = :organizationId
                AND a.debtPositionTypeOrgCode IN :debtPositionTypeOrgCodes
                AND (:sectionCode is null or a.sectionCode = :sectionCode )
                AND (:sectionDescription is null or a.sectionDescription = :sectionDescription )
                AND (:officeCode is null or a.officeCode = :officeCode )
                AND (:officeDescription is null or a.officeDescription = :officeDescription )
                AND (:assessmentCode is null or a.assessmentCode = :assessmentCode )
                AND (:assessmentDescription is null or a.assessmentDescription = :assessmentDescription )
                AND (:operatingYear is null or a.operatingYear = :operatingYear )
                AND (:status is null or a.status = :status )
            """)
    Page<AssessmentsRegistry> findAssessmentsRegistriesByFilters(
            @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
            @Parameter(required = true, array = @ArraySchema(uniqueItems = true, schema = @Schema(type = "String"))) @Param("debtPositionTypeOrgCodes") Set<String> debtPositionTypeOrgCodes,
            String sectionCode,
            String sectionDescription,
            String officeCode,
            String officeDescription,
            String assessmentCode,
            String assessmentDescription,
            String operatingYear,
            AssessmentsRegistryStatus status,
            Pageable pageable
    );

    @SuppressWarnings("squid:S107")// Suppressing too many parameters warning: it's allowed in query methods
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
    """, nativeQuery = true)
    void insertIfNotExists(
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

    @RestResource(exported = false)
    @Modifying
    @Transactional
    @Query("UPDATE AssessmentsRegistry a SET a.status = :status WHERE a.organizationId = :organizationId and a.debtPositionTypeOrgCode = :debtPositionTypeOrgCode and a.operatingYear = :operatingYear")
    void updateStatus(AssessmentsRegistryStatus status, Long organizationId, String debtPositionTypeOrgCode, String operatingYear);
}
