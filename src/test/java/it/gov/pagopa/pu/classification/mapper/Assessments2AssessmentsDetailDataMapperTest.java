package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.AssessmentsDetailDataDTO;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class Assessments2AssessmentsDetailDataMapperTest {
  @InjectMocks
  private Assessments2AssessmentsDetailDataMapper mapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void whenMapThenOk() {
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    AssessmentsDetail assessmentsDetail = podamFactory.manufacturePojo(AssessmentsDetail.class);

    AssessmentsDetailDataDTO result = mapper.map(assessments, List.of(assessmentsDetail));

    assertNotNull(result);
    TestUtils.checkNotNullFields(result);
  }
}
