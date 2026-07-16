package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.util.ErrorCodeConstants;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamento;
import it.veneto.regione.schemas._2012.pagamenti.ente.Bilancio;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.math.BigDecimal;

@Lazy
@Component
@Slf4j
public class BalanceMarshallingService {
  private final JAXBContext jaxbContext;
  private final Schema schema;
  private final XMLMarshallerService xmlMarshallerService;
  private final XMLUnmarshallerService xmlUnmarshallerService;

  private static final String NAMESPACE = "http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/";
  private static final String ROOT_ELEMENT = "bilancio";

  public BalanceMarshallingService(
    @Value("classpath:xsd/PagInf_Dovuti_Pagati_6_2_0.xsd") Resource paymetsReportingXsdResource,
    XMLMarshallerService xmlMarshallerService,
    XMLUnmarshallerService xmlUnmarshallerService
  ) {
    try {
      this.jaxbContext = JAXBContext.newInstance(Bilancio.class);
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      this.schema = schemaFactory.newSchema(paymetsReportingXsdResource.getURL());
      this.xmlMarshallerService = xmlMarshallerService;
      this.xmlUnmarshallerService = xmlUnmarshallerService;
    } catch (JAXBException | SAXException | IOException e) {
      throw new IllegalStateException("Error while creating jaxb context for CtBilancio", e);
    }
  }

  /**
   * Marshals a CtBilancio object into an XML string.
   *
   * @param ctBilancio the CtBilancio object to serialize
   * @return the marshalled CtBilancio to xmlString
   */
  public String marshal(Bilancio ctBilancio) {
    return xmlMarshallerService.marshal(ctBilancio, Bilancio.class, jaxbContext, schema, NAMESPACE, ROOT_ELEMENT);
  }

  /**
   * Unmarshals a file into a CtFlussoRiversamento object.
   *
   * @param xmlString the XML string to parse
   * @return the unmarshalled CtFlussoRiversamento object
   */
  public Bilancio unmarshal(String xmlString, Long amountCents) {
    Bilancio ctBilancio = xmlUnmarshallerService.unmarshal(xmlString, Bilancio.class, jaxbContext, schema, NAMESPACE);
    if(amountCents!=null && !isValidBalanceAmount(ctBilancio, amountCents)){
      throw new InvalidValueException(ErrorCodeConstants.ERROR_CODE_INVALID_BALANCE_AMOUNT, "Invalid amount balance");
    }
    return ctBilancio;
  }

  public boolean isValidBalanceAmount(Bilancio ctBilancio, Long amountCents) {
    BigDecimal balanceAmount = ctBilancio.getCapitolos().stream()
      .flatMap(capitolo -> capitolo.getAccertamentos().stream())
      .map(CtAccertamento::getImporto)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    return balanceAmount.compareTo(Utilities.longCentsToBigDecimalEuro(amountCents)) == 0;
  }
}
