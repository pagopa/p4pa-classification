package it.gov.pagopa.pu.classification.mapper.pages;

import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.model.Assessments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PagedAssessmentsViewMapperTest {

  private PodamFactory podamFactory;
  private PagedAssessmentsViewMapper pagedAssessmentsViewMapper;

  @BeforeEach
  void setUp() {
    pagedAssessmentsViewMapper = new PagedAssessmentsViewMapper();
    podamFactory = new PodamFactoryImpl();
  }

  @Test
  void givenPagedAssessmentsWhenMapThenCorrectMapping() {
    //given
    List<Assessments> content = List.of(
      podamFactory.manufacturePojo(Assessments.class));
    Pageable pageable = PageRequest.of(0, 10);
    Page<Assessments> pagedAssessments = new PageImpl<>(
      content, pageable, 1);
    //when
    PagedAssessmentsView result = pagedAssessmentsViewMapper.map(pagedAssessments);
    //then
    assertNotNull(result);
    assertEquals(pagedAssessments.getContent(), result.getContent());
    assertEquals(pagedAssessments.getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedAssessments.getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedAssessments.getNumber(), result.getNumber());
    assertEquals(pagedAssessments.getSize(), result.getSize());
  }

  @Test
  void givenPagedAssessmentsEmptyContentWhenMapThenCorrectMapping() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<Assessments> pagedAssessments = new PageImpl<>(
      Collections.emptyList(), pageable, 0);

    PagedAssessmentsView result = pagedAssessmentsViewMapper.map(pagedAssessments);

    assertNotNull(result);
    assertEquals(Collections.emptyList(), result.getContent());
    assertEquals(pagedAssessments.getTotalPages(),
      result.getTotalPages());
    assertEquals(pagedAssessments.getTotalElements(),
      result.getTotalElements());
    assertEquals(pagedAssessments.getNumber(), result.getNumber());
    assertEquals(pagedAssessments.getSize(), result.getSize());
  }

  @Test
  void givenNullPagedAssessmentsWhenMapThenCorrectMapping() {
    PagedAssessmentsView result = pagedAssessmentsViewMapper.map(null);
    assertNotNull(result);
    assertEquals(new PagedAssessmentsView(), result);
  }

  @Test
  void givenUnpagedAssessmentsWhenMapThenCorrectMapping() {
    List<Assessments> content = List.of(
      podamFactory.manufacturePojo(Assessments.class));
    Page<Assessments> unpagedAssessments = new PageImpl<>(
      content, Pageable.unpaged(), 1);

    PagedAssessmentsView result = pagedAssessmentsViewMapper.map(unpagedAssessments);

    assertNotNull(result);
    assertEquals(unpagedAssessments.getContent(), result.getContent());
    assertNull(result.getTotalPages());
    assertNull(result.getTotalElements());
    assertNull(result.getNumber());
    assertNull(result.getSize());
  }
}
