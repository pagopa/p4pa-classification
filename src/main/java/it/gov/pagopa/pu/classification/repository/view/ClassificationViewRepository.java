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

@RepositoryRestResource(path = "classification-export-view")
public interface ClassificationViewRepository extends Repository<ClassificationViewNoPII, Long> {

  @RestResource(exported = false)
  @Query("""
    SELECT new it.gov.pagopa.pu.classification.model.view.ClassificationViewNoPII(
      c.classificationId as classificationId,
      c.receiptFileName as receiptFileName,
      c.receiptNumTotalRows as receiptNumTotalRows,
      c.receiptIud as receiptIud,
      c.receiptIuv as receiptIuv,
      c.receiptObjectVersion as receiptObjectVersion,
      c.receiptOrgFiscalCode as receiptOrgFiscalCode,
      c.receiptBrokerId as receiptBrokerId,
      c.receiptPaymentReceiptId as receiptPaymentReceiptId,
      c.receiptPaymentDateTime as receiptPaymentDateTime,
      c.receiptPaymentRequestId as receiptPaymentRequestId,
      c.receiptPaymentRequestDateTime as receiptPaymentRequestDateTime,
      c.receiptPspCodeType as receiptPspCodeType,
      c.receiptIdPsp as receiptIdPsp,
      c.receiptPspCompanyName as receiptPspCompanyName,
      c.receiptPspOperatingUnitCode as receiptPspOperatingUnitCode,
      c.receiptPspOperatingUnitName as receiptPspOperatingUnitName,
      c.receiptPspAddress as receiptPspAddress,
      c.receiptPspCivic as receiptPspCivic,
      c.receiptPspPostalCode as receiptPspPostalCode,
      c.receiptPspLocation as receiptPspLocation,
      c.receiptPspProvince as receiptPspProvince,
      c.receiptPspNation as receiptPspNation,
      c.receiptOrgEntityType as receiptOrgEntityType,
      c.receiptBeneficiaryOrgName as receiptBeneficiaryOrgName,
      c.receiptBeneficiaryOrgOperatingUnitCode as receiptBeneficiaryOrgOperatingUnitCode,
      c.receiptBeneficiaryOrgOperatingUnitName as receiptBeneficiaryOrgOperatingUnitName,
      c.receiptBeneficiaryOrgAddress as receiptBeneficiaryOrgAddress,
      c.receiptBeneficiaryOrgCivic as receiptBeneficiaryOrgCivic,
      c.receiptBeneficiaryOrgPostalCode as receiptBeneficiaryOrgPostalCode,
      c.receiptBeneficiaryOrgLocation as receiptBeneficiaryOrgLocation,
      c.receiptBeneficiaryOrgProvince as receiptBeneficiaryOrgProvince,
      c.receiptBeneficiaryOrgNation as receiptBeneficiaryOrgNation,
      c.receiptPersonalDataId as receiptPersonalDataId,
      c.receiptPaymentOutcomeCode as receiptPaymentOutcomeCode,
      c.receiptPaymentAmount as receiptPaymentAmount,
      c.receiptCreditorReferenceId as receiptCreditorReferenceId,
      c.receiptPaymentContextId as receiptPaymentContextId,
      c.receiptTransferAmount as receiptTransferAmount,
      c.receiptTransferPaymentOutcomeCode as receiptTransferPaymentOutcomeCode,
      c.receiptTransferPaymentDateTime as receiptTransferPaymentDateTime,
      c.receiptTransferPaymentReceiptId as receiptTransferPaymentReceiptId,
      c.receiptTransferRemittanceInformation as receiptTransferRemittanceInformation,
      c.receiptTransferCategory as receiptTransferCategory,
      c.receiptDebtPositionTypeOrgId as receiptDebtPositionTypeOrgId,
      c.receiptCreationDate as receiptCreationDate,
      c.receiptInstallmentBalance as receiptInstallmentBalance,
      pr.paymentsReportingObjectVersion as paymentsReportingObjectVersion,
      pr.paymentsReportingIuf as paymentsReportingIuf,
      pr.paymentsReportingFlowDateTime as paymentsReportingFlowDateTime,
      pr.paymentsReportingRegulationUniqueIdentifier as paymentsReportingRegulationUniqueIdentifier,
      pr.paymentsReportingRegulationDate as paymentsReportingRegulationDate,
      pr.paymentsReportingSenderPspType as paymentsReportingSenderPspType,
      pr.paymentsReportingSenderPspCode as paymentsReportingSenderPspCode,
      pr.paymentsReportingSenderPspName as paymentsReportingSenderPspName,
      pr.paymentsReportingReceiverOrganizationType as paymentsReportingReceiverOrganizationType,
      pr.paymentsReportingReceiverOrganizationCode as paymentsReportingReceiverOrganizationCode,
      pr.paymentsReportingReceiverOrganizationName as paymentsReportingReceiverOrganizationName,
      pr.paymentsReportingTotalPayments as paymentsReportingTotalPayments,
      pr.paymentsReportingTotalAmountCents as paymentsReportingTotalAmountCents,
      pr.paymentsReportingIuv as paymentsReportingIuv,
      pr.paymentsReportingIur as paymentsReportingIur,
      pr.paymentsReportingAmountPaidCents as paymentsReportingAmountPaidCents,
      pr.paymentsReportingPaymentOutcomeCode as paymentsReportingPaymentOutcomeCode,
      pr.paymentsReportingPayDate as paymentsReportingPayDate,
      pr.paymentsReportingCreationDate as paymentsReportingCreationDate,
      t.treasuryAbiCode as treasuryAbiCode,
      t.treasuryCabCode as treasuryCabCode,
      t.treasuryAccountRegistryCode as treasuryAccountRegistryCode,
      t.treasuryCurrencyCode as treasuryCurrencyCode,
      t.treasuryBillDate as treasuryBillDate,
      t.treasuryRegionValueDate as treasuryRegionValueDate,
      t.treasuryBillAmountCents as treasuryBillAmountCents,
      t.treasurySignCode as treasurySignCode,
      t.treasuryRemittanceCode as treasuryRemittanceCode,
      t.treasuryCheckNumber as treasuryCheckNumber,
      t.treasuryBankReference as treasuryBankReference,
      t.treasuryCustomerReference as treasuryCustomerReference,
      t.treasuryOrderDate as treasuryOrderDate,
      t.treasuryLastName as treasuryLastName,
      t.treasuryOrlCode as treasuryOrlCode,
      t.treasuryIuf as treasuryIuf,
      t.treasuryIuv as treasuryIuv,
      t.treasuryCreationDate as treasuryCreationDate,
      t.treasuryBillYear as treasuryBillYear,
      t.treasuryBillCode as treasuryBillCode,
      t.treasuryDomainIdCode as treasuryDomainIdCode,
      t.treasuryReceptionDate as treasuryReceptionDate,
      t.treasuryDocumentYear as treasuryDocumentYear,
      t.treasuryDocumentCode as treasuryDocumentCode,
      t.treasuryProvisionalAe as treasuryProvisionalAe,
      t.treasuryProvisionalCode as treasuryProvisionalCode,
      t.treasuryActualSuspensionDate as treasuryActualSuspensionDate,
      t.treasuryManagementProvisionalCode as treasuryManagementProvisionalCode,
      c.classificationLabel as classificationLabel,
      c.classificationDate as classificationDate,
      c.typeCodePal as typeCodePal,
      c.typeDescriptionPal as typeDescriptionPal,
      c.taxonomicCodePal as taxonomicCodePal,
      c.fiscalCodePal as fiscalCodePal,
      c.namePal as namePal
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
    AND c.receiptDebtPositionTypeOrgId IN :receiptDebtPositionTypeOrgIds
    """)
  Page<ClassificationViewNoPII> findClassificationViewNoPIIDTO(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("filter") ExportClassificationsFilterDTO exportClassificationsFilterDTO,
    @Parameter(required = true) @Param("debtPositionTypeOrgCodes") List<String> receiptDebtPositionTypeOrgIds,
    Pageable pageable);
}

