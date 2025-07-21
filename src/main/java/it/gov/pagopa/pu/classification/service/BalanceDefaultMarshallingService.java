package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.enums.BalanceDefaultAmountType;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancioDefault;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
public class BalanceDefaultMarshallingService {
  private final JAXBContext jaxbContext;
  private final Schema schema;
  private final XMLMarshallerService xmlMarshallerService;
  private final XMLUnmarshallerService xmlUnmarshallerService;

  private static final String NAMESPACE = "http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/";
  private static final String ROOT_ELEMENT = "bilancio";

  public BalanceDefaultMarshallingService(@Value("classpath:xsd/bilancioDefault.xsd") Resource xsdResource,
                                          XMLMarshallerService xmlMarshallerService, XMLUnmarshallerService xmlUnmarshallerService) {
    try {
      this.jaxbContext = JAXBContext.newInstance(CtBilancioDefault.class);
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      this.schema = schemaFactory.newSchema(xsdResource.getURL());
      this.xmlMarshallerService = xmlMarshallerService;
      this.xmlUnmarshallerService = xmlUnmarshallerService;
    } catch (JAXBException | SAXException | IOException e) {
      throw new IllegalStateException("Error while creating jaxb context for CtBilancioDefault", e);
    }
  }

  /**
   * Marshal CtBilancioDefault object into an xmlString
   *
   * @param ctBilancioDefault the XML object to parse
   * @return the marshalled CtBilancioDefault to xmlString
   */
  public String marshal(CtBilancioDefault ctBilancioDefault) {
    return xmlMarshallerService.marshal(ctBilancioDefault, CtBilancioDefault.class, jaxbContext, schema, NAMESPACE, ROOT_ELEMENT);
  }

  /**
   * Unmarshal xmlString into a CtBilancioDefault object
   *
   * @param xmlString the XML string to parse
   * @return the unmarshalled CtBilancioDefault
   */
  public CtBilancioDefault unmarshal(String xmlString) {
    CtBilancioDefault ctBilancioDefault = xmlUnmarshallerService.unmarshal(xmlString, CtBilancioDefault.class, jaxbContext, schema, NAMESPACE);
    if(!isValidBalanceAmountTypes(ctBilancioDefault)){
      throw new InvalidValueException("Function type to calculate amount balance not supported");
    }
    return ctBilancioDefault;
  }

  private boolean isValidBalanceAmountTypes(CtBilancioDefault ctBilancioDefault) {
    return ctBilancioDefault.getCapitolo().stream()
      .flatMap(capitolo -> capitolo.getAccertamento().stream())
      .map(CtAccertamentoDefault::getImporto)
      .allMatch(this::containsValidAmountType);
  }

  private boolean containsValidAmountType(String amountType) {
    if (amountType == null || amountType.trim().isEmpty()) {
      return false;
    }

    return Arrays.stream(BalanceDefaultAmountType.values())
      .map(BalanceDefaultAmountType::getType).anyMatch(amountType::contains);
  }
}
