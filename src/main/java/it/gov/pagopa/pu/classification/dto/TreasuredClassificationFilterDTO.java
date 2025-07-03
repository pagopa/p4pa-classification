package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.LocalDateIntervalFilter;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TreasuredClassificationFilterDTO {

  @NotNull
  private ClassificationsEnum label;
  private String iud;
  private String iuv;
  private String iur;

  private LocalDateIntervalFilter lastClassificationDate;
  private LocalDateIntervalFilter payDate;
  private OffsetDateTimeIntervalFilter paymentDateTime;
  private LocalDateIntervalFilter regulationDate;
  private LocalDateIntervalFilter billDate;
  private LocalDateIntervalFilter regionValueDate;

  private String pspCompanyName;
  private String pspLastName;
  private String iuf;
  private String regulationUniqueIdentifier;
  private String accountRegistryCode;
  private Long billAmountCents;
  private String remittanceInformation;

  private String debtorFiscalCode;
  private Set<String> debtPositionTypeOrgCodes;

  private String billYear;
  private String billCode;
  private String documentYear;
  private String documentCode;
  private String provisionalAe;
  private String provisionalCode;
}
