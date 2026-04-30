package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.model.view.AssessmentsBalanceView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RepositoryRestResource(path = "assessments-balance-view")
public interface AssessmentsBalanceViewRepository extends Repository<AssessmentsBalanceView, String> {

  @Query("""
          SELECT new AssessmentsBalanceView(
            ad.officeCode,
            ad.debtPositionTypeOrgCode,
            ad.sectionCode,
            ad.assessmentCode,
            SUM(ad.amountCents) AS amountCents
          )
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
  List<AssessmentsBalanceView> findClosedByOrganizationIdAndIuds(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @RequestParam(required = false) @Param("iuds") List<String> iuds);

  @Query("""
    SELECT new AssessmentsBalanceView(
            ad.officeCode,
            ad.debtPositionTypeOrgCode,
            ad.sectionCode,
            ad.assessmentCode,
            SUM(ad.amountCents) AS amountCents
          )
          FROM  AssessmentsDetail ad
          JOIN  Assessments a
            ON  ad.assessmentId = a.assessmentId
          JOIN  Classification c
            ON  ad.iud = c.iud
            AND ad.organizationId = c.organizationId
          WHERE c.organizationId = :organizationId
            AND a.status = 'CLOSED'
            AND c.iuf = :iuf
            AND c.label = 'RT_IUF'
          GROUP BY  ad.officeCode,
                    ad.debtPositionTypeOrgCode,
                    ad.sectionCode,
                    ad.assessmentCode
          ORDER BY  ad.officeCode,
                    ad.debtPositionTypeOrgCode,
                    ad.sectionCode,
                    ad.assessmentCode
    """)
  List<AssessmentsBalanceView> findClosedByOrganizationIdAndIuf(
  @Parameter(required = true) @Param("organizationId") Long organizationId,
  @Parameter(required = true) @Param("iuf") String iuf);

  @Query("""
    SELECT new AssessmentsBalanceView(
            ad.officeCode,
            ad.debtPositionTypeOrgCode,
            ad.sectionCode,
            ad.assessmentCode,
            SUM(ad.amountCents) AS amountCents
          )
          FROM  AssessmentsDetail ad
          JOIN  Assessments a
            ON  ad.assessmentId = a.assessmentId
          JOIN  Classification c
            ON  ad.iud = c.iud
            AND ad.organizationId = c.organizationId
          WHERE c.organizationId = :organizationId
            AND a.status = 'CLOSED'
            AND c.billCode = :billCode
            AND c.billYear = :billYear
            AND c.label = 'RT_IUF_TES'
          GROUP BY  ad.officeCode,
                    ad.debtPositionTypeOrgCode,
                    ad.sectionCode,
                    ad.assessmentCode
          ORDER BY  ad.officeCode,
                    ad.debtPositionTypeOrgCode,
                    ad.sectionCode,
                    ad.assessmentCode
    """)
  List<AssessmentsBalanceView> findClosedByOrganizationIdAndBill(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("billCode") String billCode,
    @Parameter(required = true) @Param("billYear") String billYear);
}
