package it.gov.pagopa.pu.classification.service;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;


@Slf4j
public class NamespaceFilter extends XMLFilterImpl {

  private static final String NAMESPACE = "http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/";

  @Override
  public void endElement(String uri, String localName, String qName)
          throws SAXException {
    super.endElement(NAMESPACE, localName, qName);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
    if (uri == null || uri.isEmpty()) {
      log.info("Adding namespace to element: {}", localName);
      AttributesImpl newAttrs = new AttributesImpl(atts);
      super.startElement(NAMESPACE, localName, qName, newAttrs);
    } else {
      super.startElement(uri, localName, qName, atts);
    }
  }
}
