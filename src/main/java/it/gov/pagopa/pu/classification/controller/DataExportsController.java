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
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.LocalDateIntervalFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static it.gov.pagopa.pu.classification.util.Utilities.isValidIntervalBetweenLocalDate;

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
                                                                       Set<ClassificationsEnum> label,
                                                                       LocalDate lastClassificationDateFrom,
                                                                       LocalDate lastClassificationDateTo,
                                                                       List<String> iuf,
                                                                       String iud,
                                                                       List<String> iuv,
                                                                       List<String> iur,
                                                                       LocalDate payDateFrom,
                                                                       LocalDate payDateTo,
                                                                       OffsetDateTime paymentDateTimeFrom,
                                                                       OffsetDateTime paymentDateTimeTo,
                                                                       LocalDate regulationDateFrom,
                                                                       LocalDate regulationDateTo,
                                                                       LocalDate billDateFrom,
                                                                       LocalDate billDateTo,
                                                                       LocalDate regionValueDateFrom,
                                                                       LocalDate regionValueDateTo,
                                                                       String regulationUniqueIdentifier,
                                                                       String accountRegistryCode,
                                                                       Long billAmountCents,
                                                                       String remittanceInformation,
                                                                       String pspCompanyName,
                                                                       String pspLastName,
                                                                       Set<String> debtPositionTypeOrgCodes,
                                                                       Pageable pageable) {
    String accessToken = SecurityUtils.getAccessToken();
    LocalDateIntervalFilter lastClassificationDate = validateInterval(lastClassificationDateFrom, lastClassificationDateTo);
    LocalDateIntervalFilter payDate = new LocalDateIntervalFilter(payDateFrom, payDateTo);
    OffsetDateTimeIntervalFilter paymentDate = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);
    LocalDateIntervalFilter regulationDate = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);
    LocalDateIntervalFilter billDate = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDate = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    ExportClassificationsFilterDTO exportClassificationsFilterDTO =
      buildExportClassificationsFilterDTO(label, lastClassificationDate, iuf, iud, iuv, iur, payDate, paymentDate, regulationDate, billDate, regionValueDate, regulationUniqueIdentifier, accountRegistryCode, billAmountCents, remittanceInformation, pspCompanyName, pspLastName, debtPositionTypeOrgCodes);

    return ResponseEntity.ok(classificationService.getPagedClassificationView(organizationId, operatorExternalUserId, exportClassificationsFilterDTO, pageable, accessToken));
  }

  @Override
  public ResponseEntity<PagedFullClassificationView> exportFullClassifications(Long organizationId,
                                                                               String operatorExternalUserId,
                                                                               Set<ClassificationsEnum> label,
                                                                               LocalDate lastClassificationDateFrom,
                                                                               LocalDate lastClassificationDateTo,
                                                                               List<String> iuf,
                                                                               String iud,
                                                                               List<String> iuv,
                                                                               List<String> iur,
                                                                               LocalDate payDateFrom,
                                                                               LocalDate payDateTo,
                                                                               OffsetDateTime paymentDateTimeFrom,
                                                                               OffsetDateTime paymentDateTimeTo,
                                                                               LocalDate regulationDateFrom,
                                                                               LocalDate regulationDateTo,
                                                                               LocalDate billDateFrom,
                                                                               LocalDate billDateTo,
                                                                               LocalDate regionValueDateFrom,
                                                                               LocalDate regionValueDateTo,
                                                                               String regulationUniqueIdentifier,
                                                                               String accountRegistryCode,
                                                                               Long billAmountCents,
                                                                               String remittanceInformation,
                                                                               String pspCompanyName,
                                                                               String pspLastName,
                                                                               Set<String> debtPositionTypeOrgCodes,
                                                                               Pageable pageable) {
    String accessToken = SecurityUtils.getAccessToken();
    LocalDateIntervalFilter lastClassificationDate = validateInterval(lastClassificationDateFrom, lastClassificationDateTo);
    LocalDateIntervalFilter payDate = new LocalDateIntervalFilter(payDateFrom, payDateTo);
    OffsetDateTimeIntervalFilter paymentDate = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);
    LocalDateIntervalFilter regulationDate = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);
    LocalDateIntervalFilter billDate = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDate = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    ExportClassificationsFilterDTO exportClassificationsFilterDTO =
      buildExportClassificationsFilterDTO(label, lastClassificationDate, iuf, iud, iuv, iur, payDate, paymentDate, regulationDate, billDate, regionValueDate, regulationUniqueIdentifier, accountRegistryCode, billAmountCents, remittanceInformation, pspCompanyName, pspLastName, debtPositionTypeOrgCodes);

    return ResponseEntity.ok(classificationService.getPagedFullClassificationView(organizationId, operatorExternalUserId, exportClassificationsFilterDTO, pageable, accessToken));
  }

  private LocalDateIntervalFilter validateInterval(LocalDate dateFrom, LocalDate dateTo) {
    if (!isValidIntervalBetweenLocalDate(dateFrom, dateTo, ChronoUnit.MONTHS, maxMonthsInterval)) {
      throw new InvalidDateTimeIntervalException("The date interval between %s and %s cannot exceed %d months".formatted(dateFrom, dateTo, maxMonthsInterval));
    }
    return LocalDateIntervalFilter.builder()
      .from(dateFrom)
      .to(dateTo)
      .build();
  }

  @SuppressWarnings("squid:S107")
  private ExportClassificationsFilterDTO buildExportClassificationsFilterDTO(Set<ClassificationsEnum> label,
                                                                             LocalDateIntervalFilter lastClassificationDate,
                                                                             List<String> iuf,
                                                                             String iud,
                                                                             List<String> iuv,
                                                                             List<String> iur,
                                                                             LocalDateIntervalFilter payDate,
                                                                             OffsetDateTimeIntervalFilter paymentDateTime,
                                                                             LocalDateIntervalFilter regulationDate,
                                                                             LocalDateIntervalFilter billDate,
                                                                             LocalDateIntervalFilter regionValueDate,
                                                                             String regulationUniqueIdentifier,
                                                                             String accountRegistryCode,
                                                                             Long billAmountCents,
                                                                             String remittanceInformation,
                                                                             String pspCompanyName,
                                                                             String pspLastName,
                                                                             Set<String> debtPositionTypeOrgCodes) {
    return ExportClassificationsFilterDTO.builder()
      .label(label)
      .iuf(iuf)
      .iud(iud)
      .iuv(iuv)
      .iur(iur)
      .lastClassificationDate(lastClassificationDate)
      .payDate(payDate)
      .paymentDateTime(paymentDateTime)
      .regulationDate(regulationDate)
      .billDate(billDate)
      .regionValueDate(regionValueDate)
      .regulationUniqueIdentifier(regulationUniqueIdentifier)
      .accountRegistryCode(accountRegistryCode)
      .billAmountCents(billAmountCents)
      .remittanceInformation(remittanceInformation)
      .pspCompanyName(pspCompanyName)
      .pspLastName(pspLastName)
      .debtPositionTypeOrgCodes(debtPositionTypeOrgCodes)
      .build();
  }
}
