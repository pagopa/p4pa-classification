package it.gov.pagopa.pu.classification.connector.workflowhub.service;

import it.gov.pagopa.pu.workflow.dto.generated.WorkflowCreatedDTO;

public interface ClassificationWorkflowService {

  WorkflowCreatedDTO classifyAssessments(Long organizationId, String iuv, String iud, String accessToken);

}
