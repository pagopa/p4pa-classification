package it.gov.pagopa.pu.classification.connector.workflowhub.service;

import it.gov.pagopa.pu.classification.connector.workflowhub.client.ClassificationWorkflowClient;
import it.gov.pagopa.pu.workflow.dto.generated.WorkflowCreatedDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClassificationWorkflowServiceImplTest {

  @Mock
  private ClassificationWorkflowClient classificationWorkflowClientMock;

  private ClassificationWorkflowService classificationWorkflowService;

  @BeforeEach
  void init(){
    classificationWorkflowService = new ClassificationWorkflowServiceImpl(
      classificationWorkflowClientMock
    );
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(
      classificationWorkflowClientMock
    );
  }

  @Test
  void whenClassifyAssessmentsThenInvokeClient(){
    // Given
    String accessToken = "ACCESS_TOKEN";
    Long organizationId = 1L;
    String iuv = "IUV";
    String iud = "IUD";
    WorkflowCreatedDTO expectedResult = new WorkflowCreatedDTO();
    Mockito.when(classificationWorkflowClientMock.classifyAssessments(organizationId, iuv, iud, accessToken))
      .thenReturn(expectedResult);

    // When
    WorkflowCreatedDTO actualResult = classificationWorkflowService.classifyAssessments(organizationId, iuv, iud, accessToken);

    // Then
    Assertions.assertEquals(expectedResult, actualResult);
  }

}
