package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.Classification;
import it.gov.pagopa.pu.classification.repository.ClassificationRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
