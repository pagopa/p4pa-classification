package it.gov.pagopa.pu.classification.model.view.classification;

import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.common.pii.dto.No2PIIDTO;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FullClassificationViewNoPII extends ClassificationViewNoPII implements No2PIIDTO<ReceiptPIIDTO, PaymentNotificationPIIDTO> {
  private Long paymentNotificationIngestionFlowFileId;
  private String paymentNotificationIud;
  private String paymentNotificationIuv;
  private LocalDate paymentNotificationPaymentExecutionDate;
  private String paymentNotificationPaymentType;
  private Long paymentNotificationAmountPaidCents;
  private Long paymentNotificationPaCommissionCents;
  private String paymentNotificationRemittanceInformation;
  private String paymentNotificationTransferCategory;
  private String paymentNotificationDebtPositionTypeOrgCode;
  private String paymentNotificationBalance;
  private Long paymentNotificationPersonalDataId;

  @Override
  public Long getPersonalDataId2() {
    return paymentNotificationPersonalDataId;
  }
}
