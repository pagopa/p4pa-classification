package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.ClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.ClassificationViewNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.Person;
import org.springframework.stereotype.Service;

@Service
public class ClassificationViewPIIMapper {
  private final PersonalDataService personalDataService;

  public ClassificationViewPIIMapper(PersonalDataService personalDataService) {
    this.personalDataService = personalDataService;
  }

  public ClassificationViewDTO map(ClassificationViewNoPII noPii) {
    ReceiptPIIDTO pii = personalDataService.get(noPii.getReceiptPersonalDataId(), ReceiptPIIDTO.class);
    Person debtor = pii.getDebtor();
    Person payer = pii.getPayer();

    return ClassificationViewDTO.builder()
      .payerEntityType(payer.getEntityType().getValue())
      .payerFiscalCode(payer.getFiscalCode())
      .payerFullName(payer.getFullName())
      .payerAddress(payer.getAddress())
      .payerCivic(payer.getCivic())
      .payerPostalCode(payer.getPostalCode())
      .payerLocation(payer.getLocation())
      .payerProvince(payer.getProvince())
      .payerNation(payer.getNation())
      .payerEmail(payer.getEmail())
      .debtorEntityType(debtor.getEntityType().getValue())
      .debtorFiscalCode(debtor.getFiscalCode())
      .debtorFullName(debtor.getFullName())
      .debtorAddress(debtor.getAddress())
      .debtorCivic(debtor.getCivic())
      .debtorPostalCode(debtor.getPostalCode())
      .debtorLocation(debtor.getLocation())
      .debtorProvince(debtor.getProvince())
      .debtorNation(debtor.getNation())
      .debtorEmail(debtor.getEmail())
      .classificationId(noPii.getClassificationId())
      .receiptFileName(noPii.getReceiptFileName())
      .receiptIud(noPii.getReceiptIud())
      .receiptIuv(noPii.getReceiptIuv())
      .receiptOrgFiscalCode(noPii.getReceiptOrgFiscalCode())
      .receiptPaymentReceiptId(noPii.getReceiptPaymentReceiptId())
      .receiptPaymentDateTime(noPii.getReceiptPaymentDateTime().toLocalDateTime())
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
      .receiptCreationDate(noPii.getReceiptCreationDate().toLocalDateTime())
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
      .treasuryLastName(noPii.getTreasuryLastName())
      .treasuryIuf(noPii.getTreasuryIuf())
      .treasuryIuv(noPii.getTreasuryIuv())
      .treasuryCreationDate(noPii.getTreasuryCreationDate())
      .treasuryBillYear(noPii.getTreasuryBillYear())
      .treasuryBillCode(noPii.getTreasuryBillCode())
      .treasuryDomainIdCode(noPii.getTreasuryDomainIdCode())
      .treasuryReceptionDate(noPii.getTreasuryReceptionDate().toLocalDate())
      .treasuryDocumentYear(noPii.getTreasuryDocumentYear())
      .treasuryDocumentCode(noPii.getTreasuryDocumentCode())
      .treasuryProvisionalAe(noPii.getTreasuryProvisionalAe())
      .treasuryProvisionalCode(noPii.getTreasuryProvisionalCode())
      .treasuryActualSuspensionDate(noPii.getTreasuryActualSuspensionDate())
      .treasuryManagementProvisionalCode(noPii.getTreasuryManagementProvisionalCode())
      .classificationLabel(noPii.getClassificationLabel())
      .classificationDate(noPii.getClassificationDate())
      .build();
  }
}
