package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.FullClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.FullClassificationViewNoPII;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FullClassificationViewPIIMapper {
  private final PersonalDataService personalDataService;

  public FullClassificationViewPIIMapper(PersonalDataService personalDataService) {
    this.personalDataService = personalDataService;
  }

  public FullClassificationViewDTO map(FullClassificationViewNoPII noPii) {
    ReceiptPIIDTO receiptPIIDTO = Optional.ofNullable(noPii.getReceiptPersonalDataId())
                                    .map(id -> personalDataService.get(id, ReceiptPIIDTO.class))
                                    .orElse(null);

    PaymentNotificationPIIDTO paymentNotificationPIIDTO = personalDataService
      .get(noPii.getPaymentNotificationPersonalDataId(), PaymentNotificationPIIDTO.class);

    return FullClassificationViewDTO.builder()
      .paymentNotificationIngestionFlowFileId(noPii.getPaymentNotificationIngestionFlowFileId())
      .paymentNotificationIud(noPii.getPaymentNotificationIud())
      .paymentNotificationIuv(noPii.getPaymentNotificationIuv())
      .paymentNotificationPaymentExecutionDate(noPii.getPaymentNotificationPaymentExecutionDate())
      .paymentNotificationPaymentType(noPii.getPaymentNotificationPaymentType())
      .paymentNotificationAmountPaidCents(noPii.getPaymentNotificationAmountPaidCents())
      .paymentNotificationPaCommissionCents(noPii.getPaymentNotificationPaCommissionCents())
      .paymentNotificationRemittanceInformation(noPii.getPaymentNotificationRemittanceInformation())
      .paymentNotificationTransferCategory(noPii.getPaymentNotificationTransferCategory())
      .paymentNotificationDebtPositionTypeOrgCode(noPii.getPaymentNotificationDebtPositionTypeOrgCode())
      .paymentNotificationBalance(noPii.getPaymentNotificationBalance())
      .paymentNotificationDebtor(paymentNotificationPIIDTO.getDebtor())
      .receiptDebtor(receiptPIIDTO != null ? receiptPIIDTO.getDebtor() : null)
      .receiptPayer(receiptPIIDTO != null ? receiptPIIDTO.getPayer() : null)
      .classificationId(noPii.getClassificationId())
      .receiptFileName(noPii.getReceiptFileName())
      .receiptIud(noPii.getReceiptIud())
      .receiptIuv(noPii.getReceiptIuv())
      .receiptOrgFiscalCode(noPii.getReceiptOrgFiscalCode())
      .receiptPaymentReceiptId(noPii.getReceiptPaymentReceiptId())
      .receiptPaymentDateTime(noPii.getReceiptPaymentDateTime())
      .receiptPaymentRequestId(noPii.getReceiptPaymentRequestId())
      .receiptIdPsp(noPii.getReceiptIdPsp())
      .receiptPspCompanyName(noPii.getReceiptPspCompanyName())
      .receiptOrgEntityType(noPii.getReceiptOrgEntityType())
      .receiptBeneficiaryOrgName(noPii.getReceiptBeneficiaryOrgName())
      .receiptPaymentOutcomeCode(noPii.getReceiptPaymentOutcomeCode())
      .receiptPaymentAmount(noPii.getReceiptPaymentAmount())
      .receiptCreditorReferenceId(noPii.getReceiptCreditorReferenceId())
      .receiptTransferAmount(noPii.getReceiptTransferAmount())
      .receiptTransferRemittanceInformation(noPii.getReceiptTransferRemittanceInformation())
      .receiptTransferCategory(noPii.getReceiptTransferCategory())
      .receiptCreationDate(noPii.getReceiptCreationDate())
      .receiptInstallmentBalance(noPii.getReceiptInstallmentBalance())
      .paymentsReportingIuf(noPii.getPaymentsReportingIuf())
      .paymentsReportingFlowDateTime(noPii.getPaymentsReportingFlowDateTime())
      .paymentsReportingRegulationUniqueIdentifier(noPii.getPaymentsReportingRegulationUniqueIdentifier())
      .paymentsReportingRegulationDate(noPii.getPaymentsReportingRegulationDate())
      .paymentsReportingSenderPspType(noPii.getPaymentsReportingSenderPspType())
      .paymentsReportingSenderPspCode(noPii.getPaymentsReportingSenderPspCode())
      .paymentsReportingSenderPspName(noPii.getPaymentsReportingSenderPspName())
      .paymentsReportingReceiverOrganizationType(noPii.getPaymentsReportingReceiverOrganizationType())
      .paymentsReportingReceiverOrganizationCode(noPii.getPaymentsReportingReceiverOrganizationCode())
      .paymentsReportingReceiverOrganizationName(noPii.getPaymentsReportingReceiverOrganizationName())
      .paymentsReportingTotalPayments(noPii.getPaymentsReportingTotalPayments())
      .paymentsReportingTotalAmountCents(noPii.getPaymentsReportingTotalAmountCents())
      .paymentsReportingIuv(noPii.getPaymentsReportingIuv())
      .paymentsReportingIur(noPii.getPaymentsReportingIur())
      .paymentsReportingAmountPaidCents(noPii.getPaymentsReportingAmountPaidCents())
      .paymentsReportingPaymentOutcomeCode(noPii.getPaymentsReportingPaymentOutcomeCode())
      .paymentsReportingPayDate(noPii.getPaymentsReportingPayDate())
      .paymentsReportingCreationDate(noPii.getPaymentsReportingCreationDate())
      .treasuryAbiCode(noPii.getTreasuryAbiCode())
      .treasuryCabCode(noPii.getTreasuryCabCode())
      .treasuryAccountRegistryCode(noPii.getTreasuryAccountRegistryCode())
      .treasuryBillDate(noPii.getTreasuryBillDate())
      .treasuryRegionValueDate(noPii.getTreasuryRegionValueDate())
      .treasuryBillAmountCents(noPii.getTreasuryBillAmountCents())
      .treasurySignCode(noPii.getTreasurySignCode())
      .treasuryRemittanceCode(noPii.getTreasuryRemittanceCode())
      .treasuryLastName(noPii.getTreasuryPspLastName())
      .treasuryIuf(noPii.getTreasuryIuf())
      .treasuryIuv(noPii.getTreasuryIuv())
      .treasuryCreationDate(noPii.getTreasuryCreationDate())
      .treasuryBillYear(noPii.getTreasuryBillYear())
      .treasuryBillCode(noPii.getTreasuryBillCode())
      .treasuryDomainIdCode(noPii.getTreasuryDomainIdCode())
      .treasuryReceptionDate(noPii.getTreasuryReceptionDate())
      .treasuryDocumentYear(noPii.getTreasuryDocumentYear())
      .treasuryDocumentCode(noPii.getTreasuryDocumentCode())
      .treasuryProvisionalAe(noPii.getTreasuryProvisionalAe())
      .treasuryProvisionalCode(noPii.getTreasuryProvisionalCode())
      .treasuryActualSuspensionDate(noPii.getTreasuryActualSuspensionDate())
      .treasuryManagementProvisionalCode(noPii.getTreasuryManagementProvisionalCode())
      .treasuryOrigin(noPii.getTreasuryOrigin())
      .classificationLabel(noPii.getClassificationLabel())
      .lastClassificationDate(noPii.getLastClassificationDate())
      .build();
  }
}
