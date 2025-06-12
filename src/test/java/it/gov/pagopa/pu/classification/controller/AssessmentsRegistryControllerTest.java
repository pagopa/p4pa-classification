package it.gov.pagopa.pu.classification.controller;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsRegistryService;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryControllerTest {
  @Mock
  private AssessmentsRegistryService serviceMock;

  @InjectMocks
  private AssessmentsRegistryController assessmentsRegistryController;

  @Test
  void whenCreateAssessmentRegistryWithValidRequestThenReturnSuccess() {
    String accessToken = "accessToken";
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request =
      CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest.builder()
      .build();

    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");

    ResponseEntity<Void> response = assessmentsRegistryController.createAssessmentsRegistryByDebtPositionDTOAndIud(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

}
