package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.ClassificationDetailViewNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationDetailViewPIIMapperTest {
  @Mock
  private PersonalDataService personalDataServiceMock;

  private ClassificationDetailViewPIIMapper mapper;
  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new ClassificationDetailViewPIIMapper(personalDataServiceMock);
  }

  @Test
  void testMapper() {
    ClassificationDetailViewNoPII classificationDetailViewNoPII = podamFactory.manufacturePojo(ClassificationDetailViewNoPII.class);
    ReceiptPIIDTO receiptPIIDTO = podamFactory.manufacturePojo(ReceiptPIIDTO.class);

    when(personalDataServiceMock.get(classificationDetailViewNoPII.getReceiptPersonalDataId(), ReceiptPIIDTO.class))
      .thenReturn(receiptPIIDTO);

    ClassificationDetailViewDTO result = mapper.map(classificationDetailViewNoPII);

    assertNotNull(result);
    TestUtils.reflectionEqualsByName(classificationDetailViewNoPII, result);
    TestUtils.checkNotNullFields(result);
  }
}
