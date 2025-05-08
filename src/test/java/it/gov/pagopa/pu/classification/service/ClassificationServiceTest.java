package it.gov.pagopa.pu.classification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.TreasuredClassificationFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.mapper.TreasuredClassificationMapper;
import it.gov.pagopa.pu.classification.model.view.TreasuredClassification;
import it.gov.pagopa.pu.classification.repository.view.ClassificationDetailViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.ClassificationViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.FullClassificationViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.TreasuredClassificationRepository;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
class ClassificationServiceTest {
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private ClassificationViewPIIRepository classificationViewPIIRepositoryMock;
  @Mock
  private FullClassificationViewPIIRepository fullClassificationViewPIIRepositoryMock;
  @Mock
  private TreasuredClassificationRepository treasuredClassificationRepositoryMock;
  @Mock
  private TreasuredClassificationMapper treasuredClassificationMapperMock;
  @Mock
  private ClassificationDetailViewPIIRepository classificationDetailViewPIIRepositoryMock;

  private ClassificationService service;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    service = new ClassificationServiceImpl(debtPositionTypeOrgServiceMock,
      classificationViewPIIRepositoryMock,
      fullClassificationViewPIIRepositoryMock,
      treasuredClassificationRepositoryMock,
      treasuredClassificationMapperMock,
      classificationDetailViewPIIRepositoryMock);
  }

  @Test
  void whenGetPagedClassificationViewThenOk() {
    // Arrange
    Long organizationId = 1L;
    String operatorExternalUserId = "operator123";
    List<DebtPositionTypeOrg> debtPositionTypeOrgs = podamFactory.manufacturePojo(List.class, DebtPositionTypeOrg.class);
    List<String> debtPositionTypeOrgCodes = debtPositionTypeOrgs.stream()
      .map(DebtPositionTypeOrg::getCode)
      .toList();
    Pageable pageable = PageRequest.of(0, 10);
    ExportClassificationsFilterDTO filterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);
    PagedClassificationView pagedClassificationView = podamFactory.manufacturePojo(PagedClassificationView.class);

    when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, null))
      .thenReturn(debtPositionTypeOrgs);
    when(classificationViewPIIRepositoryMock.getPagedClassificationView(
      organizationId,
      filterDTO,
      debtPositionTypeOrgCodes,
      pageable))
      .thenReturn(pagedClassificationView);

    // Act
    PagedClassificationView result = service.getPagedClassificationView(organizationId, operatorExternalUserId, filterDTO, pageable, null);

    // Assert
    assertNotNull(result);
    assertEquals(pagedClassificationView, result);

    // Verify that the method was called with the correct parameters
    verify(debtPositionTypeOrgServiceMock, times(1)).findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, null);
    verify(classificationViewPIIRepositoryMock, times(1)).getPagedClassificationView(
      organizationId,
      filterDTO,
      debtPositionTypeOrgCodes,
      pageable);
  }

  @Test
  void whenGetPagedFullClassificationViewThenOk() {
    // Arrange
    Long organizationId = 1L;
    String operatorExternalUserId = "operator123";
    List<DebtPositionTypeOrg> debtPositionTypeOrgs = podamFactory.manufacturePojo(List.class, DebtPositionTypeOrg.class);
    List<String> debtPositionTypeOrgCodes = debtPositionTypeOrgs.stream()
      .map(DebtPositionTypeOrg::getCode)
      .toList();
    Pageable pageable = PageRequest.of(0, 10);
    ExportClassificationsFilterDTO filterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);
    PagedFullClassificationView pagedFullClassificationView = podamFactory.manufacturePojo(PagedFullClassificationView.class);

    when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, null))
      .thenReturn(debtPositionTypeOrgs);
    when(fullClassificationViewPIIRepositoryMock.getPagedFullClassificationView(
      organizationId,
      filterDTO,
      debtPositionTypeOrgCodes,
      pageable))
      .thenReturn(pagedFullClassificationView);

    // Act
    PagedFullClassificationView result = service.getPagedFullClassificationView(organizationId, operatorExternalUserId, filterDTO, pageable, null);

    // Assert
    assertNotNull(result);
    assertEquals(pagedFullClassificationView, result);

    // Verify that the method was called with the correct parameters
    verify(debtPositionTypeOrgServiceMock, times(1)).findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, null);
    verify(fullClassificationViewPIIRepositoryMock, times(1)).getPagedFullClassificationView(
      organizationId,
      filterDTO,
      debtPositionTypeOrgCodes,
      pageable);
  }


  @Test
  void whenGetPagedTreasuredClassificationThenOk() {
    // given
    Long organizationId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    TreasuredClassificationFilterDTO filterDTO = podamFactory.manufacturePojo(
      TreasuredClassificationFilterDTO.class);
    Page<TreasuredClassification> pagedTreasuredClassifications = new PageImpl<>(List.of(podamFactory.manufacturePojo(TreasuredClassification.class)));
    PagedTreasuredClassification expectedResult = podamFactory.manufacturePojo(PagedTreasuredClassification.class);

    when(treasuredClassificationRepositoryMock.getTreasuredClassifications(
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

    verify(treasuredClassificationRepositoryMock, times(1)).getTreasuredClassifications(
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
