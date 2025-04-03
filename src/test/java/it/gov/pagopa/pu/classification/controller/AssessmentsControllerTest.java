package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsService;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import org.junit.jupiter.api.AfterEach;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AssessmentsControllerTest {

  @Mock
  private AssessmentsService serviceMock;

  private AssessmentsController controller;

  @BeforeEach
  void init() {
    controller = new AssessmentsController(serviceMock);
  }

  @AfterEach
  void clear(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void whenCreateAssessmentByReceiptIdWithValidReceiptIdThenReturnAssessments() {
    Long receiptId = 1L;
    String accessToken = "accessToken";
    Mockito.when(serviceMock.createAssesment(receiptId, accessToken))
      .thenReturn(List.of(new Assessments()));
    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");

    ResponseEntity<List<Assessments>> response = controller.createAssessmentByReceiptId(receiptId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    Mockito.verify(serviceMock).createAssesment(receiptId, accessToken);
  }


}
