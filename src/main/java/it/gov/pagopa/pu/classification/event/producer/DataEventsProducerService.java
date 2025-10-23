package it.gov.pagopa.pu.classification.event.producer;

import it.gov.pagopa.pu.classification.dto.AssessmentsDetailDataDTO;
import it.gov.pagopa.pu.classification.dto.PaymentAssessmentsDataDTO;
import it.gov.pagopa.pu.classification.enums.DataEventType;
import it.gov.pagopa.pu.classification.event.dto.DataEventDTO;
import it.gov.pagopa.pu.classification.event.dto.DataEventRequestDTO;
import it.gov.pagopa.pu.classification.model.Classification;
import it.gov.pagopa.pu.classification.util.Utilities;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Component
public class DataEventsProducerService {

  @Value("${spring.cloud.stream.bindings.dataEventsProducer-out-0.binder}")
  private String binder;

  private final StreamBridge streamBridge;

  public DataEventsProducerService(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  @Configuration
  static class DataEventsProducerConfig {
    @Bean
    public Supplier<Message<DataEventType>> dataEventsProducer() {
      return () -> null;
    }
  }

  public void notifyAssessmentsDetailEvent(AssessmentsDetailDataDTO assessmentsDataDTO, DataEventRequestDTO dataEventRequest) {
    notifyDataEvent(assessmentsDataDTO.getAssessments().getOrganizationId(), String.valueOf(assessmentsDataDTO.getAssessments().getAssessmentId()), assessmentsDataDTO, dataEventRequest, "assessments");
  }

  public void notifyPaymentAssessmentsEvent(
    PaymentAssessmentsDataDTO paymentAssessmentsDataDTO, DataEventRequestDTO dataEventRequest) {
    notifyDataEvent(paymentAssessmentsDataDTO.getOrganizationId(),
      String.valueOf(paymentAssessmentsDataDTO.getAssessmentId()), paymentAssessmentsDataDTO, dataEventRequest, "assessments");
  }

  public void notifyClassificationEvent(List<Classification> classifications, DataEventRequestDTO dataEventRequest) {
    notifyDataEvent(classifications.getFirst().getOrganizationId(),
      ObjectUtils.defaultIfNull(classifications.getFirst().getIud(),"")+classifications.getFirst().getTransferIndex(), classifications, dataEventRequest, "classification");
  }

  public void notifyDataEvent(Long organizationId, String entityId, Object payload, DataEventRequestDTO dataEventRequest, String partitionKey) {
    String eventId = dataEventRequest.getDataEventType().name() + entityId + UUID.randomUUID();
    streamBridge.send("dataEventsProducer-out-0", binder,
      MessageBuilder.withPayload(DataEventDTO.builder()
          .eventId(eventId)
          .traceId(Utilities.getTraceId())
          .eventType(dataEventRequest.getDataEventType())
          .eventDateTime(OffsetDateTime.now())
          .payload(payload)
          .eventDescription(dataEventRequest.getEventDescription())
          .build())
        .setHeader(KafkaHeaders.KEY, partitionKey+organizationId)
        .build()
    );
  }
}
