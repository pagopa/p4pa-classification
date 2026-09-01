package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.CalculateAmountBalanceRequest;
import it.gov.pagopa.pu.classification.dto.generated.DebtPositionTypeOrgBalanceCostDTO;
import it.gov.pagopa.pu.classification.exception.common.InvalidValueException;
import it.gov.pagopa.pu.classification.util.ErrorCodeConstants;
import it.veneto.regione.schemas._2012.pagamenti.ente.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceTemplateResolverServiceTest {

  @Mock
  private BalanceService balanceServiceMock;

  private BalanceTemplateResolverService service;

  @BeforeEach
  void init() {
    XMLUnmarshallerService xmlUnmarshallerService = new XMLUnmarshallerService();
    XMLMarshallerService xmlMarshallerService = new XMLMarshallerService();
    BalanceDefaultMarshallingService balanceDefaultMarshallingService = new BalanceDefaultMarshallingService(new ClassPathResource("xsd/bilancioDefault.xsd"), xmlMarshallerService, xmlUnmarshallerService);
    BalanceMarshallingService balanceMarshallingService = new BalanceMarshallingService(new ClassPathResource("xsd/PagInf_Dovuti_Pagati_6_2_0.xsd"), xmlMarshallerService, xmlUnmarshallerService);

    service = new BalanceTemplateResolverService(balanceServiceMock, balanceDefaultMarshallingService, balanceMarshallingService);
  }

  @Test
  void givenBalanceNotDefaultWhenCalculateThenReturnBalanceInput() {
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance("balance")
      .amountCents(100L)
      .remittanceInformation("remittanceInformation")
      .build();

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null, true)).thenReturn(new Bilancio());

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

    BilancioDefault ctBilancioDefault = new BilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    ctCapitoloDefault.setCodCapitolo("CAP1");
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("TOTALE");
    ctCapitoloDefault.getAccertamentos().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolos().add(ctCapitoloDefault);

    String balanceExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\"><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>100.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null, true)).thenReturn(ctBilancioDefault);
    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
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

    BilancioDefault ctBilancioDefault = new BilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    ctCapitoloDefault.setCodCapitolo("CAP1");
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("function calcola_importo(IMPORTO){var importo = Number(IMPORTO);var risultato = importo * 0.80;return (Math.ceil(risultato * 100) / 100).toString();}");
    ctCapitoloDefault.getAccertamentos().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolos().add(ctCapitoloDefault);

    String balanceExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\"><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>80.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null, true)).thenReturn(ctBilancioDefault);

    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
  }

  @Test
  void givenBalanceDefaultWhenCalculateExtractThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>function estrai_importo(IMPORTO) { return '90.00'; }</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(100_00L)
      .remittanceInformation("remittanceInformation")
      .build();

    BilancioDefault ctBilancioDefault = new BilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    ctCapitoloDefault.setCodCapitolo("CAP1");
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("function estrai_importo(IMPORTO){ return '90.00'; }");
    ctCapitoloDefault.getAccertamentos().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolos().add(ctCapitoloDefault);

    String balanceExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\"><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>90.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null, true)).thenReturn(ctBilancioDefault);

    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
  }

  @Test
  void givenBalanceDefaultNotValidWhenCalculateThenReturnCalculatedBalance() {
    String balance = "<bilancio><capitolo><accertamento><importo>NOT_VALID</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(100_00L)
      .remittanceInformation("remittanceInformation")
      .build();

    BilancioDefault ctBilancioDefault = new BilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("NOT_VALID");
    ctCapitoloDefault.getAccertamentos().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolos().add(ctCapitoloDefault);

    when(balanceServiceMock.unmarshalBalance(request.getBalance(),null, true)).thenReturn(ctBilancioDefault);
    InvalidValueException exception = assertThrows(InvalidValueException.class, () -> service.calculateAmountBalance(request));

    assertEquals("BALANCE_CALCULATION_ERROR",exception.getCode());
    assertEquals("Error calculating amount of balance: NOT_VALID as function type to calculate amount balance not supported", exception.getMessage());
  }

  @Test
  void givenBalanceDefaultAndNotificationFeeWithoutDtoWhenCalculateThenReturnBalanceWithSendFallback() {
    String balance = "<bilancio><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>TOTALE</importo></accertamento></capitolo></bilancio>";
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(100_00L)
      .notificationFeeCents(150L)
      .remittanceInformation("remittanceInformation")
      .build();

    BilancioDefault ctBilancioDefault = new BilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    ctCapitoloDefault.setCodCapitolo("CAP1");
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("TOTALE");
    ctCapitoloDefault.getAccertamentos().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolos().add(ctCapitoloDefault);

    String balanceExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\"><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>100.00</importo></accertamento></capitolo><capitolo><codCapitolo>SEND</codCapitolo><codUfficio>SEND</codUfficio><accertamento><codAccertamento>SEND</codAccertamento><importo>1.50</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance(), null, true)).thenReturn(ctBilancioDefault);

    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
  }

  @Test
  void givenBalanceDefaultAndNotificationFeeWithDtoWhenCalculateThenReturnBalanceWithDtoValues() {
    String balance = "<bilancio><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>TOTALE</importo></accertamento></capitolo></bilancio>";

    DebtPositionTypeOrgBalanceCostDTO dto = new DebtPositionTypeOrgBalanceCostDTO();
    dto.setOfficeCode("UFF1");
    dto.setSectionCode("CAP1");
    dto.setAssessmentCode("ACC1");

    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance(balance)
      .amountCents(100_00L)
      .notificationFeeCents(200L)
      .debtPositionTypeOrgBalanceCost(dto)
      .remittanceInformation("remittanceInformation")
      .build();

    BilancioDefault ctBilancioDefault = new BilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    ctCapitoloDefault.setCodCapitolo("CAP1");
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setImporto("TOTALE");
    ctCapitoloDefault.getAccertamentos().add(ctAccertamentoDefault);
    ctBilancioDefault.getCapitolos().add(ctCapitoloDefault);

    String balanceExpected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\"><capitolo><codCapitolo>CAP1</codCapitolo><accertamento><importo>100.00</importo></accertamento></capitolo><capitolo><codCapitolo>CAP1</codCapitolo><codUfficio>UFF1</codUfficio><accertamento><codAccertamento>ACC1</codAccertamento><importo>2.00</importo></accertamento></capitolo></bilancio>";

    when(balanceServiceMock.unmarshalBalance(request.getBalance(), null, true)).thenReturn(ctBilancioDefault);

    String result = service.calculateAmountBalance(request);

    assertEquals(balanceExpected, result);
  }

  @Test
  void givenBalanceNotDefaultAndNotificationFeeWhenCalculateThenReturnMarshalledUpdatedBalance() {
    DebtPositionTypeOrgBalanceCostDTO dto = new DebtPositionTypeOrgBalanceCostDTO();
    dto.setOfficeCode("UFF2");
    dto.setSectionCode("CAP2");
    dto.setAssessmentCode("ACC2");

    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance("balance")
      .amountCents(100L)
      .notificationFeeCents(250L)
      .debtPositionTypeOrgBalanceCost(dto)
      .remittanceInformation("remittanceInformation")
      .build();

    Bilancio ctBilancio = new Bilancio();
    CtCapitolo ctCapitolo = new CtCapitolo();
    ctCapitolo.setCodCapitolo("CAP1");
    CtAccertamento ctAccertamento = new CtAccertamento();
    ctAccertamento.setCodAccertamento("ACC1");
    ctAccertamento.setImporto(new BigDecimal("100.00"));
    ctCapitolo.getAccertamentos().add(ctAccertamento);
    ctBilancio.getCapitolos().add(ctCapitolo);

    when(balanceServiceMock.unmarshalBalance(request.getBalance(), null, true)).thenReturn(ctBilancio);

    String expectedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
      "<bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\">" +
      "<capitolo><codCapitolo>CAP1</codCapitolo>" +
      "<accertamento><codAccertamento>ACC1</codAccertamento><importo>100.00</importo></accertamento>" +
      "</capitolo>" +
      "<capitolo><codCapitolo>CAP2</codCapitolo><codUfficio>UFF2</codUfficio>" +
      "<accertamento><codAccertamento>ACC2</codAccertamento><importo>2.50</importo></accertamento>" +
      "</capitolo></bilancio>";

    String result = service.calculateAmountBalance(request);

    assertEquals(expectedXml, result);
  }

  @Test
  void givenUnsupportedBalanceTypeWhenCalculateThenThrowInvalidValueException() {
    CalculateAmountBalanceRequest request = CalculateAmountBalanceRequest.builder()
      .balance("unsupported_structure_xml")
      .amountCents(100_00L)
      .remittanceInformation("remittanceInformation")
      .build();

    when(balanceServiceMock.unmarshalBalance(request.getBalance(), null, true))
      .thenReturn("Unsupported balance structure type");

    InvalidValueException exception = assertThrows(InvalidValueException.class, () ->
      service.calculateAmountBalance(request)
    );

    assertEquals(ErrorCodeConstants.ERROR_CODE_BALANCE_MARSHALLING_ERROR, exception.getCode());
    assertEquals("Unsupported balance structure type", exception.getMessage());
  }
}
