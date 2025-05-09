package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A reusable service for generic XML unmarshalling.
 */
@Lazy
@Slf4j
@Service
public class XMLUnmarshallerService {

	/**
	 * Unmarshals an XML string into a Java object.
	 *
	 * @param <T>         the type of the resulting Java object
	 * @param xmlString   the XML string to parse
	 * @param clazz       the class type to which the XML should be unmarshalled
	 * @param jaxbContext the pre-configured JAXBContext instance
	 * @param schema      the pre-configured Schema instance for validation (optional)
	 * @return the unmarshalled Java object of type {@code T}
	 */
	public <T> T unmarshal(String xmlString, Class<T> clazz, JAXBContext jaxbContext, Schema schema) {
		try (InputStream is = new ByteArrayInputStream(xmlString.getBytes())) {
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setNamespaceAware(true);
      SAXParser saxParser = spf.newSAXParser();
      NamespaceFilter namespaceFilter = new NamespaceFilter();
      namespaceFilter.setParent(saxParser.getXMLReader());

      SAXSource saxSource = new SAXSource(namespaceFilter, new org.xml.sax.InputSource(is));

      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
      JAXBElement<T> element = unmarshaller.unmarshal(saxSource, clazz);
      return element.getValue();
		} catch (Exception e) {
			log.error("Error while parsing xml: {}", xmlString, e);
			throw new InvalidValueException("Error while parsing xml: "+ xmlString);
		}
	}
}
