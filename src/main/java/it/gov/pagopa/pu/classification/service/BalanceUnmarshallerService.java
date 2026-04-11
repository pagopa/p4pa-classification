package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.util.ErrorCodeConstants;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamento;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
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
public class BalanceUnmarshallerService {
  private final JAXBContext jaxbContext;
  private final Schema schema;
  private final XMLUnmarshallerService xmlUnmarshallerService;

  private static final String NAMESPACE = "http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/";

  public BalanceUnmarshallerService(@Value("classpath:xsd/PagInf_Dovuti_Pagati_6_2_0.xsd") Resource paymetsReportingXsdResource,
                                    XMLUnmarshallerService xmlUnmarshallerService) {
    try {
      this.jaxbContext = JAXBContext.newInstance(CtBilancio.class);
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      this.schema = schemaFactory.newSchema(paymetsReportingXsdResource.getURL());
      this.xmlUnmarshallerService = xmlUnmarshallerService;
    } catch (JAXBException | SAXException | IOException e) {
      throw new IllegalStateException("Error while creating jaxb context for CtBilancio", e);
    }
  }

  /**
   * Unmarshals a file into a CtFlussoRiversamento object.
   *
   * @param xmlString the XML string to parse
   * @return the unmarshalled CtFlussoRiversamento object
   */
  public CtBilancio unmarshal(String xmlString, Long amountCents) {
    CtBilancio ctBilancio = xmlUnmarshallerService.unmarshal(xmlString, CtBilancio.class, jaxbContext, schema, NAMESPACE);
    if(amountCents!=null && !isValidBalanceAmount(ctBilancio, amountCents)){
      throw new InvalidValueException(ErrorCodeConstants.ERROR_CODE_INVALID_BALANCE_AMOUNT, "Invalid amount balance");
    }
    return ctBilancio;
  }

  private boolean isValidBalanceAmount(CtBilancio ctBilancio, Long amountCents) {
    BigDecimal balanceAmount = ctBilancio.getCapitolo().stream()
      .flatMap(capitolo -> capitolo.getAccertamento().stream())
      .map(CtAccertamento::getImporto)
      .reduce(BigDecimal.ZERO, BigDecimal::add);
    return balanceAmount.compareTo(Utilities.longCentsToBigDecimalEuro(amountCents)) == 0;
  }
}
