package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsDetailService;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AssessmentsDetailControllerTest {

  @Mock
  private AssessmentsDetailService assessmentsDetailServiceMock;

  private AssessmentsDetailController controller;

  @BeforeEach
  void init() {
    controller = new AssessmentsDetailController(assessmentsDetailServiceMock);
  }

  @AfterEach
  void clear(){
    Mockito.verifyNoMoreInteractions(assessmentsDetailServiceMock);
  }

  @Test
  void whenCreateAssessmentsDetailThenOk() {
    Long organizationId = 1L;
    Long assessmentsId = 2L;
    Long assessmentsRegistryId = 3L;
    Set<String> iudSet = Collections.singleton("iud");
    List<AssessmentsDetail> expectedResult = Collections.singletonList(new AssessmentsDetail());
    String accessToken = "accessToken";
    CreateAssessmentsDetail createAssessmentsDetail = new CreateAssessmentsDetail();
    createAssessmentsDetail.setAssessmentRegistryId(assessmentsRegistryId);
    createAssessmentsDetail.setIuds(iudSet);
    Mockito.when(assessmentsDetailServiceMock.createAssessmentsDetail(
            organizationId,assessmentsId,createAssessmentsDetail,accessToken))
      .thenReturn(expectedResult);
    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");

    ResponseEntity<List<AssessmentsDetail>> response = controller.createAssessmentsDetail(
            organizationId,assessmentsId,createAssessmentsDetail);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResult,response.getBody());
  }
}
