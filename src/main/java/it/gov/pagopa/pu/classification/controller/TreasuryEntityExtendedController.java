package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.Treasury;
import it.gov.pagopa.pu.classification.repository.TreasuryRepository;
import org.springframework.web.bind.annotation.*;

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

  @DeleteMapping("by-organizationId-billCode-billYear")
  public long deleteByOrganizationIdAndBillCodeAndBillYear(@RequestParam Long organizationId, @RequestParam String billCode, @RequestParam String billYear){
    return repository.deleteByOrganizationIdAndBillCodeAndBillYear(organizationId, billCode, billYear);
  }
}
