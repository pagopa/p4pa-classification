package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.PaymentsReportingEntityExtendedControllerApi;
import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import it.gov.pagopa.pu.classification.repository.PaymentsReportingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Controller to host spring-data-rest directly not supported methods */
@RestController
public class PaymentsReportingEntityExtendedController implements PaymentsReportingEntityExtendedControllerApi {

  private final PaymentsReportingRepository repository;

  public PaymentsReportingEntityExtendedController(PaymentsReportingRepository repository) {
    this.repository = repository;
  }

  @Override
  public ResponseEntity<Integer> saveAll1(List<PaymentsReporting> paymentsReportings){
    return ResponseEntity.ok(repository.saveAll(paymentsReportings).size());
  }
}
