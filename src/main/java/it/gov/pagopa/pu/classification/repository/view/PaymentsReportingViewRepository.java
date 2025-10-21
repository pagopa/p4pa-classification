package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.model.view.PaymentsReportingView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;

@RepositoryRestResource(path = "payments-reporting-view")
public interface PaymentsReportingViewRepository extends Repository<PaymentsReportingView, String> {

  @Query(value = """
      SELECT DISTINCT ON (iuf, regulation_unique_identifier) *
      FROM payments_reporting
      WHERE organization_id = :organizationId
        AND (:iuf IS NULL OR iuf = :iuf)
        AND (:iuv IS NULL OR iuv = :iuv)
        AND (:regulationUniqueIdentifier IS NULL OR regulation_unique_identifier = :regulationUniqueIdentifier)
        AND (regulation_date >= COALESCE(CAST(:regulationDateFrom AS date), regulation_date))
        AND (regulation_date <= COALESCE(CAST(:regulationDateTo AS date), regulation_date))
      ORDER BY iuf, regulation_unique_identifier, regulation_date DESC
    """, nativeQuery = true)
  Page<PaymentsReportingView> findDistinctByIufAndRegulationUniqueIdentifier(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Param("iuf") String iuf,
    @Param("regulationUniqueIdentifier") String regulationUniqueIdentifier,
    @Param("regulationDateFrom") LocalDate regulationDateFrom,
    @Param("regulationDateTo") LocalDate regulationDateTo,
    @Param("iuv") String iuv,
    Pageable pageable);

}
