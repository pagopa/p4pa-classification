package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.dto.filters.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;


@RepositoryRestResource(path = "assessments")
public interface AssessmentsRepository extends JpaRepository<Assessments,Long> {

  Assessments findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(Long organizationId, String debtPositionTypeOrgCode, String assessmentName);

  @RestResource(exported = false)
  @Query("""
    SELECT distinct a
    FROM Assessments a
    LEFT JOIN AssessmentsDetail ad ON a.assessmentId = ad.assessmentId
    WHERE a.organizationId = :organizationId
    AND (:assessmentName IS NULL OR a.assessmentName ILIKE CONCAT('%', cast(:assessmentName as text), '%'))
    AND (cast(:#{#updateDateTimeIntervalFilter.from} AS STRING) IS NULL OR a.updateDate >= :#{#updateDateTimeIntervalFilter.from})
    AND (cast(:#{#updateDateTimeIntervalFilter.to} AS STRING) IS NULL OR a.updateDate <= :#{#updateDateTimeIntervalFilter.to})
    AND (:iuv IS NULL OR ad.iuv = :iuv)
    AND (:debtPositionTypeOrgCodes IS NULL OR a.debtPositionTypeOrgCode IN :debtPositionTypeOrgCodes)
    AND (:status IS NULL OR a.status = :status)
    """)
  Page<Assessments> findPagedAssessments(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @RequestParam(required = false) @Param("assessmentName") String assessmentName,
    @RequestParam(required = false) @Param("updateDateTimeIntervalFilter") LocalDateTimeIntervalFilter updateDateTimeIntervalFilter,
    @RequestParam(required = false) @Param("iuv") String iuv,
    @RequestParam(required = false) @Param("debtPositionTypeOrgCodes") Set<String> debtPositionTypeOrgCodes,
    @RequestParam(required = false) @Param("status") AssessmentStatus status,
    Pageable pageable
  );

  @RestResource(exported = false)
  @Modifying
  @Transactional
  @Query("UPDATE Assessments a SET a.status = :status WHERE a.assessmentId = :assessmentId AND a.organizationId = :organizationId AND a.flagManualGeneration IS TRUE")
  void updateStatus(AssessmentStatus status, Long assessmentId, Long organizationId);
}
