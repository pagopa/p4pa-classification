package it.gov.pagopa.pu.classification.controller;

import static org.junit.jupiter.api.Assertions.*;

import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryRequest;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsRegistryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    CreateAssessmentsRegistryRequest request = CreateAssessmentsRegistryRequest.builder()
      .organizationId(1L)
      .debtPositionTypeOrgCode("debtPositionTypeOrgCode")
      .sectionCode("sectionCode")
      .officeCode("officeCode")
      .assessmentCode("assessmentCode")
      .operatingYear("2025")
      .build();

    Mockito.when(serviceMock.createAssessmentsRegistry(request)).thenReturn(1L);

    ResponseEntity<Long> response = assessmentsRegistryController.createAssessmentsRegistry(request);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1L, response.getBody().longValue());
    Mockito.verify(serviceMock).createAssessmentsRegistry(request);
  }

}
