package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.dto.TreasuredClassificationFilterDTO;
import it.gov.pagopa.pu.classification.model.view.TreasuredClassification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "classifications-list")
public interface TreasuredClassificationRepository extends Repository<TreasuredClassification, Long> {
  @RestResource(exported = false)
  @Query(value = """
    SELECT new TreasuredClassification(
       c.classificationId as classificationId,
       c.organizationId as organizationId,
       c.transferId as transferId,
       c.paymentNotificationId as paymentNotificationId,
       c.paymentsReportingId as paymentsReportingId,
       c.treasuryId as treasuryId,
       c.iuf as iuf,
       c.iud as iud,
       c.iuv as iuv,
       c.iur as iur,
       c.transferIndex as transferIndex,
       c.label as label,
       c.lastClassificationDate as lastClassificationDate,
       c.payDate as payDate,
       c.paymentDateTime as paymentDateTime,
       c.regulationDate as regulationDate,
       c.billDate as billDate,
       c.regionValueDate as regionValueDate,
       c.regulationUniqueIdentifier as regulationUniqueIdentifier,
       c.accountRegistryCode as accountRegistryCode,
       c.billAmountCents as billAmountCents,
       c.remittanceInformation as remittanceInformation,
       c.pspCompanyName as pspCompanyName,
       c.pspLastName as pspLastName,
       c.debtPositionTypeOrgCode as debtPositionTypeOrgCode,
       c.installmentIngestionFlowFileName as installmentIngestionFlowFileName,
       c.receiptOrgFiscalCode as receiptOrgFiscalCode,
       c.receiptPaymentReceiptId as receiptPaymentReceiptId,
       c.receiptPaymentDateTime as receiptPaymentDateTime,
       c.receiptPaymentRequestId as receiptPaymentRequestId,
       c.receiptIdPsp as receiptIdPsp,
       c.receiptPspCompanyName as receiptPspCompanyName,
       c.organizationEntityType as organizationEntityType,
       c.organizationName as organizationName,
       c.receiptPersonalDataId as receiptPersonalDataId,
       c.receiptPaymentOutcomeCode as receiptPaymentOutcomeCode,
       c.receiptPaymentAmount as receiptPaymentAmount,
       c.receiptCreditorReferenceId as receiptCreditorReferenceId,
       c.transferAmount as transferAmount,
       c.transferCategory as transferCategory,
       c.receiptCreationDate as receiptCreationDate,
       c.installmentBalance as installmentBalance,
       t.billYear as treasuryBillYear,
       t.billCode as treasuryBillCode,
       t.ingestionFlowFileId as treasuryIngestionFlowFileId,
       t.iuf as treasuryIuf,
       t.iuv as treasuryIuv,
       t.accountCode as treasuryAccountCode,
       t.domainIdCode as treasuryDomainIdCode,
       t.transactionTypeCode as treasuryTransactionTypeCode,
       t.remittanceCode as treasuryRemittanceCode,
       t.remittanceDescription as treasuryRemittanceDescription,
       t.billAmountCents as treasuryBillAmountCents,
       t.billDate as treasuryBillDate,
       t.receptionDate as treasuryReceptionDate,
       t.documentYear as treasuryDocumentYear,
       t.documentCode as treasuryDocumentCode,
       t.sealCode as treasurySealCode,
       t.pspLastName as treasuryPspLastName,
       t.pspFirstName as treasuryPspFirstName,
       t.pspAddress as treasuryPspAddress,
       t.pspPostalCode as treasuryPspPostalCode,
       t.pspCity as treasuryPspCity,
       t.pspFiscalCode as treasuryPspFiscalCode,
       t.pspVatNumber as treasuryPspVatNumber,
       t.abiCode as treasuryAbiCode,
       t.cabCode as treasuryCabCode,
       t.ibanCode as treasuryIbanCode,
       t.accountRegistryCode as treasuryAccountRegistryCode,
       t.provisionalAe as treasuryProvisionalAe,
       t.provisionalCode as treasuryProvisionalCode,
       t.accountTypeCode as treasuryAccountTypeCode,
       t.processCode as treasuryProcessCode,
       t.executionPgCode as treasuryExecutionPgCode,
       t.transferPgCode as treasuryTransferPgCode,
       t.processPgNumber as treasuryProcessPgNumber,
       t.regionValueDate as treasuryRegionValueDate,
       t.isRegularized as treasuryIsRegularized,
       t.actualSuspensionDate as treasuryActualSuspensionDate,
       t.managementProvisionalCode as treasuryManagementProvisionalCode,
       t.endToEndId as treasuryEndToEndId
    )
    FROM Classification c
    LEFT JOIN Treasury t ON c.treasuryId = t.treasuryId
    WHERE c.organizationId = :organizationId
    AND (:#{#filter.iud} IS NULL OR c.iud = :#{#filter.iud})
    AND (:#{#filter.iuv} IS NULL OR c.iuv = :#{#filter.iuv})
    AND (:#{#filter.iuf} IS NULL OR c.iuf = :#{#filter.iuf})
    AND (:#{#filter.iur} IS NULL OR c.iur = :#{#filter.iur})
    AND (CAST(:#{#filter.lastClassificationDate.from} AS STRING) IS NULL OR c.lastClassificationDate >= :#{#filter.lastClassificationDate.from})
    AND (CAST(:#{#filter.lastClassificationDate.to} AS STRING) IS NULL OR c.lastClassificationDate <= :#{#filter.lastClassificationDate.to})
    AND (CAST(:#{#filter.payDate.from} AS STRING) IS NULL OR c.payDate >= :#{#filter.payDate.from})
    AND (CAST(:#{#filter.payDate.to} AS STRING) IS NULL OR c.payDate <= :#{#filter.payDate.to})
    AND (CAST(:#{#filter.paymentDateTime.from} AS STRING) IS NULL OR c.receiptPaymentDateTime >= :#{#filter.paymentDateTime.from})
    AND (CAST(:#{#filter.paymentDateTime.to} AS STRING) IS NULL OR c.receiptPaymentDateTime <= :#{#filter.paymentDateTime.to})
    AND (CAST(:#{#filter.regulationDate.from} AS STRING) IS NULL OR c.regulationDate >= :#{#filter.regulationDate.from})
    AND (CAST(:#{#filter.regulationDate.to} AS STRING) IS NULL OR c.regulationDate <= :#{#filter.regulationDate.to})
    AND (CAST(:#{#filter.billDate.from} AS STRING) IS NULL OR c.billDate >= :#{#filter.billDate.from})
    AND (CAST(:#{#filter.billDate.to} AS STRING) IS NULL OR c.billDate <= :#{#filter.billDate.to})
    AND (CAST(:#{#filter.regionValueDate.from} AS STRING) IS NULL OR c.regionValueDate >= :#{#filter.regionValueDate.from})
    AND (CAST(:#{#filter.regionValueDate.to} AS STRING) IS NULL OR c.regionValueDate <= :#{#filter.regionValueDate.to})
    AND (:#{#filter.pspCompanyName} IS NULL OR c.receiptPspCompanyName = :#{#filter.pspCompanyName})
    AND (:#{#filter.pspLastName} IS NULL OR c.pspLastName = :#{#filter.pspLastName})
    AND (:#{#filter.regulationUniqueIdentifier} IS NULL OR c.regulationUniqueIdentifier = :#{#filter.regulationUniqueIdentifier})
    AND (:#{#filter.accountRegistryCode} IS NULL OR c.accountRegistryCode = :#{#filter.accountRegistryCode})
    AND (:#{#filter.billAmountCents} IS NULL OR c.billAmountCents = :#{#filter.billAmountCents})
    AND (:#{#filter.remittanceInformation} IS NULL OR c.remittanceInformation = :#{#filter.remittanceInformation})
    """)
  Page<TreasuredClassification> getTreasuredClassifications(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("filter") TreasuredClassificationFilterDTO treasuredClassificationFilterDTO,
    Pageable pageable);
}
