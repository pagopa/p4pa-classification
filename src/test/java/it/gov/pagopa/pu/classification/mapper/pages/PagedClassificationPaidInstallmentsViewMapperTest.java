package it.gov.pagopa.pu.classification.mapper.pages;

import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationPaidInstallmentsView;
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

class PagedClassificationPaidInstallmentsViewMapperTest {

  private PodamFactory podamFactory;
  private PagedClassificationPaidInstallmentsViewMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new PagedClassificationPaidInstallmentsViewMapper();
    podamFactory = new PodamFactoryImpl();
  }

  @Test
  void givenPagedPaidInstallmentsWhenMapThenCorrectMapping() {
    List<ClassificationPaidInstallmentsView> content = List.of(
      podamFactory.manufacturePojo(ClassificationPaidInstallmentsView.class));
    Pageable pageable = PageRequest.of(0, 10);
    Page<ClassificationPaidInstallmentsView> page = new PageImpl<>(content, pageable, 1);

    PagedClassificationPaidInstallmentsView result = mapper.map(page);

    assertNotNull(result);
    assertEquals(page.getContent(), result.getContent());
    assertEquals(page.getTotalPages(), result.getTotalPages());
    assertEquals(page.getTotalElements(), result.getTotalElements());
    assertEquals(page.getNumber(), result.getNumber());
    assertEquals(page.getSize(), result.getSize());
  }

  @Test
  void givenPagedPaidInstallmentsEmptyContentWhenMapThenCorrectMapping() {
    Pageable pageable = PageRequest.of(0, 10);
    Page<ClassificationPaidInstallmentsView> page = new PageImpl<>(
      Collections.emptyList(), pageable, 0);

    PagedClassificationPaidInstallmentsView result = mapper.map(page);

    assertNotNull(result);
    assertEquals(Collections.emptyList(), result.getContent());
    assertEquals(page.getTotalPages(), result.getTotalPages());
    assertEquals(page.getTotalElements(), result.getTotalElements());
    assertEquals(page.getNumber(), result.getNumber());
    assertEquals(page.getSize(), result.getSize());
  }

  @Test
  void givenNullPagedPaidInstallmentsWhenMapThenCorrectMapping() {
    PagedClassificationPaidInstallmentsView result = mapper.map(null);

    assertNotNull(result);
    assertEquals(new PagedClassificationPaidInstallmentsView(), result);
  }

  @Test
  void givenUnpagedPaidInstallmentsWhenMapThenCorrectMapping() {
    List<ClassificationPaidInstallmentsView> content = List.of(
      podamFactory.manufacturePojo(ClassificationPaidInstallmentsView.class));
    Page<ClassificationPaidInstallmentsView> page = new PageImpl<>(content, Pageable.unpaged(), 1);

    PagedClassificationPaidInstallmentsView result = mapper.map(page);

    assertNotNull(result);
    assertEquals(page.getContent(), result.getContent());
    assertNull(result.getTotalPages());
    assertNull(result.getTotalElements());
    assertNull(result.getNumber());
    assertNull(result.getSize());
  }
}
