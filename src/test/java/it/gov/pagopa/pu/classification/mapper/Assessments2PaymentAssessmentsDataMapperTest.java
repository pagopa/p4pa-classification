package it.gov.pagopa.pu.classification.mapper;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.classification.dto.PaymentAssessmentsDataDTO;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.util.TestUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class Assessments2PaymentAssessmentsDataMapperTest {

  @InjectMocks
  private Assessments2PaymentAssessmentsDataMapper mapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @Test
  void whenMapThenOk() {
    Assessments assessments = podamFactory.manufacturePojo(Assessments.class);
    AssessmentsDetail assessmentsDetail = podamFactory.manufacturePojo(AssessmentsDetail.class);

    PaymentAssessmentsDataDTO result = mapper.map(assessments, List.of(assessmentsDetail));

    assertNotNull(result);
    TestUtils.checkNotNullFields(result);
  }
}
