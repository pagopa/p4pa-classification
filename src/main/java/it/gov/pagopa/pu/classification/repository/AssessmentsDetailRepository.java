package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.dto.BalanceDTO;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;


@RepositoryRestResource(path = "assessments-details")
public interface AssessmentsDetailRepository extends JpaRepository<AssessmentsDetail, Long> {

  @RestResource(exported = false)
  AssessmentsDetail findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(String debtPositionTypeOrgCode, String iuv, String iud, String officeCode, String sectionCode, String assessmentCode);

  @Query("""
          SELECT
            ad.officeCode,
            ad.debtPositionTypeOrgCode,
            ad.sectionCode,
            ad.assessmentCode,
            SUM(ad.amountCents) AS amountCents
          FROM AssessmentsDetail ad
          JOIN Assessments a
            ON ad.assessmentId = a.assessmentId
          WHERE ad.organizationId = :organizationId
            AND a.status = 'CLOSED'
            AND ad.iud IN :iuds
          GROUP BY  ad.officeCode,
                    ad.debtPositionTypeOrgCode,
                    ad.sectionCode,
                    ad.assessmentCode
          ORDER BY  ad.officeCode,
                    ad.debtPositionTypeOrgCode,
                    ad.sectionCode,
                    ad.assessmentCode
    """)
  List<BalanceDTO> findByOrganizationIdAndIuds(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Param("iuds") List<String> iuds);
}
