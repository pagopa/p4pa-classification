package it.gov.pagopa.pu.classification.mapper.pages;

import it.gov.pagopa.pu.classification.dto.FullClassificationViewDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.mapper.pii.view.FullClassificationViewPIIMapper;
import it.gov.pagopa.pu.classification.model.view.classification.FullClassificationViewNoPII;
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
class PagedFullClassificationViewMapperTest {
  @Mock
  private FullClassificationViewPIIMapper fullClassificationViewPIIMapper;

  private PagedFullClassificationViewMapper mapper;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new PagedFullClassificationViewMapper(fullClassificationViewPIIMapper);
  }

  @Test
  void givenValidPagedFullClassificationViewNoPIIWhenMap2PagedFullClassificationViewThenReturnPagedFullClassificationView(){
    // Given
    int pageSize = 10;
    long totalElements = 1;

    Pageable pageable = PageRequest.of(0, pageSize);
    List<FullClassificationViewNoPII> content = List.of(podamFactory.manufacturePojo(FullClassificationViewNoPII.class));
    Page<FullClassificationViewNoPII> pagedInstallmentPaidViewNoPII = new PageImpl<>(content, pageable, totalElements);
    List<FullClassificationViewDTO> expectedContent = List.of(podamFactory.manufacturePojo(FullClassificationViewDTO.class));

    when(fullClassificationViewPIIMapper.mapAll(content)).thenReturn(expectedContent);

    // When
    PagedFullClassificationView result = mapper.map2PagedClassificationView(pagedInstallmentPaidViewNoPII);

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
  void givenEmptyPagedFullClassificationViewNoPIIWhenMap2PagedFullClassificationViewThenReturnEmptyCollection(){
    // Given
    int pageSize = 10;
    long totalElements = 0;

    Pageable pageable = PageRequest.of(0, pageSize);

    Page<FullClassificationViewNoPII> pages = new PageImpl<>(Collections.emptyList(), pageable, totalElements);

    // When
    PagedFullClassificationView result = mapper.map2PagedClassificationView(pages);

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
  void givenNullPagedFullClassificationViewNoPIIWhenMap2PagedFullClassificationViewThenReturnNewPagedInstallmentPaidView(){
    // When
    PagedFullClassificationView result = mapper.map2PagedClassificationView(null);

    // Then
    assertNotNull(result);
  }

  @Test
  void givenUnPagedFullClassificationViewNoPIIWhenMap2PagedFullClassificationViewThenReturnPagedInstallmentPaidView(){
    // Given
    List<FullClassificationViewNoPII> content = List.of(podamFactory.manufacturePojo(FullClassificationViewNoPII.class));
    Page<FullClassificationViewNoPII> pages = new PageImpl<>(content);
    List<FullClassificationViewDTO> expectedContent = List.of(podamFactory.manufacturePojo(FullClassificationViewDTO.class));

    when(fullClassificationViewPIIMapper.mapAll(content)).thenReturn(expectedContent);

    // When
    PagedFullClassificationView result = mapper.map2PagedClassificationView(pages);

    // Then
    assertNotNull(result);
    assertSame(expectedContent, result.getContent());

    TestUtils.checkNotNullFields(result, "size","totalPages", "totalElements", "number");
  }
}
