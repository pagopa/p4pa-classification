package it.gov.pagopa.pu.classification.model.view;

import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "classification")
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ClassificationList implements Serializable {
  @Id
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
  private Long receiptPersonalDataId;
  private String receiptPaymentOutcomeCode;
  private Long receiptPaymentAmount;
  private String receiptCreditorReferenceId;
  private Long transferAmount;
  private String transferCategory;
  private OffsetDateTime receiptCreationDate;
  private String installmentBalance;

  private String treasuryBillYear;
  private String treasuryBillCode;
  private Long treasuryIngestionFlowFileId;
  private String treasuryIuf;
  private String treasuryIuv;
  private String treasuryAccountCode;
  private String treasuryDomainIdCode;
  private String treasuryTransactionTypeCode;
  private String treasuryRemittanceCode;
  private String treasuryRemittanceDescription;
  private Long treasuryBillAmountCents;
  private LocalDate treasuryBillDate;
  private OffsetDateTime treasuryReceptionDate;
  private String treasuryDocumentYear;
  private String treasuryDocumentCode;
  private String treasurySealCode;
  private String treasuryPspLastName;
  private String treasuryPspFirstName;
  private String treasuryPspAddress;
  private String treasuryPspPostalCode;
  private String treasuryPspCity;
  private String treasuryPspFiscalCode;
  private String treasuryPspVatNumber;
  private String treasuryAbiCode;
  private String treasuryCabCode;
  private String treasuryIbanCode;
  private String treasuryAccountRegistryCode;
  private String treasuryProvisionalAe;
  private String treasuryProvisionalCode;
  private Character treasuryAccountTypeCode;
  private String treasuryProcessCode;
  private String treasuryExecutionPgCode;
  private String treasuryTransferPgCode;
  private Long treasuryProcessPgNumber;
  private LocalDate treasuryRegionValueDate;
  private boolean treasuryIsRegularized;
  private LocalDate treasuryActualSuspensionDate;
  private String treasuryManagementProvisionalCode;
  private String treasuryEndToEndId;

}
