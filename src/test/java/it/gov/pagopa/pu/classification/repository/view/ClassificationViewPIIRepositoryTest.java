package it.gov.pagopa.pu.classification.repository.view;

import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.exception.custom.ExportTooManyRecordsException;
import it.gov.pagopa.pu.classification.mapper.PagedClassificationViewMapper;
import it.gov.pagopa.pu.classification.model.view.ClassificationViewNoPII;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(properties = {"data-export.classification-view.max-total-elements=10"})
class ClassificationViewPIIRepositoryTest {

  @Value("${data-export.classification-view.max-total-elements}")
  private Integer maxElements;

  @Mock
  private ClassificationViewNoPIIDTORepository classificationViewNoPIIDTORepositoryMock;

  @Mock
  private PagedClassificationViewMapper pagedClassificationViewMapperMock;

  ClassificationViewPIIRepository repository;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    repository = new ClassificationViewPIIRepositoryImpl(maxElements, classificationViewNoPIIDTORepositoryMock, pagedClassificationViewMapperMock);
  }

  @Test
  void givenValidParamWhenGetPagedClassificationViewThenReturnPagedClassificationView() {
    //given
    Long organizationId = 1L;
    ExportClassificationsFilterDTO exportClassificationsFilterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);
    List<String> debtPositionTypeOrgCodes = mock(List.class);

    List<ClassificationViewNoPII> content = podamFactory.manufacturePojo(List.class, ClassificationViewNoPII.class);
    Pageable pageable = PageRequest.of(0, 10);
    Page<ClassificationViewNoPII> ClassificationViewNoPIIPages = new PageImpl<>(content, pageable, 10);

    PagedClassificationView pagedClassificationView = podamFactory.manufacturePojo(PagedClassificationView.class);

    when(classificationViewNoPIIDTORepositoryMock.findClassificationViewNoPIIDTO(organizationId, exportClassificationsFilterDTO, debtPositionTypeOrgCodes, pageable)).thenReturn(ClassificationViewNoPIIPages);
    when(pagedClassificationViewMapperMock.map2PagedClassificationView(ClassificationViewNoPIIPages)).thenReturn(pagedClassificationView);
    //when

    PagedClassificationView result = repository.getPagedClassificationView(organizationId, exportClassificationsFilterDTO, debtPositionTypeOrgCodes, pageable);
    //then
    assertNotNull(result);
    assertEquals(pagedClassificationView, result);
  }

  @Test
  void givenTooManyElementsWhenGetPagedClassificationViewThenReturnException() {
    //given
    Long organizationId = 1L;
    ExportClassificationsFilterDTO exportClassificationsFilterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);
    List<String> debtPositionTypeOrgCodes = mock(List.class);

    Pageable pageable = PageRequest.of(0, 10);

    List<ClassificationViewNoPII> content = IntStream.range(0, 12)
      .mapToObj(i -> podamFactory.manufacturePojo(ClassificationViewNoPII.class))
      .toList();

    Page<ClassificationViewNoPII> ClassificationViewNoPIIPages = new PageImpl<>(content, pageable, 12);

    when(classificationViewNoPIIDTORepositoryMock.findClassificationViewNoPIIDTO(organizationId, exportClassificationsFilterDTO, debtPositionTypeOrgCodes, Pageable.ofSize(1)))
      .thenReturn(ClassificationViewNoPIIPages);

    //when then
    assertThrows(ExportTooManyRecordsException.class,
      () -> repository.getPagedClassificationView(organizationId, exportClassificationsFilterDTO, debtPositionTypeOrgCodes, Pageable.ofSize(1)),
      "The number of elements returned: 12 exceeds the maximum limit of 10"
    );
  }
}
