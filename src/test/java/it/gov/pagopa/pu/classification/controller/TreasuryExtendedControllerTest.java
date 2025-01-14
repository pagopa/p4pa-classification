package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.model.Treasury;
import it.gov.pagopa.pu.classification.repository.TreasuryRepository;
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
class TreasuryExtendedControllerTest {

  @Mock
  private TreasuryRepository repositoryMock;

  private TreasuryEntityExtendedController controller;

  @BeforeEach
  void init(){
    controller = new TreasuryEntityExtendedController(repositoryMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(repositoryMock);
  }

  @Test
  void whenSaveAllThenInvokeRepository(){
    // Given
    List<Treasury> entities = List.of(new Treasury());
    Mockito.when(repositoryMock.saveAll(entities))
      .thenReturn(entities);

    // When
    int result = controller.saveAll(entities);

    // Then
    Assertions.assertEquals(entities.size(), result);
  }
}
