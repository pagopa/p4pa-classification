package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.service.AssessmentsService;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.classification.util.faker.InstallmentNoPIIResponseFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AssessmentsControllerTest {

  @Mock
  private AssessmentsService serviceMock;

  private AssessmentsController controller;

@BeforeEach
  void init() {
    controller = new AssessmentsController(serviceMock);
  }

  @Test
  void whenCreateAssessmentByReceiptIdThenReturnOk() {
    Long receiptId = 1L;
    Mockito.when(serviceMock.getInstallmentsByReceiptId(receiptId, TestUtils.getFakeAccessToken()))
      .thenReturn(List.of(InstallmentNoPIIResponseFaker.buildInstallmentNoPIIResponse()));
    TestUtils.setFakeAccessTokenInContext();

    ResponseEntity<List<Assessments>> response = controller.createAssessmentByReceiptId(receiptId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    Mockito.verify(serviceMock).getInstallmentsByReceiptId(receiptId, TestUtils.getFakeAccessToken());
  }
}
