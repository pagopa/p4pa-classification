package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import it.gov.pagopa.pu.classification.service.PaymentsReportingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingControllerTest {

  @Mock
  private PaymentsReportingService serviceMock;

  private PaymentsReportingController controller;

  @BeforeEach
  void init() {
    controller =  new PaymentsReportingController(serviceMock);
  }

  @Test
  void givenFiltersWhenFindAndDeletePaymentsReportingThenReturnListUpdated(){
    Long orgId = 1L;
    String iuf = "IUF";
    Long ingestionFlowFileId = 2L;

    List<PaymentsReporting> paymentsReportingList = List.of(new PaymentsReporting());

    Mockito.when(serviceMock.findAndDeleteByOrgIdAndIufAndIngestionFlowFileIdNot(orgId, iuf, ingestionFlowFileId))
      .thenReturn(paymentsReportingList);

    ResponseEntity<List<PaymentsReporting>> result = controller.findAndDeleteByOrgIdAndIufAndIngestionFlowFileIdNot(orgId, iuf, ingestionFlowFileId);

    assertEquals(paymentsReportingList, result.getBody());
  }

}
