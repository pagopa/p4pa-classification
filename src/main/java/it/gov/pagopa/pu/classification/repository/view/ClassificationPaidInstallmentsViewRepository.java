package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.model.view.ClassificationPaidInstallmentsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource(path = "classifications-paid-installments-view")
public interface ClassificationPaidInstallmentsViewRepository extends Repository<ClassificationPaidInstallmentsView, String> {
  @Query("""
    SELECT distinct new ClassificationPaidInstallmentsView(c.iud,c.iuv,c.paymentDateTime, c.updateDate, c.receiptPaymentRequestId, c.organizationId)
    FROM ClassificationPaidInstallmentsView c
    WHERE c.organizationId = :organizationId
    AND (:iuv IS NULL OR c.iuv = :iuv)
    AND (cast(:#{#paymentDateTimeIntervalFilter.from} AS STRING) IS NULL OR c.paymentDateTime >= :#{#paymentDateTimeIntervalFilter.from})
    AND (cast(:#{#paymentDateTimeIntervalFilter.to} AS STRING) IS NULL OR c.paymentDateTime <= :#{#paymentDateTimeIntervalFilter.to})
    AND (cast(:#{#updateDateTimeIntervalFilter.from} AS STRING) IS NULL OR c.updateDate >= :#{#updateDateTimeIntervalFilter.from})
    AND (cast(:#{#updateDateTimeIntervalFilter.to} AS STRING) IS NULL OR c.updateDate <= :#{#updateDateTimeIntervalFilter.to})
    AND (:iuds IS NULL OR c.iud NOT IN :iuds)
    AND c.iud IS NOT NULL
    AND c.iuv IS NOT NULL
    AND c.paymentDateTime IS NOT NULL
    AND c.updateDate IS NOT NULL
    AND c.receiptPaymentRequestId IS NOT NULL
    """)
  Page<ClassificationPaidInstallmentsView> findPaidInstallments(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Param("iuv") String iuv,
    @Param("paymentDateTimeIntervalFilter") OffsetDateTimeIntervalFilter paymentDateTimeIntervalFilter,
    @Param("updateDateTimeIntervalFilter") LocalDateTimeIntervalFilter updateDateTimeIntervalFilter,
    @Param("iuds") Set<String> iuds,
    Pageable pageable
  );
}
