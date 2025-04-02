package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.repository.view.ClassificationViewPIIRepository;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassificationServiceTest {
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private ClassificationViewPIIRepository classificationViewPIIRepositoryMock;

  private ClassificationServiceImpl service;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    service = new ClassificationServiceImpl(debtPositionTypeOrgServiceMock, classificationViewPIIRepositoryMock);
  }

  @Test
  void whenGetPagedClassificationViewThenOk() {
    // Arrange
    Long organizationId = 1L;
    String operatorExternalUserId = "operator123";
    String debtPositionTypeOrgCodes = "code1";
    DebtPositionTypeOrg debtPositionTypeOrg = new DebtPositionTypeOrg().code(debtPositionTypeOrgCodes);

    ExportClassificationsFilterDTO filterDTO = podamFactory.manufacturePojoWithFullData(ExportClassificationsFilterDTO.class);
    PagedClassificationView pagedClassificationView = podamFactory.manufacturePojo(PagedClassificationView.class);

    when(classificationViewPIIRepositoryMock.getPagedClassificationView(
        organizationId,
        filterDTO,
        List.of(debtPositionTypeOrgCodes),
        Pageable.ofSize(1)))
      .thenReturn(pagedClassificationView);
    when(debtPositionTypeOrgServiceMock.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, null))
      .thenReturn(List.of(debtPositionTypeOrg));

    // Act
    PagedClassificationView result = service.getPagedClassificationView(organizationId, operatorExternalUserId, filterDTO, Pageable.ofSize(1));

    // Assert
    assertNotNull(result);
    assertEquals(pagedClassificationView, result);

    // Verify that the method was called with the correct parameters
    verify(classificationViewPIIRepositoryMock).getPagedClassificationView(
        organizationId,
        filterDTO,
        List.of(debtPositionTypeOrgCodes),
        Pageable.ofSize(1));
  }
}
