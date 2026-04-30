package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancioDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitoloDefault;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceTemplateResolverServiceTest {

  @Mock
  private BalanceService balanceServiceMock;
  @Mock
  private ScriptEngineManager scriptEngineManagerMock;
  @Mock
  private ScriptEngine scriptEngineMock;
  @Mock
  private Invocable invocableMock;

  private BalanceUnmarshallerService balanceUnmarshallerService;

  private BalanceTemplateResolverService service;

  @BeforeEach
  void init() {
    XMLUnmarshallerService xmlUnmarshallerService = new XMLUnmarshallerService();
    XMLMarshallerService xmlMarshallerService = new XMLMarshallerService();
    BalanceDefaultMarshallingService balanceDefaultMarshallingService = new BalanceDefaultMarshallingService(new ClassPathResource("xsd/bilancioDefault.xsd"), xmlMarshallerService, xmlUnmarshallerService);
    service = new BalanceTemplateResolverService(balanceServiceMock, balanceDefaultMarshallingService);

    balanceUnmarshallerService = new BalanceUnmarshallerService(new ClassPathResource("xsd/PagInf_Dovuti_Pagati_6_2_0.xsd"),  new XMLUnmarshallerService());
  }

  @Test
  void givenBalanceNotDefaultWhenCalculateThenReturnBalanceInput() {
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance("balance")
      .amountCents(100L)
      .remittanceInformation("remittanceInformation")
      .build();

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null)).thenReturn(new CtBilancio());

    String result = service.calculateAmountBalance(request);

    assertEquals("balance", result);
  }

  @Test
  void givenBalanceDefaultWhenCalculateTotalThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>TOTALE</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(100_00L)
      .remittanceInformation("remittanceInformation")
      .build();

    CtBilancioDefault ctBilancioDefault = new CtBilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    ctCapitoloDefault.setCodCapitolo("CAP1");
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("TOTALE");
    ctCapitoloDefault.getAccertamento().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolo().add(ctCapitoloDefault);

    String balanceExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\"><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>100.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null)).thenReturn(ctBilancioDefault);
    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
    assertDoesNotThrow(() -> balanceUnmarshallerService.unmarshal(result,null));
  }

  @Test
  void givenBalanceDefaultWhenCalculateThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><codCapitolo>CAP1</codCapitolo><accertamento>" +
      "<importo>function calcola_importo(IMPORTO) {var importo = Number(IMPORTO);var risultato = importo * 0.80;return (Math.ceil(risultato * 100) / 100).toString();}</importo>" +
      "</accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(100_00L)
      .remittanceInformation("remittanceInformation")
      .build();

    CtBilancioDefault ctBilancioDefault = new CtBilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    ctCapitoloDefault.setCodCapitolo("CAP1");
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("function calcola_importo(IMPORTO){var importo = Number(IMPORTO);var risultato = importo * 0.80;return (Math.ceil(risultato * 100) / 100).toString();}");
    ctCapitoloDefault.getAccertamento().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolo().add(ctCapitoloDefault);

    String balanceExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\"><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>80.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null)).thenReturn(ctBilancioDefault);

    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
    assertDoesNotThrow(() -> balanceUnmarshallerService.unmarshal(result,null));
  }

  @Test
  void givenBalanceDefaultWhenCalculateExtractThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>function estrai_importo(IMPORTO) { return '90.00'; }</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(100_00L)
      .remittanceInformation("remittanceInformation")
      .build();

    CtBilancioDefault ctBilancioDefault = new CtBilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    ctCapitoloDefault.setCodCapitolo("CAP1");
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("function estrai_importo(IMPORTO){ return '90.00'; }");
    ctCapitoloDefault.getAccertamento().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolo().add(ctCapitoloDefault);

    String balanceExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\"><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>90.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null)).thenReturn(ctBilancioDefault);

    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
    assertDoesNotThrow(() -> balanceUnmarshallerService.unmarshal(result,null));
  }

  @Test
  void givenBalanceDefaultNotValidWhenCalculateThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><accertamento><importo>NOT_VALID</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(100_00L)
      .remittanceInformation("remittanceInformation")
      .build();

    CtBilancioDefault ctBilancioDefault = new CtBilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("NOT_VALID");
    ctCapitoloDefault.getAccertamento().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolo().add(ctCapitoloDefault);

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null)).thenReturn(ctBilancioDefault);
    InvalidValueException exception = assertThrows(InvalidValueException.class, () -> service.calculateAmountBalance(request));

    assertEquals("BALANCE_CALCULATION_ERROR",exception.getCode());
    assertEquals("Error calculating amount of balance: NOT_VALID as function type to calculate amount balance not supported", exception.getMessage());
  }
}
