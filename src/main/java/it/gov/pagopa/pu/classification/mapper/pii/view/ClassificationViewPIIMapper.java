package it.gov.pagopa.pu.classification.mapper.pii.view;

import it.gov.pagopa.pu.classification.dto.ClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationViewNoPII;
import it.gov.pagopa.pu.common.pii.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.common.pii.mapper.BasePIIMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClassificationViewPIIMapper extends BasePIIMapper<ClassificationViewDTO, ClassificationViewNoPII, ReceiptPIIDTO> {

  public ClassificationViewPIIMapper(PersonalDataService personalDataService) {
    super(ReceiptPIIDTO.class, personalDataService);
  }

  @Override
  public ClassificationViewDTO map(ClassificationViewNoPII noPii) {
    ReceiptPIIDTO pii = Optional.ofNullable(noPii.getReceiptPersonalDataId())
                          .map(id -> personalDataService.get(id, ReceiptPIIDTO.class))
                          .orElse(null);

    return map(noPii, pii);
  }

  @Override
  protected ClassificationViewDTO map(ClassificationViewNoPII noPii, ReceiptPIIDTO pii) {
    return ClassificationViewDTO.builder()
      .receiptDebtor(pii != null ? pii.getDebtor() : null)
      .receiptPayer(pii != null ? pii.getPayer() : null)
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
