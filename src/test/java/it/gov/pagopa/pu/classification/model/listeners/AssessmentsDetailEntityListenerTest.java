package it.gov.pagopa.pu.classification.model.listeners;

import it.gov.pagopa.pu.classification.connector.workflowhub.service.ClassificationWorkflowService;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssessmentsDetailEntityListenerTest {

  @Mock
  private ClassificationWorkflowService classificationWorkflowServiceMock;

  private AssessmentsDetailEntityListener listener;

  private static final AssessmentsDetail assessmentsDetail = new AssessmentsDetail();

  private static final String ACCESS_TOKEN = "ACCESS_TOKEN";

  @BeforeAll
  static void beforeAll() {
    assessmentsDetail.setOrganizationId(3L);
    assessmentsDetail.setIuv("IUV");
    assessmentsDetail.setIud("IUD");
  }

  @BeforeEach
  void setUp() {
    listener = new AssessmentsDetailEntityListener(classificationWorkflowServiceMock);
    SecurityUtilsTest.configureSecurityContext(ACCESS_TOKEN, null);
    WorkflowCreatedDTO expectedResult = new WorkflowCreatedDTO();
    Mockito.when(
      classificationWorkflowServiceMock.classifyAssessments(
        assessmentsDetail.getOrganizationId(),
        assessmentsDetail.getIuv(),
        assessmentsDetail.getIud(),
        ACCESS_TOKEN
      )
    ).thenReturn(expectedResult);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      classificationWorkflowServiceMock
    );
  }

  @AfterEach
  void clear(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void onPostPersist() {
    //Given
    //When
    listener.onPostPersist(assessmentsDetail);
    //Then
    Mockito.verify(classificationWorkflowServiceMock).classifyAssessments(
      assessmentsDetail.getOrganizationId(),
      assessmentsDetail.getIuv(),
      assessmentsDetail.getIud(),
      ACCESS_TOKEN
    );
  }

  @Test
  void onPostUpdate() {
    //Given
    //When
    listener.onPostUpdate(assessmentsDetail);
    //Then
    Mockito.verify(classificationWorkflowServiceMock).classifyAssessments(
      assessmentsDetail.getOrganizationId(),
      assessmentsDetail.getIuv(),
      assessmentsDetail.getIud(),
      ACCESS_TOKEN
    );
  }
}
