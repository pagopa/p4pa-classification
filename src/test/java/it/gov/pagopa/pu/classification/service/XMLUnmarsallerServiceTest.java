package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.exception.InvalidValueException;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamento;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.math.BigDecimal;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class XMLUnmarsallerServiceTest {

  private XMLUnmarshallerService service;
  private JAXBContext jaxbContext;
  private Schema schema;


  @BeforeEach
  void setUp() throws JAXBException {
    service = new XMLUnmarshallerService();
    jaxbContext = JAXBContext.newInstance(CtBilancio.class);

    try {
      URL xsdUrl = getClass().getResource("/xsd/PagInf_Dovuti_Pagati_6_2_0.xsd");
      if (xsdUrl != null) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schema = schemaFactory.newSchema(xsdUrl);
      }
    } catch (Exception e) {
      throw new RuntimeException("Error initializing Schema for testing", e);
    }
  }
  @Test
  void unmarshal_returnsObjectOnValidXml() {
    String xmlString = "<bilancio  xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\">" +
      "<capitolo>" +
      "<codCapitolo>CAP1</codCapitolo>" +
      "<codUfficio>UFF1</codUfficio>" +
      "<accertamento>" +
      "<codAccertamento>ACC1</codAccertamento>" +
      "<importo>100.00</importo>" +
      "</accertamento>" +
      "</capitolo>" +
      "</bilancio>";

    CtBilancio expectedBilancio = new CtBilancio();
    CtCapitolo capitolo = new CtCapitolo();
    capitolo.setCodCapitolo("CAP1");
    capitolo.setCodUfficio("UFF1");
    CtAccertamento accertamento = new CtAccertamento();
    accertamento.setCodAccertamento("ACC1");
    accertamento.setImporto(new BigDecimal("100.00"));
    capitolo.getAccertamento().add(accertamento);
    expectedBilancio.getCapitolo().add(capitolo);

    CtBilancio result = service.unmarshal(xmlString, CtBilancio.class, jaxbContext, schema);

    assertNotNull(result);
    assertEquals(expectedBilancio.getCapitolo().size(), result.getCapitolo().size());
    assertEquals(expectedBilancio.getCapitolo().get(0).getCodCapitolo(), result.getCapitolo().get(0).getCodCapitolo());
    assertEquals(expectedBilancio.getCapitolo().get(0).getCodUfficio(), result.getCapitolo().get(0).getCodUfficio());
    assertEquals(expectedBilancio.getCapitolo().get(0).getAccertamento().size(), result.getCapitolo().get(0).getAccertamento().size());
    assertEquals(expectedBilancio.getCapitolo().get(0).getAccertamento().get(0).getCodAccertamento(), result.getCapitolo().get(0).getAccertamento().get(0).getCodAccertamento());
    assertEquals(expectedBilancio.getCapitolo().get(0).getAccertamento().get(0).getImporto(), result.getCapitolo().get(0).getAccertamento().get(0).getImporto());
  }

  @Test
  void unmarshal_throwsInvalidValueExceptionOnInvalidXml() {
    String xmlString = "invalid.xml";

    assertThrows(InvalidValueException.class, () -> {
      service.unmarshal(xmlString, CtBilancio.class, jaxbContext, schema);
    });
  }

  @Test
  void unmarshal_throwsInvalidValueExceptionOnIOException() {
    String xmlString = "nonexistent.xml";

    assertThrows(InvalidValueException.class, () -> {
      service.unmarshal(xmlString, CtBilancio.class, jaxbContext, schema);
    });
  }
}
