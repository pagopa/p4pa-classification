package it.gov.pagopa.pu.classification.mapper.pii.view;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationDetailViewNoPII;
import it.gov.pagopa.pu.common.pii.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.common.pii.mapper.Base2PIIMapper;
import org.springframework.stereotype.Component;

@Component
public class ClassificationDetailViewPIIMapper extends Base2PIIMapper<ClassificationDetailViewDTO, ClassificationDetailViewNoPII, ReceiptPIIDTO, PaymentNotificationPIIDTO> {

  public ClassificationDetailViewPIIMapper(PersonalDataService personalDataService) {
    super(ReceiptPIIDTO.class, PaymentNotificationPIIDTO.class, personalDataService);
  }

  @Override
  public ClassificationDetailViewDTO map(ClassificationDetailViewNoPII noPii) {
    ReceiptPIIDTO receiptPii = null;
    if (noPii.getReceiptPersonalDataId() != null) {
      receiptPii = personalDataService.get(noPii.getReceiptPersonalDataId(), ReceiptPIIDTO.class);
    }

    PaymentNotificationPIIDTO paymentNotificationPii = null;
    if (noPii.getPaymentNotificationPersonalDataId()!=null) {
      paymentNotificationPii = personalDataService.get(noPii.getPaymentNotificationPersonalDataId(), PaymentNotificationPIIDTO.class);
    }

    return map(noPii, receiptPii, paymentNotificationPii);
  }

