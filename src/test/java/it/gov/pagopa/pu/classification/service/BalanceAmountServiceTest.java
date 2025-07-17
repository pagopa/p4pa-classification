package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtBilancioDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtCapitoloDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceAmountServiceTest {

  @Mock
  private BalanceService balanceServiceMock;
  @Mock
  private BalanceDefaultMarshallingService balanceDefaultMarshallingServiceMock;
  @Mock
  private ScriptEngineManager scriptEngineManagerMock;
  @Mock
  private ScriptEngine scriptEngineMock;
  @Mock
  private Invocable invocableMock;

  private BalanceAmountService service;

  @BeforeEach
  void init() {
    service = new BalanceAmountService(balanceServiceMock, balanceDefaultMarshallingServiceMock);
  }

  @Test
  void givenBalanceNotDefaultWhenCalculateThenReturnBalanceInput() {
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance("balance")
      .amountCents(100L)
      .remittanceInformation("remittanceInformation")
      .build();

    when(balanceServiceMock.unmarshalBalance(request.getBalance())).thenReturn(new CtBilancio());

    String result = service.calculateAmountBalance(request);

    assertEquals("balance", result);
  }

  @Test
  void givenBalanceDefaultWhenCalculateTotalThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><accertamento><importo>TOTALE</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(10000L)
      .remittanceInformation("remittanceInformation")
      .build();

    CtBilancioDefault ctBilancioDefault = new CtBilancioDefault();

    String balanceExpected = "<bilancio><capitolo><accertamento><importo>100.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance())).thenReturn(ctBilancioDefault);
    when(balanceDefaultMarshallingServiceMock.marshal(ctBilancioDefault)).thenReturn(balanceExpected);
    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
  }

  @Test
  void givenBalanceDefaultWhenCalculateThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><accertamento><importo>function calcola_importo(IMPORTO) { return '90.00'; }</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(10000L)
      .remittanceInformation("remittanceInformation")
      .build();

    CtBilancioDefault ctBilancioDefault = new CtBilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("function calcola_importo(IMPORTO){ return '90.00'; }");
    ctCapitoloDefault.getAccertamento().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolo().add(ctCapitoloDefault);

    String balanceExpected = "<bilancio><capitolo><accertamento><importo>90.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance())).thenReturn(ctBilancioDefault);

    when(balanceDefaultMarshallingServiceMock.marshal(ctBilancioDefault)).thenReturn(balanceExpected);
    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
  }

  @Test
  void givenBalanceDefaultWhenCalculateExtractThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><accertamento><importo>function estrai_importo(IMPORTO) { return '90.00'; }</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(10000L)
      .remittanceInformation("remittanceInformation")
      .build();

    CtBilancioDefault ctBilancioDefault = new CtBilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("function estrai_importo(IMPORTO){ return '90.00'; }");
    ctCapitoloDefault.getAccertamento().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolo().add(ctCapitoloDefault);

    String balanceExpected = "<bilancio><capitolo><accertamento><importo>90.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance())).thenReturn(ctBilancioDefault);

    when(balanceDefaultMarshallingServiceMock.marshal(ctBilancioDefault)).thenReturn(balanceExpected);
    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
  }

  @Test
  void givenBalanceDefaultNotValidWhenCalculateThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><accertamento><importo>NOT_VALID</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(10000L)
      .remittanceInformation("remittanceInformation")
      .build();

    CtBilancioDefault ctBilancioDefault = new CtBilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("NOT_VALID");
    ctCapitoloDefault.getAccertamento().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolo().add(ctCapitoloDefault);

    when(balanceServiceMock.unmarshalBalance(request.getBalance())).thenReturn(ctBilancioDefault);
    InvalidValueException exception = assertThrows(InvalidValueException.class, () -> service.calculateAmountBalance(request));

    assertEquals("Error calculating amount of balance: NOT_VALID as function type to calculate amount balance not supported", exception.getMessage());
  }
}
