package it.gov.pagopa.pu.classification.mapper.pii.view;

import it.gov.pagopa.pu.classification.dto.FullClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.classification.FullClassificationViewNoPII;
import it.gov.pagopa.pu.common.pii.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.common.pii.mapper.Base2PIIMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FullClassificationViewPIIMapper extends Base2PIIMapper<FullClassificationViewDTO, FullClassificationViewNoPII, ReceiptPIIDTO, PaymentNotificationPIIDTO> {

  public FullClassificationViewPIIMapper(PersonalDataService personalDataService) {
    super(ReceiptPIIDTO.class, PaymentNotificationPIIDTO.class, personalDataService);
  }

  @Override
  public FullClassificationViewDTO map(FullClassificationViewNoPII noPii) {
    ReceiptPIIDTO receiptPii = Optional.ofNullable(noPii.getReceiptPersonalDataId())
                                    .map(id -> personalDataService.get(id, ReceiptPIIDTO.class))
                                    .orElse(null);

    PaymentNotificationPIIDTO paymentNotificationPii = Optional.ofNullable(noPii.getPaymentNotificationPersonalDataId())
      .map(id -> personalDataService.get(id, PaymentNotificationPIIDTO.class))
      .orElse(null);

    return map(noPii, receiptPii, paymentNotificationPii);
  }

  @Override
  protected FullClassificationViewDTO map(FullClassificationViewNoPII noPii, ReceiptPIIDTO receiptPii, PaymentNotificationPIIDTO paymentNotificationPii) {
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
      .paymentNotificationDebtor(paymentNotificationPii != null ? paymentNotificationPii.getDebtor() : null)
      .receiptDebtor(receiptPii != null ? receiptPii.getDebtor() : null)
      .receiptPayer(receiptPii != null ? receiptPii.getPayer() : null)
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
