package it.gov.pagopa.pu.classification.connector.workflowhub.service;

import it.gov.pagopa.pu.classification.connector.workflowhub.client.ClassificationWorkflowClient;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import org.springframework.stereotype.Service;

@Service
public class ClassificationWorkflowServiceImpl implements ClassificationWorkflowService {

  private final ClassificationWorkflowClient classificationWorkflowClient;

  public ClassificationWorkflowServiceImpl(ClassificationWorkflowClient classificationWorkflowClient) {
    this.classificationWorkflowClient = classificationWorkflowClient;
  }

  @Override
  public WorkflowCreatedDTO classifyAssessments(Long organizationId, String iuv, String iud, String accessToken) {
    return classificationWorkflowClient.classifyAssessments(organizationId, iuv, iud, accessToken);
  }
}
