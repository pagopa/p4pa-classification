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
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class XMLMarshallerServiceTest {

  private JAXBContext jaxbContext;
  private Schema schema;

  private XMLMarshallerService service;

  @BeforeEach
  void setUp() throws JAXBException {
    service = new XMLMarshallerService();
    jaxbContext = JAXBContext.newInstance(BilancioDefault.class);

    try {
      URL xsdUrl = getClass().getResource("/xsd/bilancioDefault.xsd");
      if (xsdUrl != null) {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        schema = schemaFactory.newSchema(xsdUrl);
      }
    } catch (Exception e) {
      throw new RuntimeException("Error initializing Schema for testing", e);
    }
  }

  @Test
  void marshalCtBilancioDefaultToXmlString() {
    String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\">" +
      "<capitolo>" +
      "<codCapitolo>CAP1</codCapitolo>" +
      "<codUfficio>UFF1</codUfficio>" +
      "<accertamento>" +
      "<codAccertamento>ACC1</codAccertamento>" +
      "<importo>TOTALE</importo>" +
      "</accertamento>" +
      "</capitolo>" +
      "</bilancio>";

    BilancioDefault bilancio = new BilancioDefault();
    CtCapitoloDefault capitolo = new CtCapitoloDefault();
    capitolo.setCodCapitolo("CAP1");
    capitolo.setCodUfficio("UFF1");
    CtAccertamentoDefault accertamento = new CtAccertamentoDefault();
    accertamento.setCodAccertamento("ACC1");
    accertamento.setImporto("TOTALE");
    capitolo.getAccertamentos().add(accertamento);
    bilancio.getCapitolos().add(capitolo);

    String result = service.marshal(bilancio, BilancioDefault.class, jaxbContext, schema,"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/", "bilancio");

    assertNotNull(result);
    assertEquals(xmlString, result);
  }

  @Test
  void marshalWithNoNamespaceAndRootElementThenException() {
    BilancioDefault bilancio = new BilancioDefault();

    assertThrows(InvalidValueException.class, () -> {
      service.marshal(bilancio, BilancioDefault.class, jaxbContext, schema, null, null);
    });
  }

}
