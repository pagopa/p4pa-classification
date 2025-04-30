package it.gov.pagopa.pu.classification.dto.assessments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDTO {
  private String assessmentCode;
  private Long amountCents;
}
