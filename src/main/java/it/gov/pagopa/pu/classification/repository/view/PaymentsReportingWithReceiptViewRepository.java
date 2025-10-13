package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.model.view.PaymentsReportingWithReceiptView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;

@RepositoryRestResource(path = "payments-reporting-with-receipt-view")
public interface PaymentsReportingWithReceiptViewRepository extends Repository<PaymentsReportingWithReceiptView, String> {
  @Query("""
    SELECT p
    FROM PaymentsReportingWithReceiptView p
    WHERE p.organizationId = :organizationId
      AND p.iuf = :iuf
      AND (:iuv IS NULL OR p.iuv = :iuv)
      AND (cast(:payDateFrom AS DATE) IS NULL OR p.payDate >= :payDateFrom)
      AND (cast(:payDateTo AS DATE) IS NULL OR p.payDate <= :payDateTo)
    """)
  Page<PaymentsReportingWithReceiptView> findPaymentsReportingByFilters(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("iuf") String iuf,
    String iuv,
    LocalDate payDateFrom,
    LocalDate payDateTo,
    Pageable pageable);
}
