package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

  @Mock
  private BalanceUnmashallerService balanceUnmashallerServiceMock;
  @Mock
  private BalanceDefaultMarshallingService balanceMarshallerServiceMock;
  @Mock
  private AssessmentsRegistryRepository assessmentsRegistryRepositoryMock;

  private BalanceService balanceService;

  @BeforeEach
  void init() {
    balanceService = new BalanceService(balanceUnmashallerServiceMock, balanceMarshallerServiceMock, assessmentsRegistryRepositoryMock);
  }

  @Test
  void givenValidBalanceWhenValidateThenSuccess(){
    String balance = "balance";

    when(balanceUnmashallerServiceMock.unmarshal(balance)).thenReturn(new CtBilancio());

    Boolean result = balanceService.isBalanceValid(balance);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
  void givenNotValidBalanceWhenValidateThenUnsuccessful(){
    String balance = "balanceNotValid";

    when(balanceUnmashallerServiceMock.unmarshal(balance)).thenThrow(InvalidValueException.class);

    Boolean result = balanceService.isBalanceValid(balance);

    assertEquals(Boolean.FALSE, result);
  }
}
