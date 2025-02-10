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
    + "AND (:regulationUniqueIdentifier IS NULL OR p.regulationUniqueIdentifier = :regulationUniqueIdentifier) "
    + "AND ((:fromDate IS NULL AND :toDate IS NULL) OR (p.regulationDate BETWEEN :fromDate AND :toDate))")
  Page<PaymentsReportingView> findDistinctByIufAndRegulationUniqueIdentifier(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Param("iuf") String iuf,
    @Param("regulationUniqueIdentifier") String regulationUniqueIdentifier,
    @Param("fromDate") LocalDate fromDate,
    @Param("toDate") LocalDate toDate,
    Pageable pageable);

}
