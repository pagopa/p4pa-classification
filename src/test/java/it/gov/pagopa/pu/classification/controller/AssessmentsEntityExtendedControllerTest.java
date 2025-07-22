package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AssessmentsEntityExtendedControllerTest {

  @Mock
  private AssessmentsRepository repositoryMock;

  private AssessmentsEntityExtendedController controller;

  private final String accessToken = "accessToken";

  @BeforeEach
  void init() {
    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");
    controller = new AssessmentsEntityExtendedController(repositoryMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(repositoryMock);
  }

  @AfterEach
  void clearContext() {
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void whenCreateAssessmentByReceiptIdWithValidReceiptIdThenReturnAssessments() {
    Long assessmentId = 1L;
    Long organizationId = 2L;
    AssessmentStatus status = AssessmentStatus.ACTIVE;
    Mockito.doNothing().when(repositoryMock).updateStatus(status,assessmentId,organizationId);

    ResponseEntity<Void> response = controller.updateStatus(assessmentId,organizationId,status);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
