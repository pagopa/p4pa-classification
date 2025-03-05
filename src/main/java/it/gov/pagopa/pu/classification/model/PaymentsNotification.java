package it.gov.pagopa.pu.classification.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments_notification")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "paymentsNotificationId", callSuper = false)
public class PaymentsNotification extends BaseEntity implements Serializable{

  @Id
  private String paymentsNotificationId;
  @NotNull
  private Long organizationId;
  @NotNull
  private Long ingestionFlowFileId;
  @NotNull
  private String iud;
  @NotNull
  private String iuv;
  @NotNull
  private Date paymentExecutionDate;
  @NotNull
  private String paymentType;
  @NotNull
  private Long amountPaidCents;
  private Long paCommission;
  @NotNull
  private String remittanceInformation;
  @NotNull
  private String transferCategory;
  @NotNull
  private String debtPositionTypeOrgCode;
  private String balance;
  private Long personalDataId;
  private byte[] remittanceInformationHash;
  private byte[] debtor_fiscal_code_hash;


//region keep updated semanticId
  public static String buildSemanticId(PaymentsNotification paymentsReporting) {
    return paymentsReporting.getOrganizationId() + "-" +
      paymentsReporting.getIud();
  }

  private void setSemanticId() {
    this.paymentsNotificationId = buildSemanticId(this);
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
    setSemanticId();
  }

  public void setIud(String iud) {
    this.iud = iud;
    setSemanticId();
  }
//endregion
}
