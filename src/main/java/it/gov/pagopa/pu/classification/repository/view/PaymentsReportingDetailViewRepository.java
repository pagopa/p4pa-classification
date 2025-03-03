package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.model.view.PaymentsReportingDetailView;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "payments-reporting-detail-view")
public interface PaymentsReportingDetailViewRepository extends Repository<PaymentsReportingDetailView, String> {

  @Query("""
    SELECT p
    FROM PaymentsReportingDetailView p
    WHERE p.organizationId = :organizationId
      AND p.iuf = :iuf
      AND (:iuv IS NULL OR p.iuv = :iuv)
      AND (cast(:payDateFrom AS DATE) IS NULL OR p.payDate >= :payDateFrom)
      AND (cast(:payDateTo AS DATE) IS NULL OR p.payDate <= :payDateTo)
    """)
  Page<PaymentsReportingDetailView> findPaymentsReportingDetailByFilters(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("iuf") String iuf,
    @Param("iuv") String iuv,
    @Param("payDateFrom") LocalDate payDateFrom,
    @Param("payDateTo") LocalDate payDateTo,
    Pageable pageable);

}
