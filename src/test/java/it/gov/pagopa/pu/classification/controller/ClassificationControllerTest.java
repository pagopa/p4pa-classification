package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.ClassificationPaidInstallmentsFilterDTO;
import it.gov.pagopa.pu.classification.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.TreasuredClassificationFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import it.gov.pagopa.pu.classification.util.TestUtils;
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

import java.time.OffsetDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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
      filterDTO.getDebtPositionTypeOrgCodes(),
      filterDTO.getLabel(),
      filterDTO.getIud(),
      filterDTO.getIuv(),
      filterDTO.getIur(),
      filterDTO.getLastClassificationDate().getFrom(),
      filterDTO.getLastClassificationDate().getTo(),
      filterDTO.getPayDate().getFrom(),
      filterDTO.getPayDate().getTo(),
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
      filterDTO.getDebtorFiscalCode(),
      filterDTO.getBillYear(),
      filterDTO.getBillCode(),
      filterDTO.getDocumentYear(),
      filterDTO.getDocumentCode(),
      filterDTO.getProvisionalAe(),
      filterDTO.getProvisionalCode(),
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
  }

  @Test
  void givenParamsWhenGetPaidInstallmentsThenReturnPagedClassificationPaidInstallmentsView() {
    Long organizationId = 1L;
    String iuv = "IUV123";
    String debtPositionTypeOrgCode = "ORG_CODE";

    OffsetDateTime paymentDateTimeFrom = OffsetDateTime.now().minusDays(1);
    OffsetDateTime paymentDateTimeTo = OffsetDateTime.now();
    OffsetDateTime receiptCreationDateTimeFrom = OffsetDateTime.now().minusDays(2);
    OffsetDateTime receiptCreationDateTimeTo = OffsetDateTime.now();

    Set<String> iuds = Set.of("IUD1", "IUD2");
    Pageable pageable = PageRequest.of(0, 5);

    PagedClassificationPaidInstallmentsView expectedView =
      podamFactory.manufacturePojo(PagedClassificationPaidInstallmentsView.class);

    OffsetDateTimeIntervalFilter paymentInterval =
      new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);

    OffsetDateTimeIntervalFilter receiptCreationDateTimeInterval =
      new OffsetDateTimeIntervalFilter(receiptCreationDateTimeFrom, receiptCreationDateTimeTo);

    ClassificationPaidInstallmentsFilterDTO filter = ClassificationPaidInstallmentsFilterDTO.builder()
      .iuv(iuv)
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .paymentDateTimeIntervalFilter(paymentInterval)
      .receiptCreationDateIntervalFilter(receiptCreationDateTimeInterval)
      .iuds(iuds)
      .build();

    Mockito.when(classificationServiceMock.getPaidInstallmentsView(
      organizationId,
      filter,
      pageable
    )).thenReturn(expectedView);

    ResponseEntity<PagedClassificationPaidInstallmentsView> result = controller.getPaidInstallments(
      organizationId,
      debtPositionTypeOrgCode,
      iuv,
      paymentDateTimeFrom,
      paymentDateTimeTo,
      receiptCreationDateTimeFrom,
      receiptCreationDateTimeTo,
      iuds,
      pageable
    );

    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(expectedView, result.getBody());
  }
}
