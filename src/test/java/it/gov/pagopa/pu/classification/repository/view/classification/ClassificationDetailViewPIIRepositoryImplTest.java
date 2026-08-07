package it.gov.pagopa.pu.classification.repository.view.classification;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.exception.common.NotFoundException;
import it.gov.pagopa.pu.classification.mapper.pii.view.ClassificationDetailViewPIIMapper;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationDetailViewNoPII;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassificationDetailViewPIIRepositoryImplTest {

  @Mock
  private ClassificationDetailViewNoPIIRepository classificationDetailViewNoPIIRepositoryMock;

  @Mock
  private ClassificationDetailViewPIIMapper classificationDetailViewPIIMapperMock;

  @InjectMocks
  private ClassificationDetailViewPIIRepositoryImpl repository;

  @BeforeEach
  void setUp() {
    repository = new ClassificationDetailViewPIIRepositoryImpl(classificationDetailViewNoPIIRepositoryMock, classificationDetailViewPIIMapperMock);
  }

  @Test
  void givenExistingClassificationWhenGetClassificationDetailViewThenReturnClassificationDetailViewDTO()  {
    Long organizationId = 1L;
    Long classificationId = 1L;
    ClassificationDetailViewNoPII mockNoPII = new ClassificationDetailViewNoPII();
    ClassificationDetailViewDTO mockDTO = new ClassificationDetailViewDTO();

    when(classificationDetailViewNoPIIRepositoryMock.findByOrganizationIdAndClassificationId(organizationId, classificationId)).thenReturn(mockNoPII);
    when(classificationDetailViewPIIMapperMock.map(mockNoPII)).thenReturn(mockDTO);

    ClassificationDetailViewDTO result = repository.getClassificationDetailView(organizationId, classificationId);

    assertNotNull(result);
    assertEquals(mockDTO, result);
    verify(classificationDetailViewNoPIIRepositoryMock).findByOrganizationIdAndClassificationId(organizationId, classificationId);
    verify(classificationDetailViewPIIMapperMock).map(mockNoPII);
  }

  @Test
  void givenNonExistingClassificationWhenGetClassificationDetailViewThenThrowNotFoundException() {
    Long organizationId = 1L;
    Long classificationId = 1L;

    when(classificationDetailViewNoPIIRepositoryMock.findByOrganizationIdAndClassificationId(organizationId, classificationId)).thenReturn(null);

    assertThrows(NotFoundException.class, () -> repository.getClassificationDetailView(organizationId, classificationId));
    verify(classificationDetailViewNoPIIRepositoryMock).findByOrganizationIdAndClassificationId(organizationId, classificationId);
    verifyNoInteractions(classificationDetailViewPIIMapperMock);
  }
}
