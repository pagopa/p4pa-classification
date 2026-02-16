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
    //given
    int pageSize = 10;
    long totalElements = 1;

    FullClassificationViewNoPII noPII = podamFactory.manufacturePojo(FullClassificationViewNoPII.class);

    List<FullClassificationViewNoPII> content = List.of(noPII);

    Pageable pageable = PageRequest.of(0, pageSize);

    Page<FullClassificationViewNoPII> pagedInstallmentPaidViewNoPII = new PageImpl<>(content, pageable, totalElements);

    FullClassificationViewDTO viewDTO = podamFactory.manufacturePojo(FullClassificationViewDTO.class);

    when(fullClassificationViewPIIMapper.map(noPII)).thenReturn(viewDTO);
    //when

    PagedFullClassificationView result = mapper.map2PagedClassificationView(pagedInstallmentPaidViewNoPII);
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
  void givenEmptyPagedFullClassificationViewNoPIIWhenMap2PagedFullClassificationViewThenReturnEmptyCollection(){
    //given
    int pageSize = 10;
    long totalElements = 0;

    Pageable pageable = PageRequest.of(0, pageSize);

    Page<FullClassificationViewNoPII> pages = new PageImpl<>(Collections.emptyList(), pageable, totalElements);
    //when

    PagedFullClassificationView result = mapper.map2PagedClassificationView(pages);
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
  void givenNullPagedFullClassificationViewNoPIIWhenMap2PagedFullClassificationViewThenReturnNewPagedInstallmentPaidView(){
    //when
    PagedFullClassificationView result = mapper.map2PagedClassificationView(null);
    //then
    assertNotNull(result);
  }

  @Test
  void givenUnPagedFullClassificationViewNoPIIWhenMap2PagedFullClassificationViewThenReturnPagedInstallmentPaidView(){
    //given
    FullClassificationViewNoPII noPII = podamFactory.manufacturePojo(FullClassificationViewNoPII.class);

    List<FullClassificationViewNoPII> content = List.of(noPII);

    Page<FullClassificationViewNoPII> pages = new PageImpl<>(content);

    FullClassificationViewDTO viewDTO = podamFactory.manufacturePojo(FullClassificationViewDTO.class);

    when(fullClassificationViewPIIMapper.map(noPII)).thenReturn(viewDTO);

    //when
    PagedFullClassificationView result = mapper.map2PagedClassificationView(pages);

    //then
    assertNotNull(result);
    assertFalse(result.getContent().isEmpty());

    TestUtils.checkNotNullFields(result, "size","totalPages", "totalElements", "number");
  }
}
