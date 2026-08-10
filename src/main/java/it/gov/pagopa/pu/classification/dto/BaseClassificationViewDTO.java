package it.gov.pagopa.pu.classification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.enums.TreasuryOrigin;
import it.gov.pagopa.pu.debtpositions.dto.generated.PersonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BaseClassificationViewDTO {

  private Long classificationId;

  @JsonProperty("rec_fileName")
  private String receiptFileName;

  @JsonProperty("rec_iud")
  private String receiptIud;

  @JsonProperty("rec_iuv")
  private String receiptIuv;

  @JsonProperty("rec_orgFiscalCode")
  private String receiptOrgFiscalCode;

  @JsonProperty("rec_paymentReceiptId")
  private String receiptPaymentReceiptId;

  @JsonProperty("rec_paymentDateTime")
  private OffsetDateTime receiptPaymentDateTime;

  @JsonProperty("rec_paymentRequestId")
  private String receiptPaymentRequestId;

  @JsonProperty("rec_idPsp")
  private String receiptIdPsp;

  @JsonProperty("rec_pspCompanyName")
  private String receiptPspCompanyName;

  @JsonProperty("rec_orgEntityType")
  private String receiptOrgEntityType;

  @JsonProperty("rec_beneficiaryOrgName")
  private String receiptBeneficiaryOrgName;

  @JsonProperty("rec_payer")
  private PersonDTO receiptPayer;

  @JsonProperty("rec_debtor")
  private PersonDTO receiptDebtor;

  @JsonProperty("rec_paymentOutcomeCode")
  private String receiptPaymentOutcomeCode;

  @JsonProperty("rec_paymentAmount")
  private Long receiptPaymentAmount;

  @JsonProperty("rec_creditorReferenceId")
  private String receiptCreditorReferenceId;

  @JsonProperty("rec_transferAmount")
  private Long receiptTransferAmount;

  @JsonProperty("rec_transferRemittanceInformation")
  private String receiptTransferRemittanceInformation;

  @JsonProperty("rec_transferCategory")
  private String receiptTransferCategory;

  @JsonProperty("rec_creationDate")
  private OffsetDateTime receiptCreationDate;

  @JsonProperty("rec_installmentBalance")
  private String receiptInstallmentBalance;

  @JsonProperty("payRep_iuf")
  private String paymentsReportingIuf;

  @JsonProperty("payRep_flowDateTime")
  private OffsetDateTime paymentsReportingFlowDateTime;

  @JsonProperty("payRep_regulationUniqueIdentifier")
  private String paymentsReportingRegulationUniqueIdentifier;

  @JsonProperty("payRep_regulationDate")
  private LocalDate paymentsReportingRegulationDate;

  @JsonProperty("payRep_senderPspType")
  private String paymentsReportingSenderPspType;

  @JsonProperty("payRep_senderPspCode")
  private String paymentsReportingSenderPspCode;

  @JsonProperty("payRep_senderPspName")
  private String paymentsReportingSenderPspName;

  @JsonProperty("payRep_receiverOrganizationType")
  private String paymentsReportingReceiverOrganizationType;

  @JsonProperty("payRep_receiverOrganizationCode")
  private String paymentsReportingReceiverOrganizationCode;

  @JsonProperty("payRep_receiverOrganizationName")
  private String paymentsReportingReceiverOrganizationName;

  @JsonProperty("payRep_totalPayments")
  private Long paymentsReportingTotalPayments;

  @JsonProperty("payRep_totalAmountCents")
  private Long paymentsReportingTotalAmountCents;

  @JsonProperty("payRep_iuv")
  private String paymentsReportingIuv;

  @JsonProperty("payRep_iur")
  private String paymentsReportingIur;

  @JsonProperty("payRep_amountPaidCents")
  private Long paymentsReportingAmountPaidCents;

  @JsonProperty("payRep_paymentOutcomeCode")
  private String paymentsReportingPaymentOutcomeCode;

  @JsonProperty("payRep_payDate")
  private LocalDate paymentsReportingPayDate;

  @JsonProperty("payRep_creationDate")
  private LocalDate paymentsReportingCreationDate;

  @JsonProperty("tres_abiCode")
  private String treasuryAbiCode;

  @JsonProperty("tres_cabCode")
  private String treasuryCabCode;

  @JsonProperty("tres_accountRegistryCode")
  private String treasuryAccountRegistryCode;

  @JsonProperty("tres_billDate")
  private LocalDate treasuryBillDate;

  @JsonProperty("tres_regionValueDate")
  private LocalDate treasuryRegionValueDate;

  @JsonProperty("tres_billAmountCents")
  private Long treasuryBillAmountCents;

  @JsonProperty("tres_signCode")
  private String treasurySignCode;

  @JsonProperty("tres_remittanceCode")
  private String treasuryRemittanceCode;

  @JsonProperty("tres_lastName")
  private String treasuryLastName;

  @JsonProperty("tres_iuf")
  private String treasuryIuf;

  @JsonProperty("tres_iuv")
  private String treasuryIuv;

  @JsonProperty("tres_creationDate")
  private LocalDateTime treasuryCreationDate;

  @JsonProperty("tres_billYear")
  private String treasuryBillYear;

  @JsonProperty("tres_billCode")
  private String treasuryBillCode;

  @JsonProperty("tres_domainIdCode")
  private String treasuryDomainIdCode;

  @JsonProperty("tres_receptionDate")
  private OffsetDateTime treasuryReceptionDate;

  @JsonProperty("tres_documentYear")
  private String treasuryDocumentYear;

  @JsonProperty("tres_documentCode")
  private String treasuryDocumentCode;

  @JsonProperty("tres_provisionalAe")
  private String treasuryProvisionalAe;

  @JsonProperty("tres_provisionalCode")
  private String treasuryProvisionalCode;

  @JsonProperty("tres_actualSuspensionDate")
  private LocalDate treasuryActualSuspensionDate;

  @JsonProperty("tres_managementProvisionalCode")
  private String treasuryManagementProvisionalCode;

  @JsonProperty("tres_origin")
  private TreasuryOrigin treasuryOrigin;

  private ClassificationsEnum classificationLabel;
  private LocalDate lastClassificationDate;
}
