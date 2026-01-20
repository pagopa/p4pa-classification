package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.Marshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import java.io.StringWriter;

@Lazy
@Slf4j
@Service
public class XMLMarshallerService {

  /**
   * Marshals a Java object into an XML string
   *
   * @param <T>         the type of the resulting Java object
   * @param object      the XML object to parse
   * @param clazz       the class type to which the XML should be marshalled
   * @param jaxbContext the pre-configured JAXBContext instance
   * @param schema      the pre-configured Schema instance for validation (optional)
   * @param rootElement the root element
   * @return the marshalled xmlString
   */
  public <T> String marshal(T object, Class<T> clazz, JAXBContext jaxbContext, Schema schema, String namespace, String rootElement) {
    try {
      Marshaller marshaller = jaxbContext.createMarshaller();

      if (schema != null) {
        marshaller.setSchema(schema);
      }

      QName qname = new QName(namespace, rootElement);
      JAXBElement<T> element = new JAXBElement<>(qname, clazz, object);

      StringWriter writer = new StringWriter();
      marshaller.marshal(element, writer);

      return writer.toString();
    } catch (Exception e) {
      log.error("Error while marshalling {}: {}", clazz.getSimpleName(), object, e);
      throw new InvalidValueException("[XML_MARSHALLING_ERROR] Error while marshalling " + clazz.getSimpleName() + ": " + object);
    }
  }

}
