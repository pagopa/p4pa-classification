package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.dto.generated.TreasuredClassificationDTO;
import it.gov.pagopa.pu.classification.model.view.TreasuredClassification;
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class TreasuredClassificationMapper {

  public TreasuredClassificationMapper() {
  }

  public TreasuredClassificationDTO map2DTO(TreasuredClassification entity) {
    return TreasuredClassificationDTO.builder()
      .classificationId(entity.getClassificationId())
      .organizationId(entity.getOrganizationId())
      .transferId(entity.getTransferId())
      .paymentNotificationId(entity.getPaymentNotificationId())
      .paymentsReportingId(entity.getPaymentsReportingId())
      .treasuryId(entity.getTreasuryId())
      .iuf(entity.getIuf())
      .iud(entity.getIud())
      .iuv(entity.getIuv())
      .iur(entity.getIur())
      .transferIndex(entity.getTransferIndex())
      .label(entity.getLabel())
      .lastClassificationDate(entity.getLastClassificationDate())
      .payDate(entity.getPayDate())
      .paymentDateTime(entity.getPaymentDateTime())
      .regulationDate(entity.getRegulationDate())
      .billDate(entity.getBillDate())
      .regionValueDate(entity.getRegionValueDate())
      .regulationUniqueIdentifier(entity.getRegulationUniqueIdentifier())
      .accountRegistryCode(entity.getAccountRegistryCode())
      .billAmountCents(entity.getBillAmountCents())
      .remittanceInformation(entity.getRemittanceInformation())
      .pspCompanyName(entity.getPspCompanyName())
      .pspLastName(entity.getPspLastName())
      .debtPositionTypeOrgCode(entity.getDebtPositionTypeOrgCode())
      .debtPositionTypeOrgDescription(entity.getDebtPositionTypeOrgDescription())
      .installmentIngestionFlowFileName(entity.getInstallmentIngestionFlowFileName())
      .receiptOrgFiscalCode(entity.getReceiptOrgFiscalCode())
      .receiptPaymentReceiptId(entity.getReceiptPaymentReceiptId())
      .receiptPaymentDateTime(entity.getReceiptPaymentDateTime())
      .receiptPaymentRequestId(entity.getReceiptPaymentRequestId())
      .receiptIdPsp(entity.getReceiptIdPsp())
      .receiptPspCompanyName(entity.getReceiptPspCompanyName())
      .organizationEntityType(entity.getOrganizationEntityType())
      .organizationName(entity.getOrganizationName())
      .receiptPersonalDataId(entity.getReceiptPersonalDataId())
      .receiptPaymentOutcomeCode(entity.getReceiptPaymentOutcomeCode())
      .receiptPaymentAmount(entity.getReceiptPaymentAmount())
      .receiptCreditorReferenceId(entity.getReceiptCreditorReferenceId())
      .transferAmount(entity.getTransferAmount())
      .transferCategory(entity.getTransferCategory())
      .receiptCreationDate(entity.getReceiptCreationDate())
      .installmentBalance(entity.getInstallmentBalance())
      .treasuryBillYear(entity.getTreasuryBillYear())
      .treasuryBillCode(entity.getTreasuryBillCode())
      .treasuryIngestionFlowFileId(entity.getTreasuryIngestionFlowFileId())
      .treasuryIuf(entity.getTreasuryIuf())
      .treasuryIuv(entity.getTreasuryIuv())
      .treasuryAccountCode(entity.getTreasuryAccountCode())
      .treasuryDomainIdCode(entity.getTreasuryDomainIdCode())
      .treasuryTransactionTypeCode(entity.getTreasuryTransactionTypeCode())
      .treasuryRemittanceCode(entity.getTreasuryRemittanceCode())
      .treasuryRemittanceDescription(entity.getTreasuryRemittanceDescription())
      .treasuryBillAmountCents(entity.getTreasuryBillAmountCents())
      .treasuryBillDate(entity.getTreasuryBillDate())
      .treasuryReceptionDate(entity.getTreasuryReceptionDate())
      .treasuryDocumentYear(entity.getTreasuryDocumentYear())
      .treasuryDocumentCode(entity.getTreasuryDocumentCode())
      .treasurySealCode(entity.getTreasurySealCode())
      .treasuryPspLastName(entity.getTreasuryPspLastName())
      .treasuryPspFirstName(entity.getTreasuryPspFirstName())
      .treasuryPspAddress(entity.getTreasuryPspAddress())
      .treasuryPspPostalCode(entity.getTreasuryPspPostalCode())
      .treasuryPspCity(entity.getTreasuryPspCity())
      .treasuryPspFiscalCode(entity.getTreasuryPspFiscalCode())
      .treasuryPspVatNumber(entity.getTreasuryPspVatNumber())
      .treasuryAbiCode(entity.getTreasuryAbiCode())
      .treasuryCabCode(entity.getTreasuryCabCode())
      .treasuryIbanCode(entity.getTreasuryIbanCode())
      .treasuryAccountRegistryCode(entity.getTreasuryAccountRegistryCode())
      .treasuryProvisionalAe(entity.getTreasuryProvisionalAe())
      .treasuryProvisionalCode(entity.getTreasuryProvisionalCode())
      .treasuryAccountTypeCode(String.valueOf(entity.getTreasuryAccountTypeCode()))
      .treasuryProcessCode(entity.getTreasuryProcessCode())
      .treasuryExecutionPgCode(entity.getTreasuryExecutionPgCode())
      .treasuryTransferPgCode(entity.getTreasuryTransferPgCode())
      .treasuryProcessPgNumber(entity.getTreasuryProcessPgNumber())
      .treasuryRegionValueDate(entity.getTreasuryRegionValueDate())
      .treasuryIsRegularized(entity.isTreasuryIsRegularized())
      .treasuryActualSuspensionDate(entity.getTreasuryActualSuspensionDate())
      .treasuryManagementProvisionalCode(entity.getTreasuryManagementProvisionalCode())
      .treasuryEndToEndId(entity.getTreasuryEndToEndId())
      .build();
  }

  public PagedTreasuredClassification map2PagedTreasuredClassification(
    Page<TreasuredClassification> pagedClassificationListDTO) {
    PagedTreasuredClassification mappedPagedClassificationList = new PagedTreasuredClassification();

    if (pagedClassificationListDTO != null) {
      if (!pagedClassificationListDTO.getContent().isEmpty()) {
        mappedPagedClassificationList.setContent(
          pagedClassificationListDTO.stream()
            .map(this::map2DTO)
            .toList());
      } else {
        mappedPagedClassificationList.setContent(Collections.emptyList());
      }

      if (pagedClassificationListDTO.getPageable().isPaged()) {
        mappedPagedClassificationList.setTotalPages(
          (long) pagedClassificationListDTO.getTotalPages());
        mappedPagedClassificationList.setSize(
          (long) pagedClassificationListDTO.getSize());
        mappedPagedClassificationList.setNumber(
          (long) pagedClassificationListDTO.getNumber());
        mappedPagedClassificationList.setTotalElements(
          pagedClassificationListDTO.getTotalElements());
      }
    }

    return mappedPagedClassificationList;
  }
}
