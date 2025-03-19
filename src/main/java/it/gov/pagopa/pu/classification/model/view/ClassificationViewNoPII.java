package it.gov.pagopa.pu.classification.model.view;

  import jakarta.persistence.Entity;
  import jakarta.persistence.Id;
  import jakarta.persistence.Table;
  import jakarta.validation.constraints.NotNull;
  import lombok.AllArgsConstructor;
  import lombok.Data;
  import lombok.EqualsAndHashCode;
  import lombok.NoArgsConstructor;

  import java.io.Serializable;
  import java.time.LocalDate;
  import java.time.LocalDateTime;

  @Entity
  @Table(name = "classification")
  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  @EqualsAndHashCode(callSuper = false)
  public class ClassificationViewNoPII implements Serializable {
    @Id
    private Long classificationId;
    private String receiptFileName;
    private Long receiptNumTotalRows;
    private String receiptIud;
    private String receiptIuv;
    private String receiptObjectVersion;
    private String receiptOrgFiscalCode;
    private String receiptBrokerId;
    private String receiptPaymentReceiptId;
    private LocalDateTime receiptPaymentDateTime;
    private String receiptPaymentRequestId;
    private LocalDateTime receiptPaymentRequestDateTime;
    private String receiptPspCodeType;
    private String receiptIdPsp;
    private String receiptPspCompanyName;
    private String receiptPspOperatingUnitCode;
    private String receiptPspOperatingUnitName;
    private String receiptPspAddress;
    private String receiptPspCivic;
    private String receiptPspPostalCode;
    private String receiptPspLocation;
    private String receiptPspProvince;
    private String receiptPspNation;
    private String receiptOrgEntityType;

    private String receiptBeneficiaryOrgName;
    private String receiptBeneficiaryOrgOperatingUnitCode;
    private String receiptBeneficiaryOrgOperatingUnitName;
    private String receiptBeneficiaryOrgAddress;
    private String receiptBeneficiaryOrgCivic;
    private String receiptBeneficiaryOrgPostalCode;
    private String receiptBeneficiaryOrgLocation;
    private String receiptBeneficiaryOrgProvince;
    private String receiptBeneficiaryOrgNation;

    @NotNull
    private Long receiptPersonalDataId;

    private String receiptPaymentOutcomeCode;
    private Long receiptPaymentAmount;
    private String receiptCreditorReferenceId;
    private String receiptPaymentContextId;
    private Long receiptTransferAmount;
    private String receiptTransferPaymentOutcomeCode;
    private LocalDateTime receiptTransferPaymentDateTime;
    private String receiptTransferPaymentReceiptId;
    private String receiptTransferRemittanceInformation;
    private String receiptTransferCategory;
    private String receiptDebtPositionTypeOrgId;
    private LocalDateTime receiptCreationDate;
    private String receiptInstallmentBalance;
    private String paymentsReportingObjectVersion;
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
    private LocalDateTime paymentsReportingCreationDate;
    private String treasuryAbiCode;
    private String treasuryCabCode;
    private String treasuryAccountRegistryCode;
    private String treasuryCurrencyCode;
    private LocalDate treasuryBillDate;
    private LocalDate treasuryRegionValueDate;
    private Long treasuryBillAmountCents;
    private String treasurySignCode;
    private String treasuryRemittanceCode;
    private String treasuryCheckNumber;
    private String treasuryBankReference;
    private String treasuryCustomerReference;
    private LocalDate treasuryOrderDate;
    private String treasuryLastName;
    private String treasuryOrlCode;
    private String treasuryIuf;
    private String treasuryIuv;
    private LocalDateTime treasuryCreationDate;
    private String treasuryBillYear;
    private String treasuryBillCode;
    private String treasuryDomainIdCode;
    private LocalDate treasuryReceptionDate;
    private String treasuryDocumentYear;
    private String treasuryDocumentCode;
    private String treasuryProvisionalAe;
    private String treasuryProvisionalCode;
    private LocalDate treasuryActualSuspensionDate;
    private String treasuryManagementProvisionalCode;
    private String classificationLabel;
    private LocalDate classificationDate;
    private String typeCodePal;
    private String typeDescriptionPal;
    private String taxonomicCodePal;
    private String fiscalCodePal;
    private String namePal;
  }
