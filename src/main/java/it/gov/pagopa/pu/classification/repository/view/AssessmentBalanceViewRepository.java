package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.model.view.AssessmentBalanceView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "assessment-balance-view")
public interface AssessmentBalanceViewRepository extends Repository<AssessmentBalanceView, String> {

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
  List<AssessmentBalanceView> findClosedByOrganizationIdAndIuds(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Param("iuds") List<String> iuds);
}
