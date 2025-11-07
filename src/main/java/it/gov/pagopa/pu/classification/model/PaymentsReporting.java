package it.gov.pagopa.pu.classification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "payments_reporting")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "paymentsReportingId", callSuper = false)
public class PaymentsReporting extends BaseEntity implements Serializable{

  @Id
  private String paymentsReportingId;
  @NotNull
  private Long ingestionFlowFileId;
  @NotNull
  private Long organizationId;
  @NotNull
  private String iuv;
  @NotNull
  private String iur;
  @NotNull
  private Integer transferIndex;
  @NotNull
  private String pspIdentifier;
  @NotNull
  private String iuf;
  @NotNull
  private OffsetDateTime flowDateTime;
  @NotNull
  private String regulationUniqueIdentifier;
  @NotNull
  private LocalDate regulationDate;
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
  private LocalDate payDate;
  @NotNull
  private LocalDate acquiringDate;
  private String bicCodePouringBank;

//region keep updated semanticId
  public static String buildSemanticId(PaymentsReporting paymentsReporting) {
    return paymentsReporting.getIuf() + "_" +
      paymentsReporting.getIuv() + "_" +
      paymentsReporting.getTransferIndex() + "_" +
      paymentsReporting.getOrganizationId();
  }

  private void setSemanticId() {
    this.paymentsReportingId = buildSemanticId(this);
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
    setSemanticId();
  }

  public void setIuv(String iuv) {
    this.iuv = iuv;
    setSemanticId();
  }

  public void setIuf(String iuf) {
    this.iuf = iuf;
    setSemanticId();
  }

  public void setTransferIndex(Integer transferIndex) {
    this.transferIndex = transferIndex;
    setSemanticId();
  }
//endregion
}
