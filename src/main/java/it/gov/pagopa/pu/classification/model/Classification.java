package it.gov.pagopa.pu.classification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "classification")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Classification extends BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classification_generator")
  @SequenceGenerator(name = "classification_generator", sequenceName = "classification_id_seq", allocationSize = 1)
  private Long classificationId;
  @NotNull
  private Long organizationId;
  private Long transferId;
  private Long paymentNotifyId;
  private String paymentsReportingId;
  private String treasuryId;
  private String iuf;
  private String iud;
  private String iuv;
  private String iur;
  private int transferIndex;
  @NotNull
  private String label;
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

  private String receiptFileName;
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

  private String receiptTransferCategory;
  private OffsetDateTime receiptCreationDate;
  private String receiptInstallmentBalance;
}
