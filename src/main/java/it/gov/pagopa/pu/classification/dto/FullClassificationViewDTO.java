package it.gov.pagopa.pu.classification.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.gov.pagopa.pu.classification.dto.pii.PaymentNotificationPIIDTO;
import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.classification.FullClassificationViewNoPII;
import it.gov.pagopa.pu.common.pii.dto.Full2PIIDTO;
import it.gov.pagopa.pu.debtposition.dto.generated.PersonDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
public class FullClassificationViewDTO extends BaseClassificationViewDTO implements Full2PIIDTO<FullClassificationViewNoPII, ReceiptPIIDTO, PaymentNotificationPIIDTO> {

  @JsonProperty("payNotice_ingestionFlowFileId")
  private Long paymentNotificationIngestionFlowFileId;

  @JsonProperty("payNotice_iud")
  private String paymentNotificationIud;

  @JsonProperty("payNotice_iuv")
  private String paymentNotificationIuv;

  @JsonProperty("payNotice_paymentExecutionDate")
  private LocalDate paymentNotificationPaymentExecutionDate;

  @JsonProperty("payNotice_paymentType")
  private String paymentNotificationPaymentType;

  @JsonProperty("payNotice_amountPaidCents")
  private Long paymentNotificationAmountPaidCents;

  @JsonProperty("payNotice_paCommissionCents")
  private Long paymentNotificationPaCommissionCents;

  @JsonProperty("payNotice_remittanceInformation")
  private String paymentNotificationRemittanceInformation;

  @JsonProperty("payNotice_transferCategory")
  private String paymentNotificationTransferCategory;

  @JsonProperty("payNotice_debtPositionTypeOrgCode")
  private String paymentNotificationDebtPositionTypeOrgCode;

  @JsonProperty("payNotice_balance")
  private String paymentNotificationBalance;

  @JsonProperty("payNotice_debtor")
  private PersonDTO paymentNotificationDebtor;

}
