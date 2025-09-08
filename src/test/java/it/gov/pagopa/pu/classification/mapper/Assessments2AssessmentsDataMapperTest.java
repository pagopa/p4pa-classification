package it.gov.pagopa.pu.classification.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.classification.dto.AssessmentsDataDTO;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class Assessments2AssessmentsDataMapperTest {

  @InjectMocks
  private Assessments2AssessmentsDataMapper mapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void whenMapThenOk() {
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    AssessmentsDetail assessmentsDetail = podamFactory.manufacturePojo(AssessmentsDetail.class);

    AssessmentsDataDTO result = mapper.map(assessments, assessmentsDetail);

    assertNotNull(result);
    TestUtils.checkNotNullFields(result);
  }
}
