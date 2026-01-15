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

  @SuppressWarnings("squid:S107") // Suppressing too many parameters warning: it's allowed in query methods
  @Query("""
    SELECT
      p.paymentsReportingId,
      p.ingestionFlowFileId,
      p.organizationId,
      p.iuv,
      p.iur,
      p.transferIndex,
      p.pspIdentifier,
      p.iuf,
      p.flowDateTime,
      p.regulationUniqueIdentifier,
      p.regulationDate,
      p.senderPspType,
      p.senderPspCode,
      p.senderPspName,
      p.receiverOrganizationType,
      p.receiverOrganizationCode,
      p.receiverOrganizationName,
      p.totalPayments,
      p.totalAmountCents,
      p.amountPaidCents,
      p.paymentOutcomeCode,
      p.payDate,
      p.acquiringDate,
      p.bicCodePouringBank,
      p.creationDate,
      p.updateDate,
      p.updateOperatorExternalId,
      p.updateTraceId,
      c.receiptPaymentRequestId,
      c.iud,
      c.debtPositionTypeOrgDescription
    FROM PaymentsReporting p
    LEFT JOIN Classification c ON p.paymentsReportingId = c.paymentsReportingId
    WHERE p.organizationId = :organizationId
      AND p.iuf = :iuf
      AND (:iuv IS NULL OR p.iuv = :iuv)
      AND (CAST(:payDateFrom AS DATE) IS NULL OR p.payDate >= CAST(:payDateFrom AS DATE))
      AND (CAST(:payDateTo AS DATE) IS NULL OR p.payDate <= CAST(:payDateTo AS DATE))
      AND (:debtPositionTypeOrgCode IS NULL OR c.debtPositionTypeOrgCode = :debtPositionTypeOrgCode)
      AND (:fiscalCode IS NULL OR c.debtorFiscalCodeHash = :#{@dataCipherService.hash(#fiscalCode)})
    """)
  Page<PaymentsReportingWithReceiptView> findPaymentsReportingByFilters(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("iuf") String iuf,
    @Param("iuv") String iuv,
    @Param("payDateFrom") LocalDate payDateFrom,
    @Param("payDateTo") LocalDate payDateTo,
    @Param("debtPositionTypeOrgCode") String debtPositionTypeOrgCode,
    @Param("fiscalCode") String fiscalCode,
    Pageable pageable);
}
