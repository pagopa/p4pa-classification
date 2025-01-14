package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.Treasury;
import it.gov.pagopa.pu.classification.repository.TreasuryRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Controller to host spring-data-rest directly not supported methods */
@RestController
@RequestMapping("/crud-ext/treasury")
public class TreasuryEntityExtendedController {

  private final TreasuryRepository repository;

  public TreasuryEntityExtendedController(TreasuryRepository repository) {
    this.repository = repository;
  }

  @PostMapping
  public int saveAll(@RequestBody List<Treasury> treasuries){
    return repository.saveAll(treasuries).size();
  }
}
