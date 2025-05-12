package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.TreasuryEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.model.Treasury;
import it.gov.pagopa.pu.classification.repository.TreasuryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Controller to host spring-data-rest directly not supported methods */
@RestController
public class TreasuryEntityExtendedController implements TreasuryEntityExtendedControllerApi {

  private final TreasuryRepository repository;

  public TreasuryEntityExtendedController(TreasuryRepository repository) {
    this.repository = repository;
  }

  @Override
  public ResponseEntity<Integer> saveAll(List<Treasury> treasuries){
    return ResponseEntity.ok(repository.saveAll(treasuries).size());
  }

  @Override
  public ResponseEntity<Long> deleteByOrganizationIdAndBillCodeAndBillYear(Long organizationId, String billCode, String billYear){
    return ResponseEntity.ok(repository.deleteByOrganizationIdAndBillCodeAndBillYear(organizationId, billCode, billYear));
  }
}
