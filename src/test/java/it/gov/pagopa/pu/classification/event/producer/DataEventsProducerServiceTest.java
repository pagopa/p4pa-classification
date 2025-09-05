package it.gov.pagopa.pu.classification.event.producer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import it.gov.pagopa.pu.classification.enums.DataEventType;
import it.gov.pagopa.pu.classification.event.dto.DataEventDTO;
import it.gov.pagopa.pu.classification.event.dto.DataEventRequestDTO;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
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
  void whenNotifyAssessmentsEventThenSendMessage() {
    // Given
    AssessmentsDetail assessmentsDetail = new AssessmentsDetail();
    assessmentsDetail.setAssessmentId(1L);
    assessmentsDetail.setAssessmentCode("CODE");
    assessmentsDetail.setAssessmentDetailId(99L);

    DataEventRequestDTO dataEventRequestDTO = new DataEventRequestDTO(DataEventType.ASSESSMENTS_CREATED, "EVENTDESCRIPTION");
    String traceId = "TRACEID";
    MDC.put("traceId", traceId);

    // When
    dataEventsProducerService.notifyAssessmentsEvent(assessmentsDetail, dataEventRequestDTO);

    // Then
    verify(streamBridge, times(1)).send(
      Mockito.eq("dataEventsProducer-out-0"),
      Mockito.any(),
      Mockito.<Message<?>>argThat(m -> {
        DataEventDTO<?> payload = (DataEventDTO<?>)m.getPayload();
        String eventIdPrefix = dataEventRequestDTO.getDataEventType().name() + assessmentsDetail.getAssessmentId();
        Assertions.assertEquals(eventIdPrefix, payload.getEventId().substring(0, eventIdPrefix.length()));
        Assertions.assertSame(assessmentsDetail, payload.getPayload());
        Assertions.assertSame(dataEventRequestDTO.getEventDescription(), payload.getEventDescription());
        Assertions.assertSame(dataEventRequestDTO.getDataEventType(), payload.getEventType());
        Assertions.assertEquals(String.valueOf(assessmentsDetail.getOrganizationId()), m.getHeaders().get(KafkaHeaders.KEY));
        return true;
      }));
  }
}
