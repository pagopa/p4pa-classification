package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsRegistryService;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryControllerTest {
  @Mock
  private AssessmentsRegistryService serviceMock;

  @InjectMocks
  private AssessmentsRegistryController assessmentsRegistryController;

  @AfterEach
  void clearContext(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void whenCreateAssessmentRegistryByDebtPositionDTOAndIudWithValidRequestThenReturnSuccess() {
    String accessToken = "accessToken";
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request =
      CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest.builder()
      .build();

    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");

    ResponseEntity<Void> response = assessmentsRegistryController.createAssessmentsRegistryByDebtPositionDTOAndIud(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void whenCreateAssessmentRegistryWithValidRequestThenReturnSuccess() {
    String accessToken = "accessToken";
    AssessmentsRegistry assessmentsRegistry = TestUtils.getPodamFactory().manufacturePojo(AssessmentsRegistry.class);
    AssessmentsRegistry expectedResponse = TestUtils.getPodamFactory().manufacturePojo(AssessmentsRegistry.class);
    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");

    when(serviceMock.createAssessmentsRegistry(assessmentsRegistry)).thenReturn(expectedResponse);

    ResponseEntity<AssessmentsRegistry> response = assessmentsRegistryController.createAssessmentsRegistry(assessmentsRegistry);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(expectedResponse, response.getBody());
  }

}
