package it.gov.pagopa.pu.classification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BalanceDTO {
  private String officeCode;
  private String debtPositionTypeOrgCode;
  private String sectionCode;
  private String assessmentCode;
  private Long amountCents;
}
