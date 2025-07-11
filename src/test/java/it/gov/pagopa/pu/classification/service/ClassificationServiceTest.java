package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.TreasuredClassificationFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.mapper.TreasuredClassificationMapper;
import it.gov.pagopa.pu.classification.model.view.TreasuredClassificationView;
import it.gov.pagopa.pu.classification.repository.view.ClassificationDetailViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.ClassificationViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.FullClassificationViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.TreasuredClassificationViewRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassificationServiceTest {

  @Mock
  private ClassificationViewPIIRepository classificationViewPIIRepositoryMock;
  @Mock
  private FullClassificationViewPIIRepository fullClassificationViewPIIRepositoryMock;
  @Mock
  private TreasuredClassificationViewRepository treasuredClassificationViewRepositoryMock;
  @Mock
  private TreasuredClassificationMapper treasuredClassificationMapperMock;
  @Mock
  private ClassificationDetailViewPIIRepository classificationDetailViewPIIRepositoryMock;

  private ClassificationService service;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    service = new ClassificationServiceImpl(
      classificationViewPIIRepositoryMock,
      fullClassificationViewPIIRepositoryMock,
      treasuredClassificationViewRepositoryMock,
      treasuredClassificationMapperMock,
      classificationDetailViewPIIRepositoryMock);
  }

  @Test
  void whenGetPagedClassificationViewThenOk() {
    // Arrange
    Long organizationId = 1L;
    String operatorExternalUserId = "operator123";

    Pageable pageable = PageRequest.of(0, 10);
    ExportClassificationsFilterDTO filterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);
    PagedClassificationView pagedClassificationView = podamFactory.manufacturePojo(PagedClassificationView.class);

    when(classificationViewPIIRepositoryMock.getPagedClassificationView(
      organizationId,
      filterDTO,
      pageable))
      .thenReturn(pagedClassificationView);

    // Act
    PagedClassificationView result = service.getPagedClassificationView(organizationId, operatorExternalUserId, filterDTO, pageable, null);

    // Assert
    assertNotNull(result);
    assertEquals(pagedClassificationView, result);

    // Verify that the method was called with the correct parameters
    verify(classificationViewPIIRepositoryMock, times(1)).getPagedClassificationView(
      organizationId,
      filterDTO,
      pageable);
  }

  @Test
  void whenGetPagedFullClassificationViewThenOk() {
    // Arrange
    Long organizationId = 1L;
    String operatorExternalUserId = "operator123";

    Pageable pageable = PageRequest.of(0, 10);
    ExportClassificationsFilterDTO filterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);
    PagedFullClassificationView pagedFullClassificationView = podamFactory.manufacturePojo(PagedFullClassificationView.class);

    when(fullClassificationViewPIIRepositoryMock.getPagedFullClassificationView(
      organizationId,
      filterDTO,
      pageable))
      .thenReturn(pagedFullClassificationView);

    // Act
    PagedFullClassificationView result = service.getPagedFullClassificationView(organizationId, operatorExternalUserId, filterDTO, pageable, null);

    // Assert
    assertNotNull(result);
    assertEquals(pagedFullClassificationView, result);

    // Verify that the method was called with the correct parameters
    verify(fullClassificationViewPIIRepositoryMock, times(1)).getPagedFullClassificationView(
      organizationId,
      filterDTO,
      pageable);
  }


  @Test
  void whenGetPagedTreasuredClassificationThenOk() {
    // given
    Long organizationId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    TreasuredClassificationFilterDTO filterDTO = podamFactory.manufacturePojo(
      TreasuredClassificationFilterDTO.class);
    Page<TreasuredClassificationView> pagedTreasuredClassifications = new PageImpl<>(List.of(podamFactory.manufacturePojo(
      TreasuredClassificationView.class)));
    PagedTreasuredClassification expectedResult = podamFactory.manufacturePojo(PagedTreasuredClassification.class);

    when(treasuredClassificationViewRepositoryMock.getTreasuredClassifications(
      organizationId,
      filterDTO,
      pageable))
      .thenReturn(pagedTreasuredClassifications);
    when(treasuredClassificationMapperMock.map2PagedTreasuredClassification(pagedTreasuredClassifications))
      .thenReturn(expectedResult);

    // when
    PagedTreasuredClassification result = service.getPagedTreasuredClassification(organizationId, filterDTO, pageable);

    // then
    assertNotNull(result);
    assertEquals(expectedResult, result);

    verify(treasuredClassificationViewRepositoryMock, times(1)).getTreasuredClassifications(
      organizationId,
      filterDTO,
      pageable);
  }

  @Test
  void whenGetClassificationDetailViewThenOk() {
    Long organizationId = 1L;
    Long classificationId = 1L;
    ClassificationDetailViewDTO classificationDetailViewDTO = podamFactory.manufacturePojo(ClassificationDetailViewDTO.class);

    when(classificationDetailViewPIIRepositoryMock.getClassificationDetailView(organizationId, classificationId))
      .thenReturn(classificationDetailViewDTO);

    ClassificationDetailViewDTO result = service.getClassificationDetailView(organizationId, classificationId);

    assertNotNull(result);
    assertEquals(classificationDetailViewDTO, result);

    verify(classificationDetailViewPIIRepositoryMock, times(1)).getClassificationDetailView(organizationId, classificationId);
  }
}
