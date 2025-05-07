package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassificationControllerTest {
  @Mock
  private ClassificationService classificationServiceMock;

  @InjectMocks
  private ClassificationController controller;

  @BeforeEach
  void setUp() {
    controller = new ClassificationController(classificationServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(classificationServiceMock);
  }

  @Test
  void testGetClassificationDetail() {
    Long organizationId = 1L;
    Long classificationId = 1L;
    ClassificationDetailViewDTO mockDetailView = new ClassificationDetailViewDTO();
    when(classificationServiceMock.getClassificationDetailView(anyLong(), anyLong())).thenReturn(mockDetailView);

    ResponseEntity<ClassificationDetailViewDTO> response = controller.getClassificationDetail(organizationId, classificationId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockDetailView, response.getBody());
    verify(classificationServiceMock).getClassificationDetailView(organizationId, classificationId);
  }
}
