package it.gov.pagopa.pu.classification.model.view;

import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class ClassificationDetailViewNoPII implements Serializable {
  //Classification fields
  @Id
  @NotNull
  private Long classificationId;
  @NotNull
  private Long organizationId;
  private Long transferId;
  private String paymentNotificationId;
  private String paymentsReportingId;
  private String treasuryId;
  private String iuf;
  private String iud;
  private String iuv;
  private String iur;
  private int transferIndex;
  @NotNull
  @Enumerated(EnumType.STRING)
  private ClassificationsEnum label;
  private LocalDate lastClassificationDate;
  private LocalDate payDate;
  private OffsetDateTime paymentDateTime;
  private LocalDate regulationDate;
  private LocalDate billDate;
  private LocalDate regionValueDate;
  private String regulationUniqueIdentifier;
  private String accountRegistryCode;
  private Long billAmountCents;
  private String remittanceInformation;
  private String pspCompanyName;
  private String pspLastName;
  private String debtPositionTypeOrgCode;
  private String installmentIngestionFlowFileName;
  private String receiptOrgFiscalCode;
  private String receiptPaymentReceiptId;
  private OffsetDateTime receiptPaymentDateTime;
  private String receiptPaymentRequestId;
  private String receiptIdPsp;
  private String receiptPspCompanyName;
  private String organizationEntityType;
  private String organizationName;
  private Long receiptPersonalDataId;
  private String receiptPaymentOutcomeCode;
  private Long receiptPaymentAmount;
  private String receiptCreditorReferenceId;
  private Long transferAmount;
  private String transferCategory;
  private OffsetDateTime receiptCreationDate;
  private String installmentBalance;

  //Treasury fields
  @NotNull
  private String billYear;
  @NotNull
  private String billCode;
  @NotNull
  private Long ingestionFlowFileId;
  private String accountCode;
  private String domainIdCode;
  private String transactionTypeCode;
  private String remittanceCode;
  private String remittanceDescription;
  private OffsetDateTime receptionDate;
  private String documentYear;
  private String documentCode;
  private String sealCode;
  private String pspFirstName;
  private String pspAddress;
  private String pspPostalCode;
  private String pspCity;
  private String pspFiscalCode;
  private String pspVatNumber;
  private String abiCode;
  private String cabCode;
  private String ibanCode;
  private String provisionalAe;
  private String provisionalCode;
  private Character accountTypeCode;
  private String processCode;
  private String executionPgCode;
  private String transferPgCode;
  private Long processPgNumber;
  private LocalDate actualSuspensionDate;
  private String managementProvisionalCode;
  private String endToEndId;

  //PaymentsReporting fields
  @NotNull
  private String pspIdentifier;
  @NotNull
  private LocalDateTime flowDateTime;
  @NotNull
  private String senderPspType;
  @NotNull
  private String senderPspCode;
  private String senderPspName;
  private String receiverOrganizationType;
  private String receiverOrganizationCode;
  private String receiverOrganizationName;
  @NotNull
  private Long totalPayments;
  @NotNull
  private Long totalAmountCents;
  @NotNull
  private Long amountPaidCents;
  @NotNull
  private String paymentOutcomeCode;
  @NotNull
  private LocalDate acquiringDate;
  private String bicCodePouringBank;

  //PaymentNotificationNoPII fields
  @NotNull
  private LocalDate paymentExecutionDate;
  @NotNull
  private String paymentType;
  private String balance;
  private byte[] remittanceInformationHash;
  private byte[] debtorFiscalCodeHash;
}
