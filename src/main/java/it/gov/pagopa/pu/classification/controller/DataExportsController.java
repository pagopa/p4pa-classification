package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.DataExportsApi;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
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
  public ResponseEntity<PagedClassificationView> exportClassifications(Long organizationId,
                                                                       String operatorExternalUserId,
                                                                       ClassificationsEnum label,
                                                                       OffsetDateTime lastClassificationDateFrom,
                                                                       OffsetDateTime lastClassificationDateTo,
                                                                       String iuf, String iud,
                                                                       String iuv, String iur,
                                                                       OffsetDateTime payDateFrom,
                                                                       OffsetDateTime payDateTo,
                                                                       OffsetDateTime paymentDateTimeFrom,
                                                                       OffsetDateTime paymentDateTimeTo,
                                                                       OffsetDateTime regulationDateFrom,
                                                                       OffsetDateTime regulationDateTo,
                                                                       OffsetDateTime billDateFrom,
                                                                       OffsetDateTime billDateTo,
                                                                       OffsetDateTime regionValueDateFrom,
                                                                       OffsetDateTime regionValueDateTo,
                                                                       String regulationUniqueIdentifier,
                                                                       String accountRegistryCode,
                                                                       Long billAmountCents,
                                                                       String remittanceInformation,
                                                                       String pspCompanyName,
                                                                       String pspLastName,
                                                                       Pageable pageable) {
    String accessToken = SecurityUtils.getAccessToken();
    OffsetDateTimeIntervalFilter lastClassificationDate = validateInterval(lastClassificationDateFrom, lastClassificationDateTo);
    OffsetDateTimeIntervalFilter payDate = new OffsetDateTimeIntervalFilter(payDateFrom, payDateTo);
    OffsetDateTimeIntervalFilter paymentDate = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);
    OffsetDateTimeIntervalFilter regulationDate = new OffsetDateTimeIntervalFilter(regulationDateFrom, regulationDateTo);
    OffsetDateTimeIntervalFilter billDate = new OffsetDateTimeIntervalFilter(billDateFrom, billDateTo);
    OffsetDateTimeIntervalFilter regionValueDate = new OffsetDateTimeIntervalFilter(regionValueDateFrom, regionValueDateTo);

    ExportClassificationsFilterDTO exportClassificationsFilterDTO =
      buildExportClassificationsFilterDTO(label, iuf, iud, iuv, iur, lastClassificationDate, payDate, paymentDate, regulationDate, billDate, regionValueDate, regulationUniqueIdentifier, accountRegistryCode, billAmountCents, remittanceInformation, pspCompanyName, pspLastName);

    return ResponseEntity.ok(classificationService.getPagedClassificationView(organizationId, operatorExternalUserId, exportClassificationsFilterDTO, pageable, accessToken));
  }

  @Override
  public ResponseEntity<PagedFullClassificationView> exportFullClassifications(Long organizationId,
                                                                               String operatorExternalUserId,
                                                                               ClassificationsEnum  label,
                                                                               OffsetDateTime lastClassificationDateFrom,
                                                                               OffsetDateTime lastClassificationDateTo,
                                                                               String iuf, String iud,
                                                                               String iuv, String iur,
                                                                               OffsetDateTime payDateFrom,
                                                                               OffsetDateTime payDateTo,
                                                                               OffsetDateTime paymentDateTimeFrom,
                                                                               OffsetDateTime paymentDateTimeTo,
                                                                               OffsetDateTime regulationDateFrom,
                                                                               OffsetDateTime regulationDateTo,
                                                                               OffsetDateTime billDateFrom,
                                                                               OffsetDateTime billDateTo,
                                                                               OffsetDateTime regionValueDateFrom,
                                                                               OffsetDateTime regionValueDateTo,
                                                                               String regulationUniqueIdentifier,
                                                                               String accountRegistryCode,
                                                                               Long billAmountCents,
                                                                               String remittanceInformation,
                                                                               String pspCompanyName,
                                                                               String pspLastName,
                                                                               Pageable pageable) {
    String accessToken = SecurityUtils.getAccessToken();
    OffsetDateTimeIntervalFilter lastClassificationDate = validateInterval(lastClassificationDateFrom, lastClassificationDateTo);
    OffsetDateTimeIntervalFilter payDate = new OffsetDateTimeIntervalFilter(payDateFrom, payDateTo);
    OffsetDateTimeIntervalFilter paymentDate = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);
    OffsetDateTimeIntervalFilter regulationDate = new OffsetDateTimeIntervalFilter(regulationDateFrom, regulationDateTo);
    OffsetDateTimeIntervalFilter billDate = new OffsetDateTimeIntervalFilter(billDateFrom, billDateTo);
    OffsetDateTimeIntervalFilter regionValueDate = new OffsetDateTimeIntervalFilter(regionValueDateFrom, regionValueDateTo);

    ExportClassificationsFilterDTO exportClassificationsFilterDTO =
      buildExportClassificationsFilterDTO(label, iuf, iud, iuv, iur, lastClassificationDate, payDate, paymentDate, regulationDate, billDate, regionValueDate, regulationUniqueIdentifier, accountRegistryCode, billAmountCents, remittanceInformation, pspCompanyName, pspLastName);

    return ResponseEntity.ok(classificationService.getPagedFullClassificationView(organizationId, operatorExternalUserId, exportClassificationsFilterDTO, pageable, accessToken));
  }

  private OffsetDateTimeIntervalFilter validateInterval(OffsetDateTime dateFrom, OffsetDateTime dateTo) {
    if (!isValidIntervalBetweenOffsetDateTime(dateFrom, dateTo, ChronoUnit.MONTHS, maxMonthsInterval)) {
      throw new InvalidDateTimeIntervalException("The date interval between %s and %s cannot exceed %d months".formatted(dateFrom, dateTo, maxMonthsInterval));
    }
    return OffsetDateTimeIntervalFilter.builder()
      .from(dateFrom)
      .to(dateTo)
      .build();
  }

  @SuppressWarnings("squid:S107")
  private ExportClassificationsFilterDTO buildExportClassificationsFilterDTO(ClassificationsEnum  label, String iuf, String iud,
                                                                             String iuv, String iur,
                                                                             OffsetDateTimeIntervalFilter lastClassificationDate,
                                                                             OffsetDateTimeIntervalFilter payDate,
                                                                             OffsetDateTimeIntervalFilter paymentDate,
                                                                             OffsetDateTimeIntervalFilter regulationDate,
                                                                             OffsetDateTimeIntervalFilter billDate,
                                                                             OffsetDateTimeIntervalFilter regionValueDate,
                                                                             String regulationUniqueIdentifier,
                                                                             String accountRegistryCode,
                                                                             Long billAmountCents,
                                                                             String remittanceInformation,
                                                                             String pspCompanyName,
                                                                             String pspLastName) {
    return ExportClassificationsFilterDTO.builder()
      .label(label)
      .iuf(iuf)
      .iud(iud)
      .iuv(iuv)
      .iur(iur)
      .lastClassificationDate(lastClassificationDate)
      .payDate(payDate)
      .paymentDateTime(paymentDate)
      .regulationDate(regulationDate)
      .billDate(billDate)
      .regionValueDate(regionValueDate)
      .regulationUniqueIdentifier(regulationUniqueIdentifier)
      .accountRegistryCode(accountRegistryCode)
      .billAmountCents(billAmountCents)
      .remittanceInformation(remittanceInformation)
      .pspCompanyName(pspCompanyName)
      .pspLastName(pspLastName)
      .build();
  }
}
