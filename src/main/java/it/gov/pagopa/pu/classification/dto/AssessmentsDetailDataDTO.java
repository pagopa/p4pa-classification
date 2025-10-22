package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentsDetailDataDTO {
  // Assessments
  private Assessments assessments;
  // AssessmentsDetail
  private List<AssessmentsDetail> assessmentsDetailList;
}
