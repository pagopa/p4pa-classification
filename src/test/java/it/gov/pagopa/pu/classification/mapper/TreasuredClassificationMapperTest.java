package it.gov.pagopa.pu.classification.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.dto.generated.TreasuredClassificationDTO;
import it.gov.pagopa.pu.classification.model.view.TreasuredClassification;
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
class TreasuredClassificationMapperTest {

  private TreasuredClassificationMapper mapper;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    mapper = new TreasuredClassificationMapper();
  }

  @Test
  void givenPagedTreasuredClassificationsDTOWhenMapThenCorrectMapping() {
    List<TreasuredClassification> content = List.of(
      podamFactory.manufacturePojo(TreasuredClassification.class));
    Pageable pageable = PageRequest.of(0, 10);
    Page<TreasuredClassification> pagedTreasuredClassifications = new PageImpl<>(
      content, pageable, 1);

    List<TreasuredClassificationDTO> expectedContent = pagedTreasuredClassifications.stream()
      .map(mapper::map2DTO)
      .toList();

    PagedTreasuredClassification result = mapper.map2PagedTreasuredClassification(
      pagedTreasuredClassifications);

    assertNotNull(result);
    assertEquals(expectedContent, result.getContent());
    assertEquals(pagedTreasuredClassifications.getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedTreasuredClassifications.getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedTreasuredClassifications.getNumber(), result.getNumber());
    assertEquals(pagedTreasuredClassifications.getSize(), result.getSize());
  }

  @Test
  void givenPagedTreasuredClassificationsEmptyContentWhenMapThenCorrectMapping() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<TreasuredClassification> pagedTreasuredClassifications = new PageImpl<>(
      Collections.emptyList(), pageable, 0);

    PagedTreasuredClassification result = mapper.map2PagedTreasuredClassification(
      pagedTreasuredClassifications);

    assertNotNull(result);
    assertEquals(Collections.emptyList(), result.getContent());
    assertEquals(pagedTreasuredClassifications.getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedTreasuredClassifications.getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedTreasuredClassifications.getNumber(), result.getNumber());
    assertEquals(pagedTreasuredClassifications.getSize(), result.getSize());
  }

  @Test
  void givenNullPagedTreasuredClassificationsWhenMapThenCorrectMapping() {
    PagedTreasuredClassification result = mapper.map2PagedTreasuredClassification(
      null);

    assertNotNull(result);
    assertEquals(new PagedTreasuredClassification(), result);
  }

  @Test
  void givenUnpagedPagedTreasuredClassificationsWhenMapThenCorrectMapping() {
    List<TreasuredClassification> content = List.of(
      podamFactory.manufacturePojo(TreasuredClassification.class));
    Page<TreasuredClassification> pagedTreasuredClassifications = new PageImpl<>(
      content, Pageable.unpaged(), 0);

    List<TreasuredClassificationDTO> expectedContent = pagedTreasuredClassifications.stream()
      .map(mapper::map2DTO)
      .toList();

    PagedTreasuredClassification result = mapper.map2PagedTreasuredClassification(
      pagedTreasuredClassifications);

    assertNotNull(result);
    assertEquals(expectedContent, result.getContent());
    assertNull(result.getTotalPages());
    assertNull(result.getTotalElements());
    assertNull(result.getNumber());
    assertNull(result.getSize());
  }

}
