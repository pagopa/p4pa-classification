package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.ClassificationsApi;
import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassificationController implements ClassificationsApi {

  private final ClassificationService classificationService;

  public ClassificationController(ClassificationService classificationService) {
    this.classificationService = classificationService;
  }

  @Override
  public ResponseEntity<ClassificationDetailViewDTO> getClassificationDetail(Long organizationId, Long classificationId) {
    return ResponseEntity.ok(classificationService.getClassificationDetailView(organizationId, classificationId));
  }
}
