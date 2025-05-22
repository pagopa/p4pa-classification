package it.gov.pagopa.pu.classification.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.TreasuredClassificationFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import it.gov.pagopa.pu.classification.util.Constants;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import it.gov.pagopa.pu.classification.util.TestUtils;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

@ExtendWith(MockitoExtension.class)
class ClassificationControllerTest {

  @Mock
  private ClassificationService classificationServiceMock;

  @InjectMocks
  private ClassificationController controller;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    controller = new ClassificationController(classificationServiceMock);
    SecurityUtilsTest.configureSecurityContext("accessToken", "userId");
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(classificationServiceMock);
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void getClassificationsReturnsPagedTreasuredClassification() {
    Long organizationId = 1L;
    TreasuredClassificationFilterDTO filterDTO = podamFactory.manufacturePojo(
      TreasuredClassificationFilterDTO.class);

    Pageable pageable = PageRequest.of(0, 10);

    PagedTreasuredClassification expectedResult = podamFactory.manufacturePojo(PagedTreasuredClassification.class);
    when(classificationServiceMock.getPagedTreasuredClassification(
      organizationId,
      filterDTO,
      pageable)
    ).thenReturn(expectedResult);

    ResponseEntity<PagedTreasuredClassification> response = controller.getTreasuredClassifications(
      organizationId,
      filterDTO.getLastClassificationDate().getFrom(),
      filterDTO.getLastClassificationDate().getTo(),
      filterDTO.getLabel(),
      filterDTO.getIud(),
      filterDTO.getIuv(),
      filterDTO.getIur(),
      ZonedDateTime.of(filterDTO.getPayDate().getFrom(), Constants.ZONEID).toOffsetDateTime(),
      ZonedDateTime.of(filterDTO.getPayDate().getTo(), Constants.ZONEID).toOffsetDateTime(),
      filterDTO.getPaymentDateTime().getFrom(),
      filterDTO.getPaymentDateTime().getTo(),
      filterDTO.getRegulationDate().getFrom(),
      filterDTO.getRegulationDate().getTo(),
      filterDTO.getBillDate().getFrom(),
      filterDTO.getBillDate().getTo(),
      filterDTO.getRegionValueDate().getFrom(),
      filterDTO.getRegionValueDate().getTo(),
      filterDTO.getPspCompanyName(),
      filterDTO.getPspLastName(),
      filterDTO.getIuf(),
      filterDTO.getRegulationUniqueIdentifier(),
      filterDTO.getAccountRegistryCode(),
      filterDTO.getBillAmountCents(),
      filterDTO.getRemittanceInformation(),
      pageable
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedResult, response.getBody());
  }

  @Test
  void testGetClassificationDetail() {
    Long organizationId = 1L;
    Long classificationId = 1L;
    ClassificationDetailViewDTO mockDetailView = new ClassificationDetailViewDTO();
    when(classificationServiceMock.getClassificationDetailView(organizationId, classificationId)).thenReturn(mockDetailView);

    ResponseEntity<ClassificationDetailViewDTO> response = controller.getClassificationDetail(organizationId, classificationId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(mockDetailView, response.getBody());
    verify(classificationServiceMock).getClassificationDetailView(organizationId, classificationId);
  }
}
