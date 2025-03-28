package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.model.view.ClassificationViewNoPII;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.OffsetDateTime;
import java.util.List;

@RepositoryRestResource(path = "classifications-export-view")
public interface ClassificationViewNoPIIDTORepository extends Repository<ClassificationViewNoPII, Long> {
  @SuppressWarnings("squid:S107")
  @RestResource(exported = false)
  @Query("""
    SELECT new ClassificationViewNoPII(
      c.classificationId as classificationId,
      c.receiptFileName as receiptFileName,
      null as receiptNumTotalRows,
      c.iud as receiptIud,
      c.iuv as receiptIuv,
      null as receiptObjectVersion,
      c.receiptOrgFiscalCode as receiptOrgFiscalCode,
      null as receiptBrokerId,
      c.receiptPaymentReceiptId as receiptPaymentReceiptId,
      c.paymentDateTime as receiptPaymentDateTime,
      c.receiptPaymentRequestId as receiptPaymentRequestId,
      null as receiptPaymentRequestDateTime,
      null as receiptPspCodeType,
      c.receiptIdPsp as receiptIdPsp,
      c.receiptPspCompanyName as receiptPspCompanyName,
      null as receiptPspOperatingUnitCode,
      null as receiptPspOperatingUnitName,
      null as receiptPspAddress,
      null as receiptPspCivic,
      null as receiptPspPostalCode,
      null as receiptPspLocation,
      null as receiptPspProvince,
      null as receiptPspNation,
      c.receiptOrgEntityType as receiptOrgEntityType,
      c.receiptBeneficiaryOrgName as receiptBeneficiaryOrgName,
      null as receiptBeneficiaryOrgOperatingUnitCode,
      null as receiptBeneficiaryOrgOperatingUnitName,
      null as receiptBeneficiaryOrgAddress,
      null as receiptBeneficiaryOrgCivic,
      null as receiptBeneficiaryOrgPostalCode,
      null as receiptBeneficiaryOrgLocation,
      null as receiptBeneficiaryOrgProvince,
      null as receiptBeneficiaryOrgNation,
      c.receiptPersonalDataId as receiptPersonalDataId,
      c.receiptPaymentOutcomeCode as receiptPaymentOutcomeCode,
      c.receiptPaymentAmount as receiptPaymentAmount,
      c.receiptCreditorReferenceId as receiptCreditorReferenceId,
      null as receiptPaymentContextId,
      c.receiptTransferAmount as receiptTransferAmount,
      c.receiptPaymentOutcomeCode as receiptTransferPaymentOutcomeCode,
      c.receiptPaymentDateTime as receiptTransferPaymentDateTime,
      null as receiptTransferPaymentReceiptId,
      c.remittanceInformation as receiptTransferRemittanceInformation,
      c.receiptTransferCategory as receiptTransferCategory,
      c.debtPositionTypeOrgCode as receiptDebtPositionTypeOrgCode,
      c.receiptCreationDate as receiptCreationDate,
      c.receiptInstallmentBalance as receiptInstallmentBalance,
      null as paymentsReportingObjectVersion,
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
      null as treasuryCurrencyCode,
      t.billDate as treasuryBillDate,
      t.regionValueDate as treasuryRegionValueDate,
      t.billAmountCents as treasuryBillAmountCents,
      t.sealCode as treasurySignCode,
      t.remittanceCode as treasuryRemittanceCode,
      null as treasuryCheckNumber,
      null as treasuryBankReference,
      null as treasuryCustomerReference,
      null as treasuryOrderDate,
      t.pspLastName as treasuryLastName,
      null as treasuryOrlCode,
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
      c.lastClassificationDate as classificationDate,
      null as typeCodePa1,
      null as typeDescriptionPa1,
      null as taxonomicCodePa1,
      null as fiscalCodePa1,
      null as namePa1
    )
    FROM Classification c
    JOIN PaymentsReporting pr ON c.paymentsReportingId = pr.paymentsReportingId
    JOIN Treasury t ON c.treasuryId = t.treasuryId
    WHERE c.organizationId = :organizationId
    AND (:iud IS NULL OR c.iud = :iud)
    AND (:iuv IS NULL OR c.iuv = :iuv)
    AND (:iuf IS NULL OR pr.iuf = :iuf)
    AND (:iur IS NULL OR pr.iur = :iur)
    AND (:paymentDateTimeFrom IS NULL OR c.paymentDateTime >= :paymentDateTimeFrom)
    AND (:paymentDateTimeTo IS NULL OR c.paymentDateTime <= :paymentDateTimeTo)
    AND (:regulationDateFrom IS NULL OR pr.regulationDate >= :regulationDateFrom)
    AND (:regulationDateTo IS NULL OR pr.regulationDate <= :regulationDateTo)
    AND (:billDateFrom IS NULL OR t.billDate >= :billDateFrom)
    AND (:billDateTo IS NULL OR t.billDate <= :billDateTo)
    AND (:regionValueDateFrom IS NULL OR t.regionValueDate >= :regionValueDateFrom)
    AND (:regionValueDateTo IS NULL OR t.regionValueDate <= :regionValueDateTo)
    AND (:payDateFrom IS NULL OR pr.payDate >= :payDateFrom)
    AND (:payDateTo IS NULL OR pr.payDate <= :payDateTo)
    AND (:lastClassificationDateFrom IS NULL OR c.lastClassificationDate >= :lastClassificationDateFrom)
    AND (:lastClassificationDateTo IS NULL OR c.lastClassificationDate <= :lastClassificationDateTo)
    AND (:regulationUniqueIdentifier IS NULL OR pr.regulationUniqueIdentifier = :regulationUniqueIdentifier)
    AND (:accountRegistryCode IS NULL OR t.accountRegistryCode = :accountRegistryCode)
    AND (:billAmountCents IS NULL OR t.billAmountCents = :billAmountCents)
    AND (:remittanceInformation IS NULL OR c.remittanceInformation = :remittanceInformation)
    AND (:pspCompanyName IS NULL OR c.receiptPspCompanyName = :pspCompanyName)
    AND (:pspLastName IS NULL OR t.pspLastName = :pspLastName)
    AND c.debtPositionTypeOrgCode IN :debtPositionTypeOrgCodes
    """)
  Page<ClassificationViewNoPII> findClassificationViewNoPIIDTO(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @Param("iud") String iud,
    @Param("iuv") String iuv,
    @Param("iuf") String iuf,
    @Param("iur") String iur,
    @Param("paymentDateTimeFrom") OffsetDateTime paymentDateTimeFrom,
    @Param("paymentDateTimeTo") OffsetDateTime paymentDateTimeTo,
    @Param("regulationDateFrom") OffsetDateTime regulationDateFrom,
    @Param("regulationDateTo") OffsetDateTime regulationDateTo,
    @Param("billDateFrom") OffsetDateTime billDateFrom,
    @Param("billDateTo") OffsetDateTime billDateTo,
    @Param("regionValueDateFrom") OffsetDateTime regionValueDateFrom,
    @Param("regionValueDateTo") OffsetDateTime regionValueDateTo,
    @Param("payDateFrom") OffsetDateTime payDateFrom,
    @Param("payDateTo") OffsetDateTime payDateTo,
    @Param("lastClassificationDateFrom") OffsetDateTime lastClassificationDateFrom,
    @Param("lastClassificationDateTo") OffsetDateTime lastClassificationDateTo,
    @Param("regulationUniqueIdentifier") String regulationUniqueIdentifier,
    @Param("accountRegistryCode") String accountRegistryCode,
    @Param("billAmountCents") Long billAmountCents,
    @Param("remittanceInformation") String remittanceInformation,
    @Param("pspCompanyName") String pspCompanyName,
    @Param("pspLastName") String pspLastName,
    @Parameter(required = true) @Param("debtPositionTypeOrgCodes") List<String> debtPositionTypeOrgCodes,
    Pageable pageable);
}
