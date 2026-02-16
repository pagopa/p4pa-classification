package it.gov.pagopa.pu.classification.repository.view;

import it.gov.pagopa.pu.classification.dto.filters.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.exception.custom.ExportTooManyRecordsException;
import it.gov.pagopa.pu.classification.mapper.pages.PagedFullClassificationViewMapper;
import it.gov.pagopa.pu.classification.model.view.classification.FullClassificationViewNoPII;
import it.gov.pagopa.pu.classification.repository.view.classification.FullClassificationViewNoPIIDTORepository;
import it.gov.pagopa.pu.classification.repository.view.classification.FullClassificationViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.classification.FullClassificationViewPIIRepositoryImpl;
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
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@TestPropertySource(properties = {"data-export.classification-view.max-total-elements=10"})
class FullClassificationViewPIIRepositoryTest {

  @Value("${data-export.classification-view.max-total-elements}")
  private Integer maxElements;

  @Mock
  private FullClassificationViewNoPIIDTORepository fullClassificationViewNoPIIDTORepositoryMock;

  @Mock
  private PagedFullClassificationViewMapper pagedFullClassificationViewMapperMock;

  FullClassificationViewPIIRepository repository;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    repository = new FullClassificationViewPIIRepositoryImpl(maxElements, fullClassificationViewNoPIIDTORepositoryMock, pagedFullClassificationViewMapperMock);
  }

  @Test
  void givenValidParamWhenGetPagedFullClassificationViewThenReturnPagedClassificationView() {
    //given
    Long organizationId = 1L;
    ExportClassificationsFilterDTO exportClassificationsFilterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);

    List<FullClassificationViewNoPII> content = podamFactory.manufacturePojo(List.class, FullClassificationViewNoPII.class);
    Pageable pageable = PageRequest.of(0, 10);
    Page<FullClassificationViewNoPII> noPiiPage = new PageImpl<>(content, pageable, 10);

    PagedFullClassificationView pagedFullClassificationView = podamFactory.manufacturePojo(PagedFullClassificationView.class);

    when(fullClassificationViewNoPIIDTORepositoryMock.findFullClassificationViewNoPIIDTO(organizationId, exportClassificationsFilterDTO,  pageable)).thenReturn(noPiiPage);
    when(pagedFullClassificationViewMapperMock.map2PagedClassificationView(noPiiPage)).thenReturn(pagedFullClassificationView);
    //when

    PagedFullClassificationView result = repository.getPagedFullClassificationView(organizationId, exportClassificationsFilterDTO,  pageable);
    //then
    assertNotNull(result);
    assertEquals(pagedFullClassificationView, result);
  }

  @Test
  void givenTooManyElementsWhenGetPagedClassificationViewThenReturnException() {
    //given
    Long organizationId = 1L;
    ExportClassificationsFilterDTO exportClassificationsFilterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);

    Pageable pageable = PageRequest.of(0, 10);

    List<FullClassificationViewNoPII> content = IntStream.range(0, 12)
      .mapToObj(i -> podamFactory.manufacturePojo(FullClassificationViewNoPII.class))
      .toList();

    Page<FullClassificationViewNoPII> noPiiPage = new PageImpl<>(content, pageable, 12);
    Pageable pageable1 = Pageable.ofSize(1);
    when(fullClassificationViewNoPIIDTORepositoryMock.findFullClassificationViewNoPIIDTO(organizationId, exportClassificationsFilterDTO, pageable1))
      .thenReturn(noPiiPage);

    //when then
    assertThrows(ExportTooManyRecordsException.class,
      () -> repository.getPagedFullClassificationView(organizationId, exportClassificationsFilterDTO, pageable1),
      "The number of elements returned: 12 exceeds the maximum limit of 10"
    );
  }
}
