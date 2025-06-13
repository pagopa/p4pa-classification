package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryEntityExtendedControllerTest {

  @Mock
  private AssessmentsRegistryRepository repositoryMock;

  private AssessmentsRegistryEntityExtendedController controller;

  @BeforeEach
  void init(){
    controller = new AssessmentsRegistryEntityExtendedController(repositoryMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(repositoryMock);
  }

  @Test
  void whenUpdateStatusThenInvokeRepository(){
    // Given
    AssessmentsRegistryStatus status = AssessmentsRegistryStatus.ACTIVE;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    String operatingYear = "operatingYear";
    Mockito.doNothing().when(repositoryMock).updateStatus(status,debtPositionTypeOrgCode,operatingYear);

    // When
    ResponseEntity<Void> result = controller.updateStatus(status,debtPositionTypeOrgCode,operatingYear);

    // Then
    Assertions.assertTrue(result.getStatusCode().is2xxSuccessful());
  }
}
