package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.exception.custom.InvalidDateTimeIntervalException;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import it.gov.pagopa.pu.classification.util.SecurityUtilsTest;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.jemos.podam.api.PodamFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataExportsControllerTest {

  @Mock
  private ClassificationService classificationServiceMock;

  private DataExportsController controller;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final String accessToken = "accessToken";

  @BeforeEach
  void setUp() {
    Integer maxMonthsInterval = 1;
    controller = new DataExportsController(maxMonthsInterval, classificationServiceMock);
    SecurityUtilsTest.configureSecurityContext(accessToken, "userId");
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(classificationServiceMock);
    SecurityUtilsTest.clearSecurityContext();
  }

  @Test
  void exportClassificationsReturnsPagedClassificationViewOnValidInterval() {
    Long organizationId = 1L;
    String operatorExternalUserId = "operator123";
    ExportClassificationsFilterDTO filterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);

    Pageable pageable = PageRequest.of(0, 10);

    PagedClassificationView expectedView = podamFactory.manufacturePojo(PagedClassificationView.class);
    when(classificationServiceMock.getPagedClassificationView(
      organizationId,
      operatorExternalUserId,
      filterDTO,
      pageable,
      accessToken)
    ).thenReturn(expectedView);

    ResponseEntity<PagedClassificationView> response = controller.exportClassifications(
      organizationId,
      operatorExternalUserId,
      filterDTO.getLabel(),
      filterDTO.getLastClassificationDate().getFrom(),
      filterDTO.getLastClassificationDate().getTo(),
      filterDTO.getIuf(),
      filterDTO.getIud(),
      filterDTO.getIuv(),
      filterDTO.getIur(),
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
      filterDTO.getRegulationUniqueIdentifier(),
      filterDTO.getAccountRegistryCode(),
      filterDTO.getBillAmountCents(),
      filterDTO.getRemittanceInformation(),
      filterDTO.getPspCompanyName(),
      filterDTO.getPspLastName(),
      filterDTO.getDebtPositionTypeOrgCodes(),
      pageable
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedView, response.getBody());
  }

  @Test
  void exportClassificationsThrowsInvalidDateTimeIntervalExceptionOnInvalidInterval() {
    Pageable pageable = PageRequest.of(0, 10);

    LocalDate invalidFrom = LocalDate.now();
    LocalDate invalidTo = invalidFrom.plusMonths(2);

    assertThrows(InvalidDateTimeIntervalException.class, () ->
      controller.exportClassifications(
        1L,
        "operator123",
        Set.of(ClassificationsEnum.TES_NO_MATCH),
        invalidFrom,
        invalidTo,
        "iuf_value",
        "iud_value",
        List.of("iuv_value"),
        List.of("iur_value"),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "regUniqueId",
        "accRegistryCode",
        1000L,
        "remittanceInfo",
        "pspCompany",
        "pspLastName",
        Set.of("code"),
        pageable
      )
    );
  }

  @Test
  void exportFullClassificationsReturnsPagedFullClassificationViewOnValidInterval() {
    Long organizationId = 1L;
    String operatorExternalUserId = "operator123";
    Pageable pageable = PageRequest.of(0, 10);
    PagedFullClassificationView expectedView = new PagedFullClassificationView();
    ExportClassificationsFilterDTO filterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);

    when(classificationServiceMock.getPagedFullClassificationView(
      organizationId,
      operatorExternalUserId,
      filterDTO,
      pageable,
      accessToken)
    ).thenReturn(expectedView);

    ResponseEntity<PagedFullClassificationView> response = controller.exportFullClassifications(
      organizationId,
      operatorExternalUserId,
      filterDTO.getLabel(),
      filterDTO.getLastClassificationDate().getFrom(),
      filterDTO.getLastClassificationDate().getTo(),
      filterDTO.getIuf(),
      filterDTO.getIud(),
      filterDTO.getIuv(),
      filterDTO.getIur(),
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
      filterDTO.getRegulationUniqueIdentifier(),
      filterDTO.getAccountRegistryCode(),
      filterDTO.getBillAmountCents(),
      filterDTO.getRemittanceInformation(),
      filterDTO.getPspCompanyName(),
      filterDTO.getPspLastName(),
      filterDTO.getDebtPositionTypeOrgCodes(),
      pageable
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedView, response.getBody());
  }

  @Test
  void exportFullClassificationsThrowsInvalidDateTimeIntervalExceptionOnInvalidInterval() {
    LocalDate now = LocalDate.now();
    Pageable pageable = PageRequest.of(0, 10);
    LocalDate invalidFrom = now.minusMonths(2);

    assertThrows(InvalidDateTimeIntervalException.class, () ->
      controller.exportFullClassifications(
        1L,
        "operator123",
        Set.of(ClassificationsEnum.TES_NO_MATCH),
        invalidFrom,
        now,
        "iuf_value",
        "iud_value",
        List.of("iuv_value"),
        List.of("iur_value"),
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        "regUniqueId",
        "accRegistryCode",
        1000L,
        "remittanceInfo",
        "pspCompany",
        "pspLastName",
        Set.of("code"),
        pageable
      )
    );
  }
}
