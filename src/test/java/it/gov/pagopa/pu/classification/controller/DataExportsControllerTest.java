package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.exception.custom.InvalidDateTimeIntervalException;
import it.gov.pagopa.pu.classification.service.ClassificationService;
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

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataExportsControllerTest {

  @Mock
  private ClassificationService classificationServiceMock;

  private DataExportsController controller;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();
  private final Integer maxMonthsInterval = 1;

  @BeforeEach
  void setUp() {
    controller = new DataExportsController(maxMonthsInterval, classificationServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(classificationServiceMock);
  }

  @Test
  void exportClassificationsReturnsPagedClassificationViewOnValidInterval() {
    Long organizationId = 1L;
    String operatorExternalUserId = "operator123";
    ExportClassificationsFilterDTO filterDTO = podamFactory.manufacturePojo(ExportClassificationsFilterDTO.class);

    Pageable pageable = PageRequest.of(0, 10);

    PagedClassificationView expectedView = podamFactory.manufacturePojo(PagedClassificationView.class);
    lenient().when(classificationServiceMock.getPagedClassificationView(
      organizationId,
      operatorExternalUserId,
      filterDTO,
      pageable,
      null)
    ).thenReturn(expectedView);

    ResponseEntity<PagedClassificationView> response = controller.exportClassifications(
      organizationId,
      operatorExternalUserId,
      filterDTO.getLabel(),
      filterDTO.getIuf(),
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
      filterDTO.getRegulationUniqueIdentifier(),
      filterDTO.getAccountRegistryCode(),
      filterDTO.getBillAmountCents(),
      filterDTO.getRemittanceInformation(),
      filterDTO.getPspCompanyName(),
      filterDTO.getPspLastName(),
      pageable
    );

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(expectedView, response.getBody());

    verify(classificationServiceMock, times(1)).getPagedClassificationView(
      organizationId,
      operatorExternalUserId,
      filterDTO,
      pageable,
      null
    );
  }

  @Test
  void exportClassificationsThrowsInvalidDateTimeIntervalExceptionOnInvalidInterval() {
    OffsetDateTime now = OffsetDateTime.now();
    Pageable pageable = PageRequest.of(0, 10);

    OffsetDateTime invalidFrom = now.minusMonths(2);
    OffsetDateTime invalidTo = now;

    assertThrows(InvalidDateTimeIntervalException.class, () ->
      controller.exportClassifications(
        1L,
        "operator123",
        "label",
        "iuf_value",
        "iud_value",
        "iuv_value",
        "iur_value",
        invalidFrom,
        invalidTo,
        invalidFrom,
        invalidTo,
        invalidFrom,
        invalidTo,
        invalidFrom,
        invalidTo,
        invalidFrom,
        invalidTo,
        invalidFrom,
        invalidTo,
        "regUniqueId",
        "accRegistryCode",
        1000L,
        "remittanceInfo",
        "pspCompany",
        "pspLastName",
        pageable
      )
    );
  }
}
