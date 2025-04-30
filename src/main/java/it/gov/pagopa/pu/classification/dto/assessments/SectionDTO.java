package it.gov.pagopa.pu.classification.dto.assessments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
  private String sectionCode;
  List<AssessmentDTO> assessments;
}