  @Override
  protected ClassificationDetailViewDTO map(ClassificationDetailViewNoPII noPii, ReceiptPIIDTO receiptPii, PaymentNotificationPIIDTO paymentNotificationPii) {
    return ClassificationDetailViewDTO.builder()
      .receiptDebtor(receiptPii != null ? receiptPii.getDebtor() : null)
      .receiptPayer(receiptPii != null ? receiptPii.getPayer() : null)
      .classificationId(noPii.getClassificationId())
      .organizationId(noPii.getOrganizationId())
      .transferId(noPii.getTransferId())
      .paymentNotificationId(noPii.getPaymentNotificationId())
      .paymentsReportingId(noPii.getPaymentsReportingId())
      .treasuryId(noPii.getTreasuryId())
      .iuf(noPii.getIuf())
      .iud(noPii.getIud())
      .iuv(noPii.getIuv())
      .iur(noPii.getIur())
      .transferIndex(noPii.getTransferIndex())
      .label(noPii.getLabel())
      .lastClassificationDate(noPii.getLastClassificationDate())
      .payDate(noPii.getPayDate())
      .paymentDateTime(noPii.getPaymentDateTime())
      .regulationDate(noPii.getRegulationDate())
      .billDate(noPii.getBillDate())
      .regionValueDate(noPii.getRegionValueDate())
      .regulationUniqueIdentifier(noPii.getRegulationUniqueIdentifier())
      .accountRegistryCode(noPii.getAccountRegistryCode())
      .billAmountCents(noPii.getBillAmountCents())
      .remittanceInformation(noPii.getRemittanceInformation())
      .pspCompanyName(noPii.getPspCompanyName())
      .pspLastName(noPii.getPspLastName())
      .debtPositionTypeOrgCode(noPii.getDebtPositionTypeOrgCode())
      .debtPositionTypeOrgDescription(noPii.getDebtPositionTypeOrgDescription())
      .installmentIngestionFlowFileName(noPii.getInstallmentIngestionFlowFileName())
      .receiptOrgFiscalCode(noPii.getReceiptOrgFiscalCode())
      .receiptPaymentReceiptId(noPii.getReceiptPaymentReceiptId())
      .receiptPaymentDateTime(noPii.getReceiptPaymentDateTime())
      .receiptPaymentRequestId(noPii.getReceiptPaymentRequestId())
      .receiptIdPsp(noPii.getReceiptIdPsp())
      .receiptPspCompanyName(noPii.getReceiptPspCompanyName())
      .organizationEntityType(noPii.getOrganizationEntityType())
      .organizationName(noPii.getOrganizationName())
      .receiptPaymentOutcomeCode(noPii.getReceiptPaymentOutcomeCode())
      .receiptPaymentAmount(noPii.getReceiptPaymentAmount())
      .receiptCreditorReferenceId(noPii.getReceiptCreditorReferenceId())
      .transferAmount(noPii.getTransferAmount())
      .transferCategory(noPii.getTransferCategory())
      .receiptCreationDate(noPii.getReceiptCreationDate())
      .installmentBalance(noPii.getInstallmentBalance())
      .billYear(noPii.getBillYear())
      .billCode(noPii.getBillCode())
      .ingestionFlowFileId(noPii.getIngestionFlowFileId())
      .accountCode(noPii.getAccountCode())
      .domainIdCode(noPii.getDomainIdCode())
      .transactionTypeCode(noPii.getTransactionTypeCode())
      .remittanceCode(noPii.getRemittanceCode())
      .remittanceDescription(noPii.getRemittanceDescription())
      .receptionDate(noPii.getReceptionDate())
      .documentYear(noPii.getDocumentYear())
      .documentCode(noPii.getDocumentCode())
      .sealCode(noPii.getSealCode())
      .pspFirstName(noPii.getPspFirstName())
      .pspAddress(noPii.getPspAddress())
      .pspPostalCode(noPii.getPspPostalCode())
      .pspCity(noPii.getPspCity())
      .pspFiscalCode(noPii.getPspFiscalCode())
      .pspVatNumber(noPii.getPspVatNumber())
      .abiCode(noPii.getAbiCode())
      .cabCode(noPii.getCabCode())
      .ibanCode(noPii.getIbanCode())
      .provisionalAe(noPii.getProvisionalAe())
      .provisionalCode(noPii.getProvisionalCode())
      .accountTypeCode(noPii.getAccountTypeCode())
      .processCode(noPii.getProcessCode())
      .executionPgCode(noPii.getExecutionPgCode())
      .transferPgCode(noPii.getTransferPgCode())
      .processPgNumber(noPii.getProcessPgNumber())
      .actualSuspensionDate(noPii.getActualSuspensionDate())
      .managementProvisionalCode(noPii.getManagementProvisionalCode())
      .endToEndId(noPii.getEndToEndId())
      .pspIdentifier(noPii.getPspIdentifier() != null ? noPii.getPspIdentifier() : null)
      .flowDateTime(noPii.getFlowDateTime() != null ? noPii.getFlowDateTime() : null )
      .senderPspType(noPii.getSenderPspType() != null ? noPii.getSenderPspType() : null)
      .senderPspCode(noPii.getSenderPspCode() != null ? noPii.getSenderPspCode() : null)
      .senderPspName(noPii.getSenderPspName() != null ? noPii.getSenderPspName() : null)
      .receiverOrganizationType(noPii.getReceiverOrganizationType() != null ? noPii.getReceiverOrganizationType() : null)
      .receiverOrganizationCode(noPii.getReceiverOrganizationCode() != null ? noPii.getReceiverOrganizationCode() : null)
      .receiverOrganizationName(noPii.getReceiverOrganizationName() != null ? noPii.getReceiverOrganizationName() : null)
      .totalPayments(noPii.getTotalPayments() != null ? noPii.getTotalPayments() : null)
      .totalAmountCents(noPii.getTotalAmountCents() != null ? noPii.getTotalAmountCents() : null)
      .amountPaidCents(noPii.getAmountPaidCents() != null ? noPii.getAmountPaidCents(): null)
      .paymentOutcomeCode(noPii.getPaymentOutcomeCode() != null ? noPii.getPaymentOutcomeCode() : null)
      .acquiringDate(noPii.getAcquiringDate() != null ? noPii.getAcquiringDate() : null)
      .bicCodePouringBank(noPii.getBicCodePouringBank() != null ? noPii.getBicCodePouringBank() : null)
      .paymentExecutionDate(noPii.getPaymentExecutionDate())
      .paymentType(noPii.getPaymentType())
      .balance(noPii.getBalance())
      .remittanceInformationHash(noPii.getRemittanceInformationHash())
      .debtorFiscalCodeHash(noPii.getDebtorFiscalCodeHash())
      .paymentNotificationDebtor(paymentNotificationPii!=null?paymentNotificationPii.getDebtor():null)
      .paymentNotificationRemittanceInformation(noPii.getPaymentNotificationRemittanceInformation())
      .paymentNotificationIud(noPii.getPaymentNotificationIud())
      .paymentNotificationAmountPaidCents(noPii.getPaymentNotificationAmountPaidCents())
      .paymentNotificationDebtPositionTypeOrgCode(noPii.getPaymentNotificationDebtPositionTypeOrgCode())
      .debtPositionOrigin(noPii.getDebtPositionOrigin())
      .receiptOrigin(noPii.getReceiptOrigin())
      .build();
  }
}
