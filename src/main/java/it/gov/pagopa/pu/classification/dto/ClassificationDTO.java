package it.gov.pagopa.pu.classification.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationDTO {

  private String iuf;
  private String iud;
  private String iuv;
  private String iur;
  @NotNull
  private String label;

  private OffsetDateTimeIntervalFilter lastClassificationDate;
  private OffsetDateTimeIntervalFilter payDate;
  private OffsetDateTimeIntervalFilter paymentDateTime;
  private OffsetDateTimeIntervalFilter regulationDate;
  private OffsetDateTimeIntervalFilter billDate;
  private OffsetDateTimeIntervalFilter regionValueDate;

  private String regulationUniqueIdentifier;
  private String accountRegistryCode;
  private Long billAmountCents;
  private String remittanceInformation;
  private String pspCompanyName;
  private String pspLastName;
}
