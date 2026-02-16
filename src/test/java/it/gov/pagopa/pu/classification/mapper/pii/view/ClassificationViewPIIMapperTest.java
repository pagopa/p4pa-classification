package it.gov.pagopa.pu.classification.mapper.pii.view;

import it.gov.pagopa.pu.classification.dto.ClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationViewNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.common.pii.mapper.BasePIIMapperTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationViewPIIMapperTest extends BasePIIMapperTest<ClassificationViewDTO, ClassificationViewNoPII, ReceiptPIIDTO> {

  private ClassificationViewPIIMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new ClassificationViewPIIMapper(personalDataServiceMock);
  }

  @Override
  public ClassificationViewPIIMapper getMapper() {
    return mapper;
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
    TestUtils.reflectionEqualsByName(classificationViewNoPII, result);
    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNullPersonalDataIdWhenMapThenOk() {
    ClassificationViewNoPII classificationViewNoPII = podamFactory.manufacturePojo(ClassificationViewNoPII.class);

    when(personalDataServiceMock.get(classificationViewNoPII.getReceiptPersonalDataId(), ReceiptPIIDTO.class))
      .thenReturn(null);

    ClassificationViewDTO result = mapper.map(classificationViewNoPII);

    assertNotNull(result);
    TestUtils.reflectionEqualsByName(classificationViewNoPII, result);
    TestUtils.checkNotNullFields(result, "receiptDebtor", "receiptPayer");
  }
}
