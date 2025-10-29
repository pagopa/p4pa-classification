package it.gov.pagopa.pu.classification.connector.workflowhub.client;

import it.gov.pagopa.pu.classification.connector.workflowhub.config.WorkflowHubApisHolder;
import it.gov.pagopa.pu.workflow.client.generated.ClassificationApi;
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
class ClassificationWorkflowClientTest {

  @Mock
  private WorkflowHubApisHolder workflowHubApisHolderMock;
  @Mock
  private ClassificationApi classificationApiMock;

  private ClassificationWorkflowClient classificationWorkflowClient;

  @BeforeEach
  void setUp() {
    classificationWorkflowClient = new ClassificationWorkflowClient(workflowHubApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      workflowHubApisHolderMock,
      classificationApiMock
    );
  }

  @Test
  void testClassifyAssessments() {
    // Given
    String accessToken = "ACCESS_TOKEN";
    Long organizationId = 1L;
    String iuv = "IUV";
    String iud = "IUD";
    WorkflowCreatedDTO expectedResult = new WorkflowCreatedDTO();

    Mockito.when(workflowHubApisHolderMock.getClassificationApi(accessToken))
      .thenReturn(classificationApiMock);
    Mockito.when(classificationApiMock.assessmentsClassification(organizationId, iuv, iud))
      .thenReturn(expectedResult);

    // When
    WorkflowCreatedDTO result = classificationWorkflowClient.classifyAssessments(organizationId, iuv, iud, accessToken);

    // Then
    Assertions.assertSame(expectedResult, result);
  }

}
