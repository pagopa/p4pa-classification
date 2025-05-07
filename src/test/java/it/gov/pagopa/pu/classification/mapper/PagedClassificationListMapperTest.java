package it.gov.pagopa.pu.classification.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import it.gov.pagopa.pu.classification.dto.ClassificationListDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationListDTO;
import it.gov.pagopa.pu.classification.util.TestUtils;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class PagedClassificationListMapperTest {

  private PagedClassificationListMapper mapper;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new PagedClassificationListMapper();
  }

  @Test
  void givenPagedClassificationListDTOWhenMapThenCorrectMapping() {
    List<ClassificationListDTO> content = List.of(
      podamFactory.manufacturePojo(ClassificationListDTO.class));
    Pageable pageable = PageRequest.of(0, 10);
    Page<ClassificationListDTO> pagedClassificationListDTO = new PageImpl<>(
      content, pageable, 1);

    PagedClassificationListDTO result = mapper.map2PagedClassificationListDTO(
      pagedClassificationListDTO);

    assertNotNull(result);
    assertEquals(pagedClassificationListDTO.getContent(), result.getContent());
    assertEquals(pagedClassificationListDTO.getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedClassificationListDTO.getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedClassificationListDTO.getNumber(), result.getNumber());
    assertEquals(pagedClassificationListDTO.getSize(), result.getSize());
  }

  @Test
  void givenPagedClassificationListDTOEmptyContentWhenMapThenCorrectMapping() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<ClassificationListDTO> pagedClassificationListDTO = new PageImpl<>(
      Collections.emptyList(), pageable, 0);

    PagedClassificationListDTO result = mapper.map2PagedClassificationListDTO(
      pagedClassificationListDTO);

    assertNotNull(result);
    assertEquals(Collections.emptyList(), result.getContent());
    assertEquals(pagedClassificationListDTO.getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedClassificationListDTO.getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedClassificationListDTO.getNumber(), result.getNumber());
    assertEquals(pagedClassificationListDTO.getSize(), result.getSize());
  }

  @Test
  void givenNullPagedClassificationListDTOWhenMapThenCorrectMapping() {
    PagedClassificationListDTO result = mapper.map2PagedClassificationListDTO(
      null);

    assertNotNull(result);
    assertEquals(new PagedClassificationListDTO(), result);
  }

  @Test
  void givenUnpagedPagedClassificationListDTOWhenMapThenCorrectMapping() {
    List<ClassificationListDTO> content = List.of(
      podamFactory.manufacturePojo(ClassificationListDTO.class));
    Page<ClassificationListDTO> pagedClassificationListDTO = new PageImpl<>(
      content, Pageable.unpaged(), 0);

    PagedClassificationListDTO result = mapper.map2PagedClassificationListDTO(
      pagedClassificationListDTO);

    assertNotNull(result);
    assertEquals(pagedClassificationListDTO.getContent(), result.getContent());
    assertNull(result.getTotalPages());
    assertNull(result.getTotalElements());
    assertNull(result.getNumber());
    assertNull(result.getSize());
  }

}
