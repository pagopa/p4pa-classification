package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import it.gov.pagopa.pu.classification.repository.PaymentsReportingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingExtendedControllerTest {

  @Mock
  private PaymentsReportingRepository repositoryMock;

  private PaymentsReportingEntityExtendedController controller;

  @BeforeEach
  void init(){
    controller = new PaymentsReportingEntityExtendedController(repositoryMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(repositoryMock);
  }

  @Test
  void whenSaveAllThenInvokeRepository(){
    // Given
    List<PaymentsReporting> entities = List.of(new PaymentsReporting());
    Mockito.when(repositoryMock.saveAll(entities))
      .thenReturn(entities);

    // When
    Integer result = controller.saveAll1(entities).getBody();

    // Then
    Assertions.assertEquals(entities.size(), result);
  }
}
