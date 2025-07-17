package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;


@RepositoryRestResource(path = "assessments")
public interface AssessmentsRepository extends JpaRepository<Assessments,Long> {

  Assessments findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(Long organizationId, String debtPositionTypeOrgCode, String assessmentName);

  @RestResource(exported = false)
  @Query("""
    FROM Assessments a
    LEFT JOIN AssessmentsDetail ad ON a.assessmentId = ad.assessmentId
    WHERE
      (:assessmentName IS NULL OR a.assessmentName = :assessmentName)
      AND (cast(:#{#updateDateTimeIntervalFilter.from} AS STRING) IS NULL OR a.updateDate >= :#{#updateDateTimeIntervalFilter.from})
      AND (cast(:#{#updateDateTimeIntervalFilter.to} AS STRING) IS NULL OR a.updateDate <= :#{#updateDateTimeIntervalFilter.to})
      AND (:iuv IS NULL OR ad.iuv = :iuv)
      AND (:debtPositionTypeOrgCodes IS NULL OR a.debtPositionTypeOrgCode IN :debtPositionTypeOrgCodes)
      AND (:status IS NULL OR a.status = :status)
    """)
  Page<Assessments> findPagedAssessments(
    @Param("assessmentName") String assessmentName,
    @Param("updateDateTimeIntervalFilter") LocalDateTimeIntervalFilter updateDateTimeIntervalFilter,
    @Param("iuv") String iuv,
    @Param("debtPositionTypeOrgCodes") Set<String> debtPositionTypeOrgCodes,
    @Param("status") AssessmentStatus status,
    Pageable pageable
  );

}
