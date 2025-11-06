package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.LocalDateIntervalFilter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExportClassificationsFilterDTO {

  private List<String> iuf;
  private String iud;
  private List<String> iuv;
  private List<String> iur;
  @NotNull
  private Set<ClassificationsEnum> label;

  private LocalDateIntervalFilter lastClassificationDate;
  private LocalDateIntervalFilter payDate;
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
  private Set<String> debtPositionTypeOrgCodes;
}
