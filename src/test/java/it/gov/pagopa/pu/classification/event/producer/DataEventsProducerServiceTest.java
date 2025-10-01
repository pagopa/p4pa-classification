package it.gov.pagopa.pu.classification.event.producer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import it.gov.pagopa.pu.classification.dto.AssessmentsDetailDataDTO;
import it.gov.pagopa.pu.classification.dto.PaymentAssessmentsDataDTO;
import it.gov.pagopa.pu.classification.enums.DataEventType;
import it.gov.pagopa.pu.classification.event.dto.DataEventDTO;
import it.gov.pagopa.pu.classification.event.dto.DataEventRequestDTO;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.model.Classification;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

@ExtendWith(MockitoExtension.class)
class DataEventsProducerServiceTest {

  @Mock
  private StreamBridge streamBridge;

  private DataEventsProducerService dataEventsProducerService;

  @BeforeEach
  void setUp() {
    dataEventsProducerService = new DataEventsProducerService(streamBridge);
  }

  @AfterEach
  void clear(){
    MDC.clear();
  }

  @Test
  void whenNotifyAssessmentsDetailEventThenSendMessage() {
    // Given
    AssessmentsDetailDataDTO assessmentsDataDTO = new AssessmentsDetailDataDTO();
    Assessments assessments = new Assessments();
    assessments.setAssessmentId(1L);
    AssessmentsDetail assessmentsDetail = new AssessmentsDetail();
    assessmentsDetail.setAssessmentDetailId(99L);
    assessmentsDataDTO.setAssessments(assessments);
    assessmentsDataDTO.setAssessmentsDetailList(List.of(assessmentsDetail));

    DataEventRequestDTO dataEventRequestDTO = new DataEventRequestDTO(DataEventType.ASSESSMENTS_DETAIL, "EVENTDESCRIPTION");
    String traceId = "TRACEID";
    MDC.put("traceId", traceId);

    // When
    dataEventsProducerService.notifyAssessmentsDetailEvent(assessmentsDataDTO, dataEventRequestDTO);

    // Then
    verify(streamBridge, times(1)).send(
      Mockito.eq("dataEventsProducer-out-0"),
      Mockito.any(),
      Mockito.<Message<?>>argThat(m -> {
        DataEventDTO<?> payload = (DataEventDTO<?>)m.getPayload();
        String eventIdPrefix = dataEventRequestDTO.getDataEventType().name() + assessmentsDataDTO.getAssessments().getAssessmentId();
        Assertions.assertEquals(eventIdPrefix, payload.getEventId().substring(0, eventIdPrefix.length()));
        Assertions.assertSame(assessmentsDataDTO, payload.getPayload());
        Assertions.assertSame(dataEventRequestDTO.getEventDescription(), payload.getEventDescription());
        Assertions.assertSame(dataEventRequestDTO.getDataEventType(), payload.getEventType());
        Assertions.assertEquals("assessments"+assessmentsDataDTO.getAssessments().getOrganizationId(), m.getHeaders().get(KafkaHeaders.KEY));
        return true;
      }));
  }

  @Test
  void whenNotifyPaymentAssessmentsEventThenSendMessage() {
    // Given
    PaymentAssessmentsDataDTO assessmentsDataDTO = new PaymentAssessmentsDataDTO();
    assessmentsDataDTO.setAssessmentId(1L);
    AssessmentsDetail assessmentsDetail = new AssessmentsDetail();
    assessmentsDetail.setAssessmentDetailId(99L);
    assessmentsDataDTO.setAssessmentsDetailList(List.of(assessmentsDetail));

    DataEventRequestDTO dataEventRequestDTO = new DataEventRequestDTO(DataEventType.PAYMENT_ASSESSMENTS, "EVENTDESCRIPTION");
    String traceId = "TRACEID";
    MDC.put("traceId", traceId);

    // When
    dataEventsProducerService.notifyPaymentAssessmentsEvent(assessmentsDataDTO, dataEventRequestDTO);

    // Then
    verify(streamBridge, times(1)).send(
      Mockito.eq("dataEventsProducer-out-0"),
      Mockito.any(),
      Mockito.<Message<?>>argThat(m -> {
        DataEventDTO<?> payload = (DataEventDTO<?>)m.getPayload();
        String eventIdPrefix = dataEventRequestDTO.getDataEventType().name() + assessmentsDataDTO.getAssessmentId();
        Assertions.assertEquals(eventIdPrefix, payload.getEventId().substring(0, eventIdPrefix.length()));
        Assertions.assertSame(assessmentsDataDTO, payload.getPayload());
        Assertions.assertSame(dataEventRequestDTO.getEventDescription(), payload.getEventDescription());
        Assertions.assertSame(dataEventRequestDTO.getDataEventType(), payload.getEventType());
        Assertions.assertEquals("assessments"+assessmentsDataDTO.getOrganizationId(), m.getHeaders().get(KafkaHeaders.KEY));
        return true;
      }));
  }

  @Test
  void whenNotifyClassificationEventThenSendMessage() {
    // Given
    Classification classification = new Classification();
    classification.setOrganizationId(1L);
    classification.setTransferIndex(1);
    classification.setIud("IUD");
    List<Classification> classificationList = List.of(classification);

    DataEventRequestDTO dataEventRequestDTO = new DataEventRequestDTO(DataEventType.TRANSFER_CLASSIFICATION_LABELS, "EVENTDESCRIPTION");
    String traceId = "TRACEID";
    MDC.put("traceId", traceId);

    // When
    dataEventsProducerService.notifyClassificationEvent(classificationList, dataEventRequestDTO);

    // Then
    verify(streamBridge, times(1)).send(
        Mockito.eq("dataEventsProducer-out-0"),
        Mockito.any(),
        Mockito.<Message<?>>argThat(m -> {
          DataEventDTO<?> payload = (DataEventDTO<?>)m.getPayload();
          String eventIdPrefix = dataEventRequestDTO.getDataEventType().name() + classification.getIud()+classification.getTransferIndex();
          Assertions.assertEquals(eventIdPrefix, payload.getEventId().substring(0, eventIdPrefix.length()));
          Assertions.assertSame(classificationList, payload.getPayload());
          Assertions.assertSame(dataEventRequestDTO.getEventDescription(), payload.getEventDescription());
          Assertions.assertSame(dataEventRequestDTO.getDataEventType(), payload.getEventType());
          Assertions.assertEquals("classification"+classification.getOrganizationId(), m.getHeaders().get(KafkaHeaders.KEY));
          return true;
        }));
  }
}
