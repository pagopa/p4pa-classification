package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.BilancioDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitoloDefault;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceDefaultMarshallingServiceTest {

  private Resource resource;
  private BalanceDefaultMarshallingService service;

  private static final String XML_STRING_CAPITOLO_DEFAULT =  "<capitolo>" +
    "<codCapitolo>CAP1</codCapitolo>" +
    "<codUfficio>UFF1</codUfficio>" +
    "<accertamento>" +
    "<codAccertamento>ACC1</codAccertamento>" +
    "<importo>TOTALE</importo>" +
    "</accertamento>" +
    "</capitolo>";

  private static final String XML_STRING_BILANCIO_WITHOUT_NAMESPACE = "<bilancio>" +
    XML_STRING_CAPITOLO_DEFAULT +
    "</bilancio>";


  private static final String XML_STRING_BILANCIO_WITH_NAMESPACE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\">" +
    XML_STRING_CAPITOLO_DEFAULT +
    "</bilancio>";

  @BeforeEach
  void setUp() {
    XMLUnmarshallerService xmlUnmarshallerService = new XMLUnmarshallerService();
    XMLMarshallerService xmlMarshallerService = new XMLMarshallerService();
    resource = new ClassPathResource("xsd/bilancioDefault.xsd");
    service = new BalanceDefaultMarshallingService(resource, xmlMarshallerService, xmlUnmarshallerService);
  }

  @Test
  void testMarshalValidCtBilancioDefaultWithNamespace() {
    BilancioDefault ctBilancioDefault = new BilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setCodAccertamento("ACC1");
    ctAccertamentoDefault.setImporto("TOTALE");
    ctCapitoloDefault.getAccertamentos().add(ctAccertamentoDefault);
    ctCapitoloDefault.setCodCapitolo("CAP1");
    ctCapitoloDefault.setCodUfficio("UFF1");
    ctBilancioDefault.getCapitolos().add(ctCapitoloDefault);

    String result = service.marshal(ctBilancioDefault);

    assertNotNull(result);
    assertEquals(XML_STRING_BILANCIO_WITH_NAMESPACE, result);
  }

  @ParameterizedTest
  @ValueSource(strings = {"NOT_VALID", ""})
  void testUnmarshalNotValidCtBilancioDefaultWithNamespace(String amountValueType) {
    String balance = "<bilancio>" +
      "<capitolo>" +
      "<codCapitolo>CAP1</codCapitolo>" +
      "<accertamento>" +
      "<importo>" + amountValueType + "</importo>" +
      "</accertamento>" +
      "</capitolo>" +
      "</bilancio>" ;

    InvalidValueException exception = assertThrows(InvalidValueException.class, () -> service.unmarshal(balance));

    assertEquals("BALANCE_MARSHALLING_ERROR",exception.getCode());
    assertEquals("Function type to calculate amount balance not supported", exception.getMessage());
  }

  @Test
  void testUnmarshalValidXmlWithNamespace() {
    BilancioDefault result = service.unmarshal(XML_STRING_BILANCIO_WITH_NAMESPACE);

    assertNotNull(result);
    assertEquals("CAP1", result.getCapitolos().getFirst().getCodCapitolo());
    assertEquals("UFF1", result.getCapitolos().getFirst().getCodUfficio());
    assertEquals("ACC1", result.getCapitolos().getFirst().getAccertamentos().getFirst().getCodAccertamento());
    assertEquals("TOTALE", result.getCapitolos().getFirst().getAccertamentos().getFirst().getImporto());
  }

  @Test
  void testUnmarshalValidXmlWithoutNamespace() {
    BilancioDefault result = service.unmarshal(XML_STRING_BILANCIO_WITHOUT_NAMESPACE);

    assertNotNull(result);
    assertEquals("CAP1", result.getCapitolos().getFirst().getCodCapitolo());
    assertEquals("UFF1", result.getCapitolos().getFirst().getCodUfficio());
    assertEquals("ACC1", result.getCapitolos().getFirst().getAccertamentos().getFirst().getCodAccertamento());
    assertEquals("TOTALE", result.getCapitolos().getFirst().getAccertamentos().getFirst().getImporto());
  }

  @Test
  void testJAXBExceptionInConstructor() {
    try(MockedStatic<JAXBContext> mockedStaticJAXBContext = Mockito.mockStatic(JAXBContext.class)) {
      mockedStaticJAXBContext.when(() -> JAXBContext.newInstance(BilancioDefault.class))
        .thenThrow(new JAXBException("Simulated JAXBException"));
      assertThrows(IllegalStateException.class, () -> new BalanceDefaultMarshallingService(resource, null, null));
    }
  }

  @Test
  void testIOExceptionInConstructor() throws IOException {
    Resource mockResource = mock(Resource.class);
    when(mockResource.getURL()).thenThrow(new IOException("Simulated IOException"));

    assertThrows(IllegalStateException.class, () -> new BalanceDefaultMarshallingService(mockResource, null, null));
  }

}
