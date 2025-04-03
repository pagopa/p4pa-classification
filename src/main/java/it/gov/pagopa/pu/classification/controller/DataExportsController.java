package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.DataExportsApi;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.exception.custom.InvalidDateTimeIntervalException;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static it.gov.pagopa.pu.classification.util.Utilities.isValidIntervalBetweenOffsetDateTime;

@RestController
public class DataExportsController implements DataExportsApi {
  private final Integer maxMonthsInterval;
  private final ClassificationService classificationService;

  public DataExportsController(@Value("${data-export.classification-view.max-months-interval}") Integer maxMonthsInterval,
                               ClassificationService classificationService) {
    this.maxMonthsInterval = maxMonthsInterval;
    this.classificationService = classificationService;
  }

  @Override
  public ResponseEntity<PagedClassificationView> exportClassifications(Long organizationId, String operatorExternalUserId, String label, String iuf, String iud, String iuv, String iur, OffsetDateTime lastClassificationDateFrom, OffsetDateTime lastClassificationDateTo, OffsetDateTime payDateFrom, OffsetDateTime payDateTo, OffsetDateTime paymentDateTimeFrom, OffsetDateTime paymentDateTimeTo, OffsetDateTime regulationDateFrom, OffsetDateTime regulationDateTo, OffsetDateTime billDateFrom, OffsetDateTime billDateTo, OffsetDateTime regionValueDateFrom, OffsetDateTime regionValueDateTo, String regulationUniqueIdentifier, String accountRegistryCode, Long billAmountCents, String remittanceInformation, String pspCompanyName, String pspLastName, Pageable pageable) {
    String accessToken = SecurityUtils.getAccessToken();
    validateInterval(lastClassificationDateFrom, lastClassificationDateTo);
    validateInterval(payDateFrom, payDateTo);
    validateInterval(paymentDateTimeFrom, paymentDateTimeTo);
    validateInterval(regulationDateFrom, regulationDateTo);
    validateInterval(billDateFrom, billDateTo);
    validateInterval(regionValueDateFrom, regionValueDateTo);

    ExportClassificationsFilterDTO exportClassificationsFilterDTO = ExportClassificationsFilterDTO.builder()
            .label(label)
            .iuf(iuf)
            .iud(iud)
            .iuv(iuv)
            .iur(iur)
            .lastClassificationDate(OffsetDateTimeIntervalFilter.builder()
                .from(lastClassificationDateFrom)
                .to(lastClassificationDateTo)
                .build())
            .payDate(OffsetDateTimeIntervalFilter.builder()
                .from(payDateFrom)
                .to(payDateTo)
                .build())
            .paymentDateTime(OffsetDateTimeIntervalFilter.builder()
                .from(paymentDateTimeFrom)
                .to(paymentDateTimeTo)
                .build())
            .regulationDate(OffsetDateTimeIntervalFilter.builder()
                .from(regulationDateFrom)
                .to(regulationDateTo)
                .build())
            .billDate(OffsetDateTimeIntervalFilter.builder()
                .from(billDateFrom)
                .to(billDateTo)
                .build())
            .regionValueDate(OffsetDateTimeIntervalFilter.builder()
                .from(regionValueDateFrom)
                .to(regionValueDateTo)
                .build())
            .regulationUniqueIdentifier(regulationUniqueIdentifier)
            .accountRegistryCode(accountRegistryCode)
            .billAmountCents(billAmountCents)
            .remittanceInformation(remittanceInformation)
            .pspCompanyName(pspCompanyName)
            .pspLastName(pspLastName)
            .build();

    return ResponseEntity.ok(classificationService.getPagedClassificationView(organizationId, operatorExternalUserId, exportClassificationsFilterDTO, pageable, accessToken));
  }

  private void validateInterval(OffsetDateTime dateFrom, OffsetDateTime dateTo) {
    if (!isValidIntervalBetweenOffsetDateTime(dateFrom, dateTo, ChronoUnit.MONTHS, maxMonthsInterval)) {
      throw new InvalidDateTimeIntervalException("The date interval between %s and %s cannot exceed %d months".formatted(dateFrom, dateTo, maxMonthsInterval));
    }
  }
}
