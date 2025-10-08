package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.ClassificationEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.model.Classification;
import it.gov.pagopa.pu.classification.repository.ClassificationRepository;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Controller to host spring-data-rest directly not supported methods */
@RestController
public class ClassificationEntityExtendedController implements ClassificationEntityExtendedControllerApi {

  private final ClassificationRepository repository;
  private final ClassificationService service;

  public ClassificationEntityExtendedController(ClassificationRepository repository,
    ClassificationService service) {
    this.repository = repository;
    this.service = service;
  }

  @Override
  public ResponseEntity<Integer> saveAll2(List<Classification> classifications){
    return ResponseEntity.ok(service.saveAll(classifications).size());
  }

  @Override
  public ResponseEntity<Integer> deleteByOrganizationIdAndIufAndLabel(Long organizationId, String iuf, ClassificationsEnum  label){
    return ResponseEntity.ok(repository.deleteByOrganizationIdAndIufAndLabel(organizationId, iuf, label));
  }

  @Override
  public ResponseEntity<Integer> deleteByOrganizationIdAndIuvAndIurAndTransferIndex(Long organizationId, String iuv, String iur, Integer transferIndex){
    return ResponseEntity.ok(repository.deleteByOrganizationIdAndIuvAndIurAndTransferIndex(organizationId, iuv, iur, transferIndex));
  }

  @Override
  public ResponseEntity<Integer> deleteByOrganizationIdAndTreasuryId(Long organizationId, String treasuryId){
    return ResponseEntity.ok(repository.deleteByOrganizationIdAndTreasuryId(organizationId, treasuryId));
  }

  @Override
  public ResponseEntity<Integer> deleteByOrganizationIdAndIudAndLabel(Long organizationId, String iud, ClassificationsEnum  label){
    return ResponseEntity.ok(repository.deleteByOrganizationIdAndIudAndLabel(organizationId, iud, label));
  }
}
