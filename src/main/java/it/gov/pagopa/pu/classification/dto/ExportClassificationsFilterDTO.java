package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.LocalDateIntervalFilter;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExportClassificationsFilterDTO {

  private String iuf;
  private String iud;
  private String iuv;
  private String iur;
  @NotNull
  private ClassificationsEnum label;

  private LocalDateIntervalFilter lastClassificationDate;
  private LocalDateTimeIntervalFilter payDate;
  private OffsetDateTimeIntervalFilter paymentDateTime;
  private LocalDateIntervalFilter regulationDate;
  private LocalDateIntervalFilter billDate;
  private LocalDateIntervalFilter regionValueDate;

  private String regulationUniqueIdentifier;
  private String accountRegistryCode;
  private Long billAmountCents;
  private String remittanceInformation;
  private String pspCompanyName;
  private String pspLastName;
}
