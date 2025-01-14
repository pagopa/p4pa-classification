package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.Classification;
import it.gov.pagopa.pu.classification.repository.ClassificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller to host spring-data-rest directly not supported methods */
@RestController
@RequestMapping("/crud-ext/classifications")
public class ClassificationEntityExtendedController {

  private final ClassificationRepository repository;

  public ClassificationEntityExtendedController(ClassificationRepository repository) {
    this.repository = repository;
  }

  @PostMapping
  public int saveAll(@RequestBody List<Classification> classifications){
    return repository.saveAll(classifications).size();
  }

  @DeleteMapping("by-organizationId-iuf-label")
  public long deleteByOrganizationIdAndIufAndLabel(@RequestParam Long organizationId,@RequestParam String iuf,@RequestParam String label){
    return repository.deleteByOrganizationIdAndIufAndLabel(organizationId, iuf, label);
  }

  @DeleteMapping("by-organizationId-iuv-iur-transferIndex")
  public long deleteByOrganizationIdAndIuvAndIurAndTransferIndex(@RequestParam Long organizationId,@RequestParam String iuv,@RequestParam String iur, @RequestParam Integer transferIndex){
    return repository.deleteByOrganizationIdAndIuvAndIurAndTransferIndex(organizationId, iuv, iur, transferIndex);
  }

}
