package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.model.view.FullClassificationViewNoPII;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "classifications-export-full-view")
public interface FullClassificationViewNoPIIDTORepository extends Repository<FullClassificationViewNoPII, Long> {
  @RestResource(exported = false)
  @Query(value = """
    SELECT new FullClassificationViewNoPII(
      c.classificationId as classificationId,
      c.installmentIngestionFlowFileName as receiptFileName,
      c.iud as receiptIud,
      c.iuv as receiptIuv,
      c.receiptOrgFiscalCode as receiptOrgFiscalCode,
      c.receiptPaymentReceiptId as receiptPaymentReceiptId,
      c.paymentDateTime as receiptPaymentDateTime,
      c.receiptPaymentRequestId as receiptPaymentRequestId,
      c.receiptIdPsp as receiptIdPsp,
      c.receiptPspCompanyName as receiptPspCompanyName,
      c.organizationEntityType as receiptOrgEntityType,
      c.organizationName as receiptBeneficiaryOrgName,
      c.receiptPersonalDataId as receiptPersonalDataId,
      c.receiptPaymentOutcomeCode as receiptPaymentOutcomeCode,
      c.receiptPaymentAmount as receiptPaymentAmount,
      c.receiptCreditorReferenceId as receiptCreditorReferenceId,
      c.transferAmount as receiptTransferAmount,
      c.remittanceInformation as receiptTransferRemittanceInformation,
      c.transferCategory as receiptTransferCategory,
      c.debtPositionTypeOrgCode as receiptDebtPositionTypeOrgCode,
      c.receiptCreationDate as receiptCreationDate,
      c.installmentBalance as receiptInstallmentBalance,
      pr.iuf as paymentsReportingIuf,
      pr.flowDateTime as paymentsReportingFlowDateTime,
      pr.regulationUniqueIdentifier as paymentsReportingRegulationUniqueIdentifier,
      pr.regulationDate as paymentsReportingRegulationDate,
      pr.senderPspType as paymentsReportingSenderPspType,
      pr.senderPspCode as paymentsReportingSenderPspCode,
      pr.senderPspName as paymentsReportingSenderPspName,
      pr.receiverOrganizationType as paymentsReportingReceiverOrganizationType,
      pr.receiverOrganizationCode as paymentsReportingReceiverOrganizationCode,
      pr.receiverOrganizationName as paymentsReportingReceiverOrganizationName,
      pr.totalPayments as paymentsReportingTotalPayments,
      pr.totalAmountCents as paymentsReportingTotalAmountCents,
      pr.iuv as paymentsReportingIuv,
      pr.iur as paymentsReportingIur,
      pr.amountPaidCents as paymentsReportingAmountPaidCents,
      pr.paymentOutcomeCode as paymentsReportingPaymentOutcomeCode,
      pr.payDate as paymentsReportingPayDate,
      pr.acquiringDate as paymentsReportingCreationDate,
      t.abiCode as treasuryAbiCode,
      t.cabCode as treasuryCabCode,
      t.accountRegistryCode as treasuryAccountRegistryCode,
      t.billDate as treasuryBillDate,
      t.regionValueDate as treasuryRegionValueDate,
      t.billAmountCents as treasuryBillAmountCents,
      t.sealCode as treasurySignCode,
      t.remittanceCode as treasuryRemittanceCode,
      t.pspLastName as treasuryPspLastName,
      t.iuf as treasuryIuf,
      t.iuv as treasuryIuv,
      t.creationDate as treasuryCreationDate,
      t.billYear as treasuryBillYear,
      t.billCode as treasuryBillCode,
      t.domainIdCode as treasuryDomainIdCode,
      t.receptionDate as treasuryReceptionDate,
      t.documentYear as treasuryDocumentYear,
      t.documentCode as treasuryDocumentCode,
      t.provisionalAe as treasuryProvisionalAe,
      t.provisionalCode as treasuryProvisionalCode,
      t.actualSuspensionDate as treasuryActualSuspensionDate,
      t.managementProvisionalCode as treasuryManagementProvisionalCode,
      c.label as classificationLabel,
      c.lastClassificationDate as lastClassificationDate,
      pn.ingestionFlowFileId as paymentNotificationIngestionFlowFileId,
      pn.iud as paymentNotificationIud,
      pn.iuv as paymentNotificationIuv,
      pn.paymentExecutionDate as paymentNotificationPaymentExecutionDate,
      pn.paymentType as paymentNotificationPaymentType,
      pn.amountPaidCents as paymentNotificationAmountPaidCents,
      pn.paCommissionCents as paymentNotificationPaCommissionCents,
      pn.remittanceInformation as paymentNotificationRemittanceInformation,
      pn.transferCategory as paymentNotificationTransferCategory,
      pn.debtPositionTypeOrgCode as paymentNotificationDebtPositionTypeOrgCode,
      pn.balance as paymentNotificationBalance,
      pn.personalDataId as paymentNotificationPersonalDataId
    )
    FROM Classification c
    LEFT JOIN PaymentsReporting pr ON c.paymentsReportingId = pr.paymentsReportingId
    LEFT JOIN Treasury t ON c.treasuryId = t.treasuryId
    LEFT JOIN PaymentNotificationNoPII pn ON c.paymentNotificationId = pn.paymentNotificationId
    WHERE c.organizationId = :organizationId
    AND (:#{#filter.iud} IS NULL OR c.iud = :#{#filter.iud})
    AND (:#{#filter.iuv} IS NULL OR c.iuv = :#{#filter.iuv})
    AND (:#{#filter.iuf} IS NULL OR c.iuf = :#{#filter.iuf})
    AND (:#{#filter.iur} IS NULL OR c.iur = :#{#filter.iur})
    AND (CAST(:#{#filter.paymentDateTime.from} AS STRING) IS NULL OR c.receiptPaymentDateTime >= :#{#filter.paymentDateTime.from})
    AND (CAST(:#{#filter.paymentDateTime.to} AS STRING) IS NULL OR c.receiptPaymentDateTime <= :#{#filter.paymentDateTime.to})
    AND (CAST(:#{#filter.regulationDate.from} AS STRING) IS NULL OR pr.regulationDate >= :#{#filter.regulationDate.from})
    AND (CAST(:#{#filter.regulationDate.to} AS STRING) IS NULL OR pr.regulationDate <= :#{#filter.regulationDate.to})
    AND (CAST(:#{#filter.billDate.from} AS STRING) IS NULL OR t.billDate >= :#{#filter.billDate.from})
    AND (CAST(:#{#filter.billDate.to} AS STRING) IS NULL OR t.billDate <= :#{#filter.billDate.to})
    AND (CAST(:#{#filter.regionValueDate.from} AS STRING) IS NULL OR t.regionValueDate >= :#{#filter.regionValueDate.from})
    AND (CAST(:#{#filter.regionValueDate.to} AS STRING) IS NULL OR t.regionValueDate <= :#{#filter.regionValueDate.to})
    AND (CAST(:#{#filter.payDate.from} AS STRING) IS NULL OR pr.payDate >= :#{#filter.payDate.from})
    AND (CAST(:#{#filter.payDate.to} AS STRING) IS NULL OR pr.payDate <= :#{#filter.payDate.to})
    AND (CAST(:#{#filter.lastClassificationDate.from} AS STRING) IS NULL OR c.lastClassificationDate >= :#{#filter.lastClassificationDate.from})
    AND (CAST(:#{#filter.lastClassificationDate.to} AS STRING) IS NULL OR c.lastClassificationDate <= :#{#filter.lastClassificationDate.to})
    AND (:#{#filter.regulationUniqueIdentifier} IS NULL OR pr.regulationUniqueIdentifier = :#{#filter.regulationUniqueIdentifier})
    AND (:#{#filter.accountRegistryCode} IS NULL OR t.accountRegistryCode = :#{#filter.accountRegistryCode})
    AND (:#{#filter.billAmountCents} IS NULL OR t.billAmountCents = :#{#filter.billAmountCents})
    AND (:#{#filter.remittanceInformation} IS NULL OR c.remittanceInformation = :#{#filter.remittanceInformation})
    AND (:#{#filter.pspCompanyName} IS NULL OR c.receiptPspCompanyName = :#{#filter.pspCompanyName})
    AND (:#{#filter.pspLastName} IS NULL OR c.pspLastName = :#{#filter.pspLastName})
    AND (c.debtPositionTypeOrgCode IS NULL OR c.debtPositionTypeOrgCode IN (:debtPositionTypeOrgCodes))
    """)
  Page<FullClassificationViewNoPII> findFullClassificationViewNoPIIDTO(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("filter") ExportClassificationsFilterDTO exportClassificationsFilterDTO,
    @Parameter(required = true) @Param("debtPositionTypeOrgCodes") List<String> debtPositionTypeOrgCodes,
    Pageable pageable);
}
