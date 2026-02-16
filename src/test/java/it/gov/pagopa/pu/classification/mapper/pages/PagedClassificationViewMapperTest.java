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
    //given
    int pageSize = 10;
    long totalElements = 1;

    ClassificationViewNoPII classificationViewNoPII = podamFactory.manufacturePojo(ClassificationViewNoPII.class);

    List<ClassificationViewNoPII> content = List.of(classificationViewNoPII);

    Pageable pageable = PageRequest.of(0, pageSize);

    Page<ClassificationViewNoPII> classificationViewNoPIIPage = new PageImpl<>(content, pageable, totalElements);

    ClassificationViewDTO classificationViewDTO = podamFactory.manufacturePojo(ClassificationViewDTO.class);

    when(classificationViewPIIMapperMock.map(classificationViewNoPII)).thenReturn(classificationViewDTO);
    //when

    PagedClassificationView result = mapper.map2PagedClassificationView(classificationViewNoPIIPage);
    //then
    assertNotNull(result);
    assertFalse(result.getContent().isEmpty());
    assertEquals(1, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertEquals(10, result.getSize());
    assertEquals(0, result.getNumber());

    TestUtils.checkNotNullFields(result);
  }

  @Test
  void givenEmptyPagedClassificationViewNoPIIWhenMap2PagedClassificationViewThenReturnEmptyCollection(){
    //given
    int pageSize = 10;
    long totalElements = 0;

    Pageable pageable = PageRequest.of(0, pageSize);

    Page<ClassificationViewNoPII> pagedInstallmentPaidViewNoPII = new PageImpl<>(Collections.emptyList(), pageable, totalElements);
    //when

    PagedClassificationView result = mapper.map2PagedClassificationView(pagedInstallmentPaidViewNoPII);
    //then
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
    //when
    PagedClassificationView result = mapper.map2PagedClassificationView(null);
    //then
    assertNotNull(result);
  }

  @Test
  void givenUnPagedClassificationViewNoPIIWhenMap2PagedClassificationViewThenReturnPagedInstallmentPaidView(){
    //given
    ClassificationViewNoPII classificationViewNoPII = podamFactory.manufacturePojo(ClassificationViewNoPII.class);

    List<ClassificationViewNoPII> content = List.of(classificationViewNoPII);

    Page<ClassificationViewNoPII> pagedInstallmentPaidViewNoPII = new PageImpl<>(content);

    ClassificationViewDTO classificationViewDTO = podamFactory.manufacturePojo(ClassificationViewDTO.class);

    when(classificationViewPIIMapperMock.map(classificationViewNoPII)).thenReturn(classificationViewDTO);

    //when
    PagedClassificationView result = mapper.map2PagedClassificationView(pagedInstallmentPaidViewNoPII);

    //then
    assertNotNull(result);
    assertFalse(result.getContent().isEmpty());

    TestUtils.checkNotNullFields(result, "size","totalPages", "totalElements", "number");
  }
}
