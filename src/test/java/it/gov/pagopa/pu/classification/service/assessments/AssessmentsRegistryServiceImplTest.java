package it.gov.pagopa.pu.classification.service.assessments;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryRequest;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import it.gov.pagopa.pu.classification.util.Utilities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryServiceImplTest {

  @Mock
  private AssessmentsRegistryRepository assessmentsRegistryRepositoryMock;

  @InjectMocks
  private AssessmentsRegistryServiceImpl assessmentsRegistryService;

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(assessmentsRegistryRepositoryMock);
  }

  @Test
  void givenValidRequestWhenCreateAssessmentsRegistryThenReturnId() {
    // Given
    String externalUserId = "USERID";
    String traceId = "TRACEID";
    CreateAssessmentsRegistryRequest request = new CreateAssessmentsRegistryRequest();
    request.setOrganizationId(1L);
    request.setDebtPositionTypeOrgCode("debtPositionTypeOrgCode");
    request.setSectionCode("sectionCode");
    request.setSectionDescription("sectionDescription");
    request.setOfficeCode("officeCode");
    request.setOfficeDescription("officeDescription");
    request.setAssessmentCode("assessmentCode");
    request.setAssessmentDescription("assessmentDescription");
    request.setOperatingYear("2025");

    try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class);
         MockedStatic<Utilities> utilMock = mockStatic(Utilities.class)) {

      securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserExternalId).thenReturn(externalUserId);
      utilMock.when(Utilities::getTraceId).thenReturn(traceId);

      when(assessmentsRegistryRepositoryMock.insertIfNotExists(
        request.getOrganizationId(),
        request.getDebtPositionTypeOrgCode(),
        request.getSectionCode(),
        request.getSectionDescription(),
        request.getOfficeCode(),
        request.getOfficeDescription(),
        request.getAssessmentCode(),
        request.getAssessmentDescription(),
        request.getOperatingYear(),
        externalUserId,
        traceId)).thenReturn(3L);

      // When
      Long result = assessmentsRegistryService.createAssessmentsRegistry(request);

      // Then
      assertEquals(3L, result);
    }
  }
}
