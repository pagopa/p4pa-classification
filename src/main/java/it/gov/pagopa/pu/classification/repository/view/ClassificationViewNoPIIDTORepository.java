package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.model.view.ClassificationViewNoPII;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(path = "classifications-export-view")
public interface ClassificationViewNoPIIDTORepository extends Repository<ClassificationViewNoPII, Long> {
  @SuppressWarnings("squid:S107")
  @RestResource(exported = false)
  @Query(value = """
    SELECT new ClassificationViewNoPII(
      c.classificationId as classificationId,
      c.receiptFileName as receiptFileName,
      c.iud as receiptIud,
      c.iuv as receiptIuv,
      c.receiptOrgFiscalCode as receiptOrgFiscalCode,
      c.receiptPaymentReceiptId as receiptPaymentReceiptId,
      c.paymentDateTime as receiptPaymentDateTime,
      c.receiptPaymentRequestId as receiptPaymentRequestId,
      c.receiptIdPsp as receiptIdPsp,
      c.receiptPspCompanyName as receiptPspCompanyName,
      c.receiptOrgEntityType as receiptOrgEntityType,
      c.receiptBeneficiaryOrgName as receiptBeneficiaryOrgName,
      c.receiptPersonalDataId as receiptPersonalDataId,
      c.receiptPaymentOutcomeCode as receiptPaymentOutcomeCode,
      c.receiptPaymentAmount as receiptPaymentAmount,
      c.receiptCreditorReferenceId as receiptCreditorReferenceId,
      c.receiptTransferAmount as receiptTransferAmount,
      c.remittanceInformation as receiptTransferRemittanceInformation,
      c.receiptTransferCategory as receiptTransferCategory,
      c.debtPositionTypeOrgCode as receiptDebtPositionTypeOrgCode,
      c.receiptCreationDate as receiptCreationDate,
      c.receiptInstallmentBalance as receiptInstallmentBalance,
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
      t.pspLastName as treasuryLastName,
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
      c.lastClassificationDate as classificationDate
    )
    FROM Classification c
    JOIN PaymentReporting pr ON c.paymentsReportingId = pr.paymentsReportingId
    JOIN Treasury t ON c.treasuryId = t.treasuryId
    WHERE c.organizationId = :organizationId
    AND (:#{#filters.iud} IS NULL OR c.iud = :#{#filters.iud})
    AND (:#{#filters.iuv} IS NULL OR c.iuv = :#{#filters.iuv})
    AND (:#{#filters.iuf} IS NULL OR c.iuf = :#{#filters.iuf})
    AND (:#{#filters.iur} IS NULL OR c.iur = :#{#filters.iur})
    AND (:#{#filters.paymentDateTime.from} IS NULL OR c.receiptPaymentDateTime >= :#{#filters.paymentDateTime.from})
    AND (:#{#filters.paymentDateTime.to} IS NULL OR c.receiptPaymentDateTime <= :#{#filters.paymentDateTime.to})
    AND (:#{#filters.regulationDate.from} IS NULL OR pr.paymentsReportingRegulationDate >= :#{#filters.regulationDate.from})
    AND (:#{#filters.regulationDate.to} IS NULL OR pr.paymentsReportingRegulationDate <= :#{#filters.regulationDate.to})
    AND (:#{#filters.billDate.from} IS NULL OR t.treasuryBillDate >= :#{#filters.billDate.from})
    AND (:#{#filters.billDate.to} IS NULL OR t.treasuryBillDate <= :#{#filters.billDate.to})
    AND (:#{#filters.regionValueDate.from} IS NULL OR t.treasuryRegionValueDate >= :#{#filters.regionValueDate.from})
    AND (:#{#filters.regionValueDate.to} IS NULL OR t.treasuryRegionValueDate <= :#{#filters.regionValueDate.to})
    AND (:#{#filters.payDate.from} IS NULL OR pr.paymentsReportingPayDate >= :#{#filters.payDate.from})
    AND (:#{#filters.payDate.to} IS NULL OR pr.paymentsReportingPayDate <= :#{#filters.payDate.to})
    AND (:#{#filters.lastClassificationDate.from} IS NULL OR c.classificationDate >= :#{#filters.lastClassificationDate.from})
    AND (:#{#filters.lastClassificationDate.to} IS NULL OR c.classificationDate <= :#{#filters.lastClassificationDate.to})
    AND (:#{#filters.regulationUniqueIdentifier} IS NULL OR pr.paymentsReportingRegulationUniqueIdentifier = :#{#filters.regulationUniqueIdentifier})
    AND (:#{#filters.accountRegistryCode} IS NULL OR t.treasuryAccountRegistryCode = :#{#filters.accountRegistryCode})
    AND (:#{#filters.billAmountCents} IS NULL OR t.treasuryBillAmountCents = :#{#filters.billAmountCents})
    AND (:#{#filters.remittanceInformation} IS NULL OR c.receiptTransferRemittanceInformation = :#{#filters.remittanceInformation})
    AND (:#{#filters.pspCompanyName} IS NULL OR c.receiptPspCompanyName = :#{#filters.pspCompanyName})
    AND (:#{#filters.pspLastName} IS NULL OR c.treasuryLastName = :#{#filters.pspLastName})
    AND c.debtPositionTypeOrgCode IN :receiptDebtPositionTypeOrgCodes
    """, nativeQuery = true)
  Page<ClassificationViewNoPII> findClassificationViewNoPIIDTO(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("filter") ExportClassificationsFilterDTO exportClassificationsFilterDTO,
    @Parameter(required = true) @Param("debtPositionTypeOrgCodes") List<String> receiptDebtPositionTypeOrgCodes,
    Pageable pageable);
}
