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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@RepositoryRestResource(path = "payments-reporting-with-receipt-view")
public interface PaymentsReportingWithReceiptViewRepository extends Repository<PaymentsReportingWithReceiptView, String> {

  @SuppressWarnings("squid:S107") // Suppressing too many parameters warning: it's allowed in query methods
  @Query("""
    SELECT DISTINCT new PaymentsReportingWithReceiptView(
      p.paymentsReportingId AS paymentsReportingId,
      p.ingestionFlowFileId AS ingestionFlowFileId,
      p.organizationId AS organizationId,
      p.iuv AS iuv,
      p.iur AS iur,
      p.transferIndex AS transferIndex,
      p.pspIdentifier AS pspIdentifier,
      p.iuf AS iuf,
      p.flowDateTime AS flowDateTime,
      p.regulationUniqueIdentifier AS regulationUniqueIdentifier,
      p.regulationDate AS regulationDate,
      p.senderPspType AS senderPspType,
      p.senderPspCode AS senderPspCode,
      p.senderPspName AS senderPspName,
      p.receiverOrganizationType AS receiverOrganizationType,
      p.receiverOrganizationCode AS receiverOrganizationCode,
      p.receiverOrganizationName AS receiverOrganizationName,
      p.totalPayments AS totalPayments,
      p.totalAmountCents AS totalAmountCents,
      p.amountPaidCents AS amountPaidCents,
      p.paymentOutcomeCode AS paymentOutcomeCode,
      p.payDate AS payDate,
      p.acquiringDate AS acquiringDate,
      p.bicCodePouringBank AS bicCodePouringBank,
      p.creationDate AS creationDate,
      p.updateDate AS updateDate,
      p.updateOperatorExternalId AS updateOperatorExternalId,
      p.updateTraceId AS updateTraceId,
      c.receiptPaymentRequestId AS receiptId,
      c.iud AS iud,
      c.debtPositionTypeOrgDescription AS debtPositionTypeOrgDescription
    )
    FROM PaymentsReportingWithReceiptView p
    LEFT JOIN Classification c ON p.paymentsReportingId = c.paymentsReportingId
    WHERE p.organizationId = :organizationId
      AND p.iuf = :iuf
      AND (:iuv IS NULL OR p.iuv = :iuv)
      AND (CAST(:payDateFrom AS DATE) IS NULL OR p.payDate >= :payDateFrom)
      AND (CAST(:payDateTo AS DATE) IS NULL OR p.payDate <= :payDateTo)
      AND (:debtPositionTypeOrgCode IS NULL OR c.debtPositionTypeOrgCode = :debtPositionTypeOrgCode)
      AND (:fiscalCode IS NULL OR c.debtorFiscalCodeHash = :#{@dataCipherService.hash(#fiscalCode)})
    """)
  Page<PaymentsReportingWithReceiptView> findPaymentsReportingByFilters(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("iuf") String iuf,
    @RequestParam(required = false) @Param("iuv") String iuv,
    @RequestParam(required = false) @Param("payDateFrom") LocalDate payDateFrom,
    @RequestParam(required = false) @Param("payDateTo") LocalDate payDateTo,
    @RequestParam(required = false) @Param("debtPositionTypeOrgCode") String debtPositionTypeOrgCode,
    @RequestParam(required = false) @Param("fiscalCode") String fiscalCode,
    Pageable pageable);
}
