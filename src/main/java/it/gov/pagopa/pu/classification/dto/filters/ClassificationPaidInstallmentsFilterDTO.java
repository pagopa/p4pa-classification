package it.gov.pagopa.pu.classification.dto.filters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationPaidInstallmentsFilterDTO {
  private String iuv;
  private OffsetDateTimeIntervalFilter paymentDateTimeIntervalFilter;
  private OffsetDateTimeIntervalFilter receiptCreationDateIntervalFilter;
  private String debtPositionTypeOrgCode;
  private Set<String> iuds;
}
