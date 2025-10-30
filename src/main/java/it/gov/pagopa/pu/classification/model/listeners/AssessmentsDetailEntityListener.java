package it.gov.pagopa.pu.classification.model.listeners;

import it.gov.pagopa.pu.classification.connector.workflowhub.service.ClassificationWorkflowService;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class AssessmentsDetailEntityListener {

  private final ClassificationWorkflowService classificationWorkflowService;

  public AssessmentsDetailEntityListener(@Lazy ClassificationWorkflowService classificationWorkflowService) {
    this.classificationWorkflowService = classificationWorkflowService;
  }

  @PostPersist
  public void onPostPersist(AssessmentsDetail entity) {
    onSave(entity);
  }

  @PostUpdate
  public void onPostUpdate(AssessmentsDetail entity) {
    onSave(entity);
  }

  private void onSave(AssessmentsDetail entity) {
    if(entity.getClassificationLabel() != null)
      return;
    classificationWorkflowService.classifyAssessments(
      entity.getOrganizationId(),
      entity.getIuv(),
      entity.getIud(),
      SecurityUtils.getAccessToken()
    );
  }
}
