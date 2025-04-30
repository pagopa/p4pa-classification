package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;


@RepositoryRestResource(path = "payments-reporting")
public interface PaymentsReportingRepository extends JpaRepository<PaymentsReporting,String> {

  List<PaymentsReporting> findByOrganizationIdAndIuf(Long organizationId, String iuf);

  PaymentsReporting findByOrganizationIdAndPaymentsReportingId(Long organizationId, String paymentsReportingId);

  @Query("SELECT p FROM PaymentsReporting p WHERE " +
    "p.organizationId=:organizationId AND " +
    "p.iuv=:iuv AND " +
    "p.iur=:iur AND " +
    "p.transferIndex=:transferIndex")
  List<PaymentsReporting> findByTransferSemanticKey (Long organizationId, String iuv, String iur, int transferIndex);

  @Query("""
    SELECT p
    FROM PaymentsReporting p
    WHERE p.organizationId = :organizationId
      AND p.iuf = :iuf
      AND (:iuv IS NULL OR p.iuv = :iuv)
      AND (cast(:payDateFrom AS DATE) IS NULL OR p.payDate >= :payDateFrom)
      AND (cast(:payDateTo AS DATE) IS NULL OR p.payDate <= :payDateTo)
    """)
  Page<PaymentsReporting> findPaymentsReportingByFilters(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("iuf") String iuf,
    String iuv,
    LocalDate payDateFrom,
    LocalDate payDateTo,
    Pageable pageable);
}
