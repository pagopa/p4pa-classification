package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import it.gov.pagopa.pu.classification.repository.PaymentsReportingRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Controller to host spring-data-rest directly not supported methods */
@RestController
@RequestMapping("/crud-ext/payments-reporting")
public class PaymentsReportingEntityExtendedController {

  private final PaymentsReportingRepository repository;

  public PaymentsReportingEntityExtendedController(PaymentsReportingRepository repository) {
    this.repository = repository;
  }

  @PostMapping
  public int saveAll(@RequestBody List<PaymentsReporting> paymentsReportings){
    return repository.saveAll(paymentsReportings).size();
  }
}
