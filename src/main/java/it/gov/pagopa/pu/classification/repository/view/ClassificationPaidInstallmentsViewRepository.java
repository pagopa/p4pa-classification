package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.dto.ClassificationPaidInstallmentsFilterDTO;
import it.gov.pagopa.pu.classification.model.view.ClassificationPaidInstallmentsView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "classifications-paid-installments-view")
public interface ClassificationPaidInstallmentsViewRepository extends Repository<ClassificationPaidInstallmentsView, String> {
  @RestResource(exported = false)
  @Query("""
    SELECT distinct new ClassificationPaidInstallmentsView(
    c.iud,
    c.iuv,
    c.paymentDateTime,
    c.updateDate,
    c.receiptPaymentRequestId,
    c.organizationId,
    c.debtPositionTypeOrgCode,
    c.transferAmount AS amount)
    FROM Classification c
    WHERE c.organizationId = :organizationId
    AND (:#{#filter.iuv} IS NULL OR c.iuv = :#{#filter.iuv})
    AND (CAST(:#{#filter.paymentDateTimeIntervalFilter.from} AS STRING) IS NULL OR c.paymentDateTime >= :#{#filter.paymentDateTimeIntervalFilter.from})
    AND (CAST(:#{#filter.paymentDateTimeIntervalFilter.to} AS STRING) IS NULL OR c.paymentDateTime <= :#{#filter.paymentDateTimeIntervalFilter.to})
    AND (CAST(:#{#filter.updateDateTimeIntervalFilter.from} AS STRING) IS NULL OR c.updateDate >= :#{#filter.updateDateTimeIntervalFilter.from})
    AND (CAST(:#{#filter.updateDateTimeIntervalFilter.to} AS STRING) IS NULL OR c.updateDate <= :#{#filter.updateDateTimeIntervalFilter.to})
    AND (c.debtPositionTypeOrgCode = :#{#filter.debtPositionTypeOrgCode})
    AND (:#{#filter.iuds} IS NULL OR c.iud NOT IN :#{#filter.iuds})
    AND c.iud IS NOT NULL
    AND c.iuv IS NOT NULL
    AND c.paymentDateTime IS NOT NULL
    AND c.updateDate IS NOT NULL
    AND c.receiptPaymentRequestId IS NOT NULL
    """)
  Page<ClassificationPaidInstallmentsView> findPaidInstallments(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("filter")ClassificationPaidInstallmentsFilterDTO filter,
    Pageable pageable
  );
}
