package it.gov.pagopa.pu.classification.mapper.pages;

import it.gov.pagopa.pu.classification.dto.ClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.mapper.pii.view.ClassificationViewPIIMapper;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationViewNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagedClassificationViewMapperTest {
  @Mock
  private ClassificationViewPIIMapper classificationViewPIIMapperMock;

  private PagedClassificationViewMapper mapper;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new PagedClassificationViewMapper(classificationViewPIIMapperMock);
  }

  @Test
  void givenValidPagedClassificationViewNoPIIWhenMap2PagedClassificationViewThenReturnPagedClassificationView(){
    // Given
    int pageSize = 10;
    long totalElements = 1;

    Pageable pageable = PageRequest.of(0, pageSize);
    List<ClassificationViewNoPII> content = List.of(podamFactory.manufacturePojo(ClassificationViewNoPII.class));
    Page<ClassificationViewNoPII> classificationViewNoPIIPage = new PageImpl<>(content, pageable, totalElements);
    List<ClassificationViewDTO> expectedContent = List.of(podamFactory.manufacturePojo(ClassificationViewDTO.class));

    when(classificationViewPIIMapperMock.mapAll(content)).thenReturn(expectedContent);

    // When
    PagedClassificationView result = mapper.map2PagedClassificationView(classificationViewNoPIIPage);

    // Then
    assertNotNull(result);
    assertSame(expectedContent, result.getContent());
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertEquals(10, result.getSize());
    assertEquals(0, result.getNumber());

    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenEmptyPagedClassificationViewNoPIIWhenMap2PagedClassificationViewThenReturnEmptyCollection(){
    // Given
    int pageSize = 10;
    long totalElements = 0;

    Pageable pageable = PageRequest.of(0, pageSize);

    Page<ClassificationViewNoPII> pagedInstallmentPaidViewNoPII = new PageImpl<>(Collections.emptyList(), pageable, totalElements);

    // When
    PagedClassificationView result = mapper.map2PagedClassificationView(pagedInstallmentPaidViewNoPII);

    // Then
    assertNotNull(result);
    assertTrue(result.getContent().isEmpty());
    assertEquals(0, result.getTotalElements());
    assertEquals(0, result.getTotalPages());
    assertEquals(10, result.getSize());
    assertEquals(0, result.getNumber());

    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenNullPagedClassificationViewNoPIIWhenMap2PagedClassificationViewThenReturnNewPagedInstallmentPaidView(){
    // When
    PagedClassificationView result = mapper.map2PagedClassificationView(null);

    // Then
    assertNotNull(result);
  }

  @Test
  void givenUnPagedClassificationViewNoPIIWhenMap2PagedClassificationViewThenReturnPagedInstallmentPaidView(){
    // Given
    List<ClassificationViewNoPII> content = List.of(podamFactory.manufacturePojo(ClassificationViewNoPII.class));
    Page<ClassificationViewNoPII> pagedInstallmentPaidViewNoPII = new PageImpl<>(content);
    List<ClassificationViewDTO> expectedContent = List.of(podamFactory.manufacturePojo(ClassificationViewDTO.class));

    when(classificationViewPIIMapperMock.mapAll(content)).thenReturn(expectedContent);

    // When
    PagedClassificationView result = mapper.map2PagedClassificationView(pagedInstallmentPaidViewNoPII);

    // Then
    assertNotNull(result);
    assertSame(expectedContent, result.getContent());

    TestUtils.checkNotNullFields(result, "size","totalPages", "totalElements", "number");
  }
}
