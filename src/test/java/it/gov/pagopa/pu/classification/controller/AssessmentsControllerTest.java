package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.service.AssessmentsService;
import it.gov.pagopa.pu.classification.util.faker.InstallmentNoPIIResponseFaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

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
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(serviceMock);
  }

  @Test
  void whenCreateAssessmentByReceiptIdThenReturnOk() {
    // Given
    Long receiptId = 1L;
    Mockito.when(serviceMock.getInstallmentsByReceiptId(receiptId))
      .thenReturn(List.of(InstallmentNoPIIResponseFaker.buildInstallmentNoPIIResponse()));

    // When
    ResponseEntity<Void> response = controller.createAssessmentByReceiptId(receiptId);

    // Then
    Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
