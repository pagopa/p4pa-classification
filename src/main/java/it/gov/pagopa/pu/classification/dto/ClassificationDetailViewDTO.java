package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionOrigin;
import it.gov.pagopa.pu.debtposition.dto.generated.PersonDTO;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptOriginType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClassificationDetailViewDTO {
  //Classification fields
  private Long classificationId;
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
  private String debtPositionTypeOrgDescription;
  private String installmentIngestionFlowFileName;
  private String receiptOrgFiscalCode;
  private String receiptPaymentReceiptId;
  private OffsetDateTime receiptPaymentDateTime;
  private String receiptPaymentRequestId;
  private String receiptIdPsp;
  private String receiptPspCompanyName;
  private String organizationEntityType;
  private String organizationName;
  private PersonDTO receiptPayer;
  private PersonDTO receiptDebtor;
  private String receiptPaymentOutcomeCode;
  private Long receiptPaymentAmount;
  private String receiptCreditorReferenceId;
  private Long transferAmount;
  private String transferCategory;
  private OffsetDateTime receiptCreationDate;
  private String installmentBalance;

  //Treasury fields
  private String billYear;
  private String billCode;
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
  private String pspIdentifier;
  private OffsetDateTime flowDateTime;
  private String senderPspType;
  private String senderPspCode;
  private String senderPspName;
  private String receiverOrganizationType;
  private String receiverOrganizationCode;
  private String receiverOrganizationName;
  private Long totalPayments;
  private Long totalAmountCents;
  private Long amountPaidCents;
  private String paymentOutcomeCode;
  private LocalDate acquiringDate;
  private String bicCodePouringBank;

  //PaymentNotificationNoPII fields
  private LocalDate paymentExecutionDate;
  private String paymentType;
  private String balance;
  private byte[] remittanceInformationHash;
  private byte[] debtorFiscalCodeHash;
  private PersonDTO paymentNotificationDebtor;
  private String paymentNotificationRemittanceInformation;
  private String paymentNotificationIud;
  private Long paymentNotificationAmountPaidCents;
  private String paymentNotificationDebtPositionTypeOrgCode;

  @Enumerated(EnumType.STRING)
  private DebtPositionOrigin debtPositionOrigin;
  @Enumerated(EnumType.STRING)
  private ReceiptOriginType receiptOrigin;
}
