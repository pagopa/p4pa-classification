package it.gov.pagopa.pu.classification.service;

import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;


@Slf4j
public class NamespaceFilter extends XMLFilterImpl {

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
    super.startElement("", localName, localName, attributes);
  }

  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    super.endElement("", localName, localName);
  }

  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException {
    log.info("Ignoring namespace prefix: {} with uri: {}", prefix, uri);
  }
}
