package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsService;
import it.gov.pagopa.pu.classification.util.TestUtils;
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
  void whenCreateAssessmentByReceiptIdWithValidReceiptIdThenReturnAssessments() {
    Long receiptId = 1L;
    Mockito.when(serviceMock.createAssesment(receiptId, TestUtils.getFakeAccessToken()))
      .thenReturn(List.of(new Assessments()));
    TestUtils.setFakeAccessTokenInContext();

    ResponseEntity<List<Assessments>> response = controller.createAssessmentByReceiptId(receiptId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().size());
    Mockito.verify(serviceMock).createAssesment(receiptId, TestUtils.getFakeAccessToken());
  }


}
