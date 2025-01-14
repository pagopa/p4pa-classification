package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.Classification;
import it.gov.pagopa.pu.classification.repository.ClassificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ClassificationEntityExtendedControllerTest {

  @Mock
  private ClassificationRepository repositoryMock;

  private ClassificationEntityExtendedController controller;

  @BeforeEach
  void init(){
    controller = new ClassificationEntityExtendedController(repositoryMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(repositoryMock);
  }

  @Test
  void whenSaveAllThenInvokeRepository(){
    // Given
    List<Classification> entities = List.of(new Classification());
    Mockito.when(repositoryMock.saveAll(entities))
      .thenReturn(entities);

    // When
    int result = controller.saveAll(entities);

    // Then
    Assertions.assertEquals(entities.size(), result);
  }

  @Test
  void whenDeleteByOrganizationIdAndIufAndLabelThenInvokeRepository(){
    // Given
    Long organizationId = 0L;
    String iuf = "IUF";
    String label = "LABEL";
    long expectedResult = 1L;

    Mockito.when(repositoryMock.deleteByOrganizationIdAndIufAndLabel(Mockito.same(organizationId), Mockito.same(iuf), Mockito.same(label)))
      .thenReturn(expectedResult);

    // When
    long result = controller.deleteByOrganizationIdAndIufAndLabel(organizationId, iuf, label);

    // Then
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenDeleteByOrganizationIdAndIuvAndIurAndTransferIndexThenInvokeRepository(){
    // Given
    Long organizationId = 0L;
    String iuv = "IUV";
    String iur = "IUR";
    Integer transferIndex = 1;
    long expectedResult = 1L;

    Mockito.when(repositoryMock.deleteByOrganizationIdAndIuvAndIurAndTransferIndex(Mockito.same(organizationId), Mockito.same(iuv), Mockito.same(iur), Mockito.same(transferIndex)))
      .thenReturn(expectedResult);

    // When
    long result = controller.deleteByOrganizationIdAndIuvAndIurAndTransferIndex(organizationId, iuv, iur, transferIndex);

    // Then
    Assertions.assertEquals(expectedResult, result);
  }
}
