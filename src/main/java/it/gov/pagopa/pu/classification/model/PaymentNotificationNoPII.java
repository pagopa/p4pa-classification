package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.dto.PaymentNotificationPIIDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Entity
@Table(name = "payment_notification")
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of = "paymentNotificationId", callSuper = false)
public class PaymentNotificationNoPII extends BaseEntity implements Serializable, NoPIIEntity<PaymentNotificationPIIDTO>{

  @Id
  private String paymentNotificationId;
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
  private byte[] debtorFiscalCodeHash;


//region keep updated semanticId
  public static String buildSemanticId(PaymentNotificationNoPII paymentsReporting) {
    return paymentsReporting.getIud() + "_" +
      paymentsReporting.getOrganizationId();
  }

  private void setSemanticId() {
    this.paymentNotificationId = buildSemanticId(this);
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
