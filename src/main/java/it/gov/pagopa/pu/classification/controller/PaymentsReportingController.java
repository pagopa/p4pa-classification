package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.PaymentsReportingApi;
import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import it.gov.pagopa.pu.classification.service.PaymentsReportingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class PaymentsReportingController implements PaymentsReportingApi {

  private final PaymentsReportingService paymentsReportingService;

  public PaymentsReportingController(PaymentsReportingService paymentsReportingService) {
    this.paymentsReportingService = paymentsReportingService;
  }

  @Override
  public ResponseEntity<List<PaymentsReporting>> findAndDeleteByOrgIdAndIufAndIngestionFlowFileIdNot(Long organizationId, String iuf, Long ingestionFlowFileId){
    return new ResponseEntity<>(paymentsReportingService.findAndDeleteByOrgIdAndIufAndIngestionFlowFileIdNot(organizationId, iuf, ingestionFlowFileId), HttpStatus.OK);
  }
}
