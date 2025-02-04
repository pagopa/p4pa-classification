package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.ClassificationEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.model.Classification;
import it.gov.pagopa.pu.classification.repository.ClassificationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Controller to host spring-data-rest directly not supported methods */
@RestController
public class ClassificationEntityExtendedController implements ClassificationEntityExtendedControllerApi {

  private final ClassificationRepository repository;

  public ClassificationEntityExtendedController(ClassificationRepository repository) {
    this.repository = repository;
  }

  @Override
  public ResponseEntity<Integer> saveAll2(List<Classification> classifications){
    return ResponseEntity.ok(repository.saveAll(classifications).size());
  }

  @Override
  public ResponseEntity<Long> deleteByOrganizationIdAndIufAndLabel(Long organizationId, String iuf, String label){
    return ResponseEntity.ok(repository.deleteByOrganizationIdAndIufAndLabel(organizationId, iuf, label));
  }

  @Override
  public ResponseEntity<Long> deleteByOrganizationIdAndIuvAndIurAndTransferIndex(Long organizationId, String iuv, String iur, Integer transferIndex){
    return ResponseEntity.ok(repository.deleteByOrganizationIdAndIuvAndIurAndTransferIndex(organizationId, iuv, iur, transferIndex));
  }

}
