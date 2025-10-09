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

  @Query("SELECT distinct p FROM PaymentsReportingView p "
    + "WHERE p.organizationId = :organizationId "
    + "AND (:iuf IS NULL OR p.iuf = :iuf) "
    + "AND (:iuv IS NULL or p.iuv = :iuv) "
    + "AND (:regulationUniqueIdentifier IS NULL OR p.regulationUniqueIdentifier = :regulationUniqueIdentifier) "
    + "AND (cast(:regulationDateFrom AS DATE) IS NULL OR p.regulationDate >= :regulationDateFrom) "
    + "AND (cast(:regulationDateTo AS DATE) IS NULL OR p.regulationDate <= :regulationDateTo)")
  Page<PaymentsReportingView> findDistinctByIufAndRegulationUniqueIdentifierAndIuv(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Param("iuf") String iuf,
    @Param("regulationUniqueIdentifier") String regulationUniqueIdentifier,
    @Param("regulationDateFrom") LocalDate regulationDateFrom,
    @Param("regulationDateTo") LocalDate regulationDateTo,
    @Param("iuv") String iuv,
    Pageable pageable);

}
