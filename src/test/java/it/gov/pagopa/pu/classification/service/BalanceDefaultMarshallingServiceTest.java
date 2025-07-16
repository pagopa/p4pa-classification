package it.gov.pagopa.pu.classification.service;

import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtBilancioDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtCapitoloDefault;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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


  private static final String XML_STRING_BILANCIO_WITH_NAMESPACE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/BilancioDefault/\">" +
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
    CtBilancioDefault ctBilancioDefault = new CtBilancioDefault();
    CtCapitoloDefault ctCapitoloDefault = new CtCapitoloDefault();
    CtAccertamentoDefault ctAccertamentoDefault = new CtAccertamentoDefault();
    ctAccertamentoDefault.setCodAccertamento("ACC1");
    ctAccertamentoDefault.setImporto("TOTALE");
    ctCapitoloDefault.getAccertamento().add(ctAccertamentoDefault);
    ctCapitoloDefault.setCodCapitolo("CAP1");
    ctCapitoloDefault.setCodUfficio("UFF1");
    ctBilancioDefault.getCapitolo().add(ctCapitoloDefault);

    String result = service.marshal(ctBilancioDefault);

    assertNotNull(result);
    assertEquals(XML_STRING_BILANCIO_WITH_NAMESPACE, result);
  }

  @Test
  void testUnmarshalValidXmlWithNamespace() {
    CtBilancioDefault result = service.unmarshal(XML_STRING_BILANCIO_WITH_NAMESPACE);

    assertNotNull(result);
    assertEquals("CAP1", result.getCapitolo().getFirst().getCodCapitolo());
    assertEquals("UFF1", result.getCapitolo().getFirst().getCodUfficio());
    assertEquals("ACC1", result.getCapitolo().getFirst().getAccertamento().getFirst().getCodAccertamento());
    assertEquals("TOTALE", result.getCapitolo().getFirst().getAccertamento().getFirst().getImporto());
  }

  @Test
  void testUnmarshalValidXmlWithoutNamespace() {
    CtBilancioDefault result = service.unmarshal(XML_STRING_BILANCIO_WITHOUT_NAMESPACE);

    assertNotNull(result);
    assertEquals("CAP1", result.getCapitolo().getFirst().getCodCapitolo());
    assertEquals("UFF1", result.getCapitolo().getFirst().getCodUfficio());
    assertEquals("ACC1", result.getCapitolo().getFirst().getAccertamento().getFirst().getCodAccertamento());
    assertEquals("TOTALE", result.getCapitolo().getFirst().getAccertamento().getFirst().getImporto());
  }

  @Test
  void testJAXBExceptionInConstructor() {
    try(MockedStatic<JAXBContext> mockedStaticJAXBContext = Mockito.mockStatic(JAXBContext.class)) {
      mockedStaticJAXBContext.when(() -> JAXBContext.newInstance(CtBilancioDefault.class))
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
