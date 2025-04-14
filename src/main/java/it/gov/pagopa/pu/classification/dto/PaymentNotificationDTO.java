package it.gov.pagopa.pu.classification.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.gov.pagopa.pu.classification.model.PaymentNotificationNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotificationDTO implements FullPIIDTO<PaymentNotificationNoPII, PaymentNotificationPIIDTO> {

  private String paymentNotificationId;
  private Long organizationId;
  private Long ingestionFlowFileId;
  private String iud;
  private String iuv;
  private LocalDate paymentExecutionDate;
  private String paymentType;
  private Long amountPaidCents;
  private Long paCommission;
  private String remittanceInformation;
  private String transferCategory;
  private String debtPositionTypeOrgCode;
  private String balance;
  private Person debtor;
  private LocalDateTime creationDate;
  private LocalDateTime updateDate;
  private String updateOperatorExternalId;

  @JsonIgnore
  private PaymentNotificationNoPII noPII;

}
