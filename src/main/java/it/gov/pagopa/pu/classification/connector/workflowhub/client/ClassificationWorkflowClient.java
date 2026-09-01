package it.gov.pagopa.pu.classification.connector.workflowhub.client;

import it.gov.pagopa.pu.classification.connector.workflowhub.config.WorkflowHubApisHolder;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowCreatedDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClassificationWorkflowClient {

  private final WorkflowHubApisHolder workflowHubApisHolder;

  public ClassificationWorkflowClient(WorkflowHubApisHolder workflowHubApisHolder) {
    this.workflowHubApisHolder = workflowHubApisHolder;
  }

  public WorkflowCreatedDTO classifyAssessments(Long organizationId, String iuv, String iud, String accessToken) {
    return workflowHubApisHolder.getClassificationApi(accessToken)
      .assessmentsClassification(organizationId, iuv, iud);
  }

}
