package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.model.view.ClassificationDetailViewNoPII;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "classifications-detail-view")
public interface ClassificationDetailViewNoPIIRepository extends Repository<ClassificationDetailViewNoPII, Long> {

  @Query(value = """
    SELECT new ClassificationDetailViewNoPII(
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
      t.billYear as billYear,
      t.billCode as billCode,
      t.ingestionFlowFileId as ingestionFlowFileId,
      t.accountCode as accountCode,
      t.domainIdCode as domainIdCode,
      t.transactionTypeCode as transactionTypeCode,
      t.remittanceCode as remittanceCode,
      t.remittanceDescription as remittanceDescription,
      t.receptionDate as receptionDate,
      t.documentYear as documentYear,
      t.documentCode as documentCode,
      t.sealCode as sealCode,
      t.pspFirstName as pspFirstName,
      t.pspAddress as pspAddress,
      t.pspPostalCode as pspPostalCode,
      t.pspCity as pspCity,
      t.pspFiscalCode as pspFiscalCode,
      t.pspVatNumber as pspVatNumber,
      t.abiCode as abiCode,
      t.cabCode as cabCode,
      t.ibanCode as ibanCode,
      t.provisionalAe as provisionalAe,
      t.provisionalCode as provisionalCode,
      t.accountTypeCode as accountTypeCode,
      t.processCode as processCode,
      t.executionPgCode as executionPgCode,
      t.transferPgCode as transferPgCode,
      t.processPgNumber as  processPgNumber,
      t.actualSuspensionDate as actualSuspensionDate,
      t.managementProvisionalCode as managementProvisionalCode,
      t.endToEndId as endToEndId,
      pr.pspIdentifier as pspIdentifier,
      pr.flowDateTime as flowDateTime,
      pr.senderPspType as senderPspType,
      pr.senderPspCode as senderPspCode,
      pr.senderPspName as senderPspName,
      pr.receiverOrganizationType as receiverOrganizationType,
      pr.receiverOrganizationCode as receiverOrganizationCode,
      pr.receiverOrganizationName as receiverOrganizationName,
      pr.totalPayments as totalPayments,
      pr.totalAmountCents as totalAmountCents,
      pr.amountPaidCents as amountPaidCents,
      pr.paymentOutcomeCode as paymentOutcomeCode,
      pr.acquiringDate as acquiringDate,
      pr.bicCodePouringBank as bicCodePouringBank,
      pn.paymentExecutionDate as paymentExecutionDate,
      pn.paymentType as paymentType,
      pn.balance as balance,
      pn.remittanceInformationHash as remittanceInformationHash,
      pn.debtorFiscalCodeHash as debtorFiscalCodeHash
    )
    FROM Classification c
    LEFT JOIN Treasury t ON c.treasuryId = t.treasuryId
    LEFT JOIN PaymentsReporting pr ON c.paymentsReportingId = pr.paymentsReportingId
    LEFT JOIN PaymentNotificationNoPII pn ON c.paymentNotificationId = pn.paymentNotificationId
    WHERE c.organizationId = :organizationId
    AND c.classificationId = :classificationId
    """)
  ClassificationDetailViewNoPII findByOrganizationIdAndClassificationId(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("classificationId") Long classificationId);
}
