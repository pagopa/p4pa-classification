package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.AssessmentsDetailDataDTO;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class Assessments2AssessmentsDetailDataMapper {
  public AssessmentsDetailDataDTO map(Assessments assessments, List<AssessmentsDetail> assessmentsDetail) {
    return AssessmentsDetailDataDTO.builder()
      .assessments(assessments)
      .assessmentsDetailList(assessmentsDetail)
      .build();
  }
}
