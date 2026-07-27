package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.model.Classification;
import it.gov.pagopa.pu.classification.repository.ClassificationRepository;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ClassificationEntityExtendedControllerTest {

  @Mock
  private ClassificationRepository repositoryMock;
  @Mock
  private ClassificationService serviceMock;

  @InjectMocks
  private ClassificationEntityExtendedController controller;

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(repositoryMock, serviceMock);
  }

  @Test
  void whenSaveAllThenInvokeRepository(){
    // Given
    List<Classification> entities = List.of(new Classification());
    Mockito.when(serviceMock.saveAll(entities))
      .thenReturn(entities);

    // When
    Integer result = controller.saveAll2(entities).getBody();

    // Then
    Assertions.assertEquals(entities.size(), result);
  }

  @Test
  void whenDeleteByOrganizationIdAndIufAndLabelThenInvokeRepository(){
    // Given
    Long organizationId = 0L;
    String iuf = "IUF";
    ClassificationsEnum label = ClassificationsEnum.RT_NO_IUF;
    int expectedResult = 1;

    Mockito.when(repositoryMock.deleteByOrganizationIdAndIufAndLabel(Mockito.same(organizationId), Mockito.same(iuf), Mockito.same(label)))
      .thenReturn(expectedResult);

    // When
    Integer result = controller.deleteByOrganizationIdAndIufAndLabel(organizationId, iuf, label).getBody();

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
    int expectedResult = 1;

    Mockito.when(repositoryMock.deleteByOrganizationIdAndIuvAndIurAndTransferIndex(Mockito.same(organizationId), Mockito.same(iuv), Mockito.same(iur), Mockito.same(transferIndex)))
      .thenReturn(expectedResult);

    // When
    Integer result = controller.deleteByOrganizationIdAndIuvAndIurAndTransferIndex(organizationId, iuv, iur, transferIndex).getBody();

    // Then
    Assertions.assertEquals(expectedResult, result);
  }


  @Test
  void whenDeleteByOrganizationIdAndIuvAndIurAndTransferIndexAndLabelNotThenInvokeRepository(){
    // Given
    Long organizationId = 0L;
    String iuv = "IUV";
    String iur = "IUR";
    Integer transferIndex = 1;
    ClassificationsEnum label = ClassificationsEnum.DOPPI;
    int expectedResult = 1;

    Mockito.when(repositoryMock.deleteByOrganizationIdAndIuvAndIurAndTransferIndexAndLabelNot(Mockito.same(organizationId), Mockito.same(iuv), Mockito.same(iur), Mockito.same(transferIndex), Mockito.same(label)))
      .thenReturn(expectedResult);

    // When
    Integer result = controller.deleteByOrganizationIdAndIuvAndIurAndTransferIndexAndLabelNot(organizationId, iuv, iur, transferIndex, label).getBody();

    // Then
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenDeleteByOrganizationIdAndTreasuryIdThenInvokeRepository(){
    // Given
    Long organizationId = 0L;
    String treasuryId = "TREASURYID";
    int expectedResult = 1;

    Mockito.when(repositoryMock.deleteByOrganizationIdAndTreasuryId(Mockito.same(organizationId), Mockito.same(treasuryId)))
      .thenReturn(expectedResult);

    // When
    Integer result = controller.deleteByOrganizationIdAndTreasuryId(organizationId, treasuryId).getBody();

    // Then
    Assertions.assertEquals(expectedResult, result);
  }


  @Test
  void whenDeleteDuplicatesThenInvokeRepository() {
    // Given
    Long organizationId = 0L;
    String iuv = "IUV";
    int transferIndex = 1;
    ClassificationsEnum label = ClassificationsEnum.DOPPI;
    int expectedResult = 1;

    Mockito.when(repositoryMock.deleteDuplicates(Mockito.same(organizationId),
        Mockito.same(iuv), Mockito.same(transferIndex),
        Mockito.same(label)))
      .thenReturn(expectedResult);

    // When
    Integer result = controller.deleteDuplicates(organizationId, iuv,
        transferIndex, label)
      .getBody();

    // Then
    Assertions.assertEquals(expectedResult, result);
  }

  @Test
  void whenDeleteByOrganizationIdAndIudAndLabelThenInvokeRepository(){
    // Given
    Long organizationId = 0L;
    String iud = "IUD";
    ClassificationsEnum label = ClassificationsEnum.IUD_NO_RT;
    int expectedResult = 1;

    Mockito.when(repositoryMock.deleteByOrganizationIdAndIudAndLabel(Mockito.same(organizationId), Mockito.same(iud), Mockito.same(label)))
      .thenReturn(expectedResult);

    // When
    Integer result = controller.deleteByOrganizationIdAndIudAndLabel(organizationId, iud, label).getBody();

    // Then
    Assertions.assertEquals(expectedResult, result);
  }
}
