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
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

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

  //region findByOrgFiscalCode test
  @Test
  void whenClassifyAssessmentsThenInvokeWithAccessToken() {
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

  @Test
  void givenNotExistentOrgFiscalCodeWhenGetOrgFiscalCodeThenNull() {
    // Given
    String accessToken = "ACCESS_TOKEN";
    Long organizationId = 1L;
    String iuv = "IUV";
    String iud = "IUD";

    Mockito.when(workflowHubApisHolderMock.getClassificationApi(accessToken))
      .thenReturn(classificationApiMock);
    Mockito.when(classificationApiMock.assessmentsClassification(organizationId, iuv, iud))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    // When
    WorkflowCreatedDTO result = classificationWorkflowClient.classifyAssessments(organizationId, iuv, iud, accessToken);

    // Then
    Assertions.assertNull(result);
  }
  //endregion
}
