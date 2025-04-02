package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.citizen.service.PersonalDataService;
import it.gov.pagopa.pu.classification.dto.ClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.ClassificationViewNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationViewPIIMapperTest {
  @Mock
  private PersonalDataService personalDataServiceMock;

  private ClassificationViewPIIMapper mapper;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new ClassificationViewPIIMapper(personalDataServiceMock);
  }

  @Test
  void testMapper() {
    // Given
    ClassificationViewNoPII classificationViewNoPII = podamFactory.manufacturePojo(ClassificationViewNoPII.class);
    ReceiptPIIDTO receiptPIIDTO = podamFactory.manufacturePojo(ReceiptPIIDTO.class);

    when(personalDataServiceMock.get(classificationViewNoPII.getReceiptPersonalDataId(), ReceiptPIIDTO.class))
      .thenReturn(receiptPIIDTO);

    // When
    ClassificationViewDTO result = mapper.map(classificationViewNoPII);

    // Then
    assertNotNull(result);
    TestUtils.checkNotNullFields(result);
  }
}
