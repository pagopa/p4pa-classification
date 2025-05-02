package it.gov.pagopa.pu.classification.model.view;

import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

  @Entity
  @Table(name = "classification")
  @Data
  @SuperBuilder(toBuilder = true)
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  public class ClassificationViewNoPII implements Serializable {
    @Id
    private Long classificationId;
    private String receiptFileName;
    private String receiptIud;
    private String receiptIuv;
    private String receiptOrgFiscalCode;
    private String receiptPaymentReceiptId;
    private OffsetDateTime receiptPaymentDateTime;
    private String receiptPaymentRequestId;
    private String receiptIdPsp;
    private String receiptPspCompanyName;
    private String receiptOrgEntityType;
    private String receiptBeneficiaryOrgName;
    private Long receiptPersonalDataId;
    private String receiptPaymentOutcomeCode;
    private Long receiptPaymentAmount;
    private String receiptCreditorReferenceId;
    private Long receiptTransferAmount;
    private String receiptTransferRemittanceInformation;
    private String receiptTransferCategory;
    private String receiptDebtPositionTypeOrgCode;
    private OffsetDateTime receiptCreationDate;
    private String receiptInstallmentBalance;
    private String paymentsReportingIuf;
    private LocalDateTime paymentsReportingFlowDateTime;
    private String paymentsReportingRegulationUniqueIdentifier;
    private LocalDate paymentsReportingRegulationDate;
    private String paymentsReportingSenderPspType;
    private String paymentsReportingSenderPspCode;
    private String paymentsReportingSenderPspName;
    private String paymentsReportingReceiverOrganizationType;
    private String paymentsReportingReceiverOrganizationCode;
    private String paymentsReportingReceiverOrganizationName;
    private Long paymentsReportingTotalPayments;
    private Long paymentsReportingTotalAmountCents;
    private String paymentsReportingIuv;
    private String paymentsReportingIur;
    private Long paymentsReportingAmountPaidCents;
    private String paymentsReportingPaymentOutcomeCode;
    private LocalDate paymentsReportingPayDate;
    private LocalDate paymentsReportingCreationDate;
    private String treasuryAbiCode;
    private String treasuryCabCode;
    private String treasuryAccountRegistryCode;
    private LocalDate treasuryBillDate;
    private LocalDate treasuryRegionValueDate;
    private Long treasuryBillAmountCents;
    private String treasurySignCode;
    private String treasuryRemittanceCode;
    private String treasuryPspLastName;
    private String treasuryIuf;
    private String treasuryIuv;
    private LocalDateTime treasuryCreationDate;
    private String treasuryBillYear;
    private String treasuryBillCode;
    private String treasuryDomainIdCode;
    private OffsetDateTime treasuryReceptionDate;
    private String treasuryDocumentYear;
    private String treasuryDocumentCode;
    private String treasuryProvisionalAe;
    private String treasuryProvisionalCode;
    private LocalDate treasuryActualSuspensionDate;
    private String treasuryManagementProvisionalCode;
    @Enumerated(EnumType.STRING)
    private ClassificationsEnum classificationLabel;
    private LocalDate lastClassificationDate;
  }
