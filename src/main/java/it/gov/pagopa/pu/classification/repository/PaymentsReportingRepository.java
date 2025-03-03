package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "payments-reporting")
public interface PaymentsReportingRepository extends JpaRepository<PaymentsReporting,String> {

  List<PaymentsReporting> findByOrganizationIdAndIuf(Long organizationId, String iuf);

  @Query("SELECT p FROM PaymentsReporting p WHERE " +
    "p.organizationId=:organizationId AND " +
    "p.iuv=:iuv AND " +
    "p.iur=:iur AND " +
    "p.transferIndex=:transferIndex")
  PaymentsReporting findBySemanticKey(Long organizationId, String iuv, String iur, int transferIndex);

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
    @Param("iuv") String iuv,
    @Param("payDateFrom") LocalDate payDateFrom,
    @Param("payDateTo") LocalDate payDateTo,
    Pageable pageable);
}
