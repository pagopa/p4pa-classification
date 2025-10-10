package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.dto.ClassificationPaidInstallmentsFilterDTO;
import it.gov.pagopa.pu.classification.model.view.ClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.model.view.ClassificationPaidInstallmentsViewId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "classifications-paid-installments-view")
public interface ClassificationPaidInstallmentsViewRepository extends Repository<ClassificationPaidInstallmentsView, ClassificationPaidInstallmentsViewId> {
  @RestResource(exported = false)
  @Query("""
    SELECT distinct c
    FROM ClassificationPaidInstallmentsView c
    WHERE c.organizationId = :organizationId
    AND (:#{#filter.iuv} IS NULL OR c.iuv = :#{#filter.iuv})
    AND (CAST(:#{#filter.paymentDateTimeIntervalFilter.from} AS STRING) IS NULL OR c.paymentDateTime >= :#{#filter.paymentDateTimeIntervalFilter.from})
    AND (CAST(:#{#filter.paymentDateTimeIntervalFilter.to} AS STRING) IS NULL OR c.paymentDateTime <= :#{#filter.paymentDateTimeIntervalFilter.to})
    AND (CAST(:#{#filter.receiptCreationDateIntervalFilter.from} AS STRING) IS NULL OR c.receiptCreationDate >= :#{#filter.receiptCreationDateIntervalFilter.from})
    AND (CAST(:#{#filter.receiptCreationDateIntervalFilter.to} AS STRING) IS NULL OR c.receiptCreationDate <= :#{#filter.receiptCreationDateIntervalFilter.to})
    AND (c.debtPositionTypeOrgCode = :#{#filter.debtPositionTypeOrgCode})
    AND (:#{#filter.iuds} IS NULL OR c.iud NOT IN :#{#filter.iuds})
    AND c.iud IS NOT NULL
    AND c.iuv IS NOT NULL
    AND c.paymentDateTime IS NOT NULL
    AND c.receiptCreationDate IS NOT NULL
    AND c.receiptPaymentRequestId IS NOT NULL
    AND NOT EXISTS (SELECT ad FROM AssessmentsDetail ad where ad.iud = c.iud and ad.organizationId = :organizationId)
    """)
  Page<ClassificationPaidInstallmentsView> findPaidInstallments(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("filter")ClassificationPaidInstallmentsFilterDTO filter,
    Pageable pageable
  );
}
