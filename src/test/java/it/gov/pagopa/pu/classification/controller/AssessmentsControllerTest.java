package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsService;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class AssessmentsControllerTest {

  @Mock
  private AssessmentsService serviceMock;

  private AssessmentsController controller;
  private PodamFactory podamFactory;

  @BeforeEach
  void init() {
    controller = new AssessmentsController(serviceMock);
    podamFactory = new PodamFactoryImpl();
  }

  @AfterEach
  void clear(){
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void whenCreateAssessmentByReceiptIdWithValidReceiptIdThenReturnAssessments() {
    Long receiptId = 1L;
    String accessToken = "accessToken";
    Mockito.when(serviceMock.createAssesment(receiptId, accessToken))
      .thenReturn(List.of(new Assessments()));
    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");

    ResponseEntity<List<Assessments>> response = controller.createAssessmentByReceiptId(receiptId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(1, response.getBody().size());
    Mockito.verify(serviceMock).createAssesment(receiptId, accessToken);
  }


  @Test
  void givenParamsWhenGetPagedAssessmentsListThenReturnPagedAssessmentsView() {
    //given
    String assessmentName = "ASSESSMENT_NAME";
    OffsetDateTime from = OffsetDateTime.now();
    OffsetDateTime to = OffsetDateTime.now().plusDays(1L);
    String iuv = "IUV";
    String debtPositionTypeOrg = "DEBT_POSITION_TYPE_ORG";
    String accessToken = "accessToken";
    LocalDateTimeIntervalFilter localDateTimeIntervalFilter = new LocalDateTimeIntervalFilter(from.toLocalDateTime(), to.toLocalDateTime());
    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");

    PagedAssessmentsView pagedAssessmentsView = podamFactory.manufacturePojo(PagedAssessmentsView.class);
    Mockito.when(serviceMock.getPagedAssessmentsView(assessmentName, localDateTimeIntervalFilter, iuv, debtPositionTypeOrg, AssessmentStatus.NEW, Pageable.ofSize(1), accessToken)).thenReturn(pagedAssessmentsView);
    //when
    ResponseEntity<PagedAssessmentsView> result = controller.getPagedAssessmentsList(assessmentName, from, to, iuv, debtPositionTypeOrg, AssessmentStatus.NEW, Pageable.ofSize(1));
    //then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(5, result.getBody().getContent().size());
    Mockito.verify(serviceMock).getPagedAssessmentsView(assessmentName, localDateTimeIntervalFilter, iuv, debtPositionTypeOrg, AssessmentStatus.NEW, Pageable.ofSize(1), accessToken);
  }
}
