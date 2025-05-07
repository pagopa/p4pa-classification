package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.ClassificationsApi;
import it.gov.pagopa.pu.classification.dto.ClassificationListFilterDTO;
import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationListDTO;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import it.gov.pagopa.pu.classification.util.DateConversionUtils;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.LocalDateIntervalFilter;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClassificationController implements ClassificationsApi {

  private final ClassificationService classificationService;

  public ClassificationController(ClassificationService classificationService) {
    this.classificationService = classificationService;
  }

  @Override
  public ResponseEntity<PagedClassificationListDTO> getClassifications(
    Long organizationId,
    ClassificationsEnum label,
    LocalDate lastClassificationDateFrom, LocalDate lastClassificationDateTo,
    String iud, String iuv, String iur, OffsetDateTime payDateFrom,
    OffsetDateTime payDateTo, OffsetDateTime paymentDateTimeFrom,
    OffsetDateTime paymentDateTimeTo, LocalDate regulationDateFrom,
    LocalDate regulationDateTo, LocalDate billDateFrom, LocalDate billDateTo,
    LocalDate regionValueDateFrom, LocalDate regionValueDateTo,
    String pspCompanyName, String pspLastName, String iuf,
    String regulationUniqueIdentifier, String accountRegistryCode,
    Long billAmountCents, String remittanceInformation, Pageable pageable) {

    LocalDateIntervalFilter lastClassificationDate = new LocalDateIntervalFilter(
      lastClassificationDateFrom, lastClassificationDateTo);
    LocalDateTimeIntervalFilter payDate = new LocalDateTimeIntervalFilter(
      DateConversionUtils.offsetDateTime2LocalDateTime(payDateFrom),
      DateConversionUtils.offsetDateTime2LocalDateTime(payDateTo));
    OffsetDateTimeIntervalFilter paymentDate = new OffsetDateTimeIntervalFilter(
      paymentDateTimeFrom, paymentDateTimeTo);
    LocalDateIntervalFilter regulationDate = new LocalDateIntervalFilter(
      regulationDateFrom, regulationDateTo);
    LocalDateIntervalFilter billDate = new LocalDateIntervalFilter(billDateFrom,
      billDateTo);
    LocalDateIntervalFilter regionValueDate = new LocalDateIntervalFilter(
      regionValueDateFrom, regionValueDateTo);

    ClassificationListFilterDTO filter = buildClassificationListFilterDTO(label,
      iud, iuv, iur, lastClassificationDate, payDate, paymentDate,
      regulationDate, billDate, regionValueDate, pspCompanyName, pspLastName,
      iuf, regulationUniqueIdentifier, accountRegistryCode, billAmountCents,
      remittanceInformation);

    return ResponseEntity.ok(
      classificationService.getPagedClassificationList(organizationId, filter,
        pageable));
  }

  private ClassificationListFilterDTO buildClassificationListFilterDTO(
    ClassificationsEnum label,
    String iud, String iuv, String iur,
    LocalDateIntervalFilter lastClassificationDate,
    LocalDateTimeIntervalFilter payDate,
    OffsetDateTimeIntervalFilter paymentDate,
    LocalDateIntervalFilter regulationDate,
    LocalDateIntervalFilter billDate,
    LocalDateIntervalFilter regionValueDate,
    String pspCompanyName, String pspLastName, String iuf,
    String regulationUniqueIdentifier, String accountRegistryCode,
    Long billAmountCents, String remittanceInformation) {
    return ClassificationListFilterDTO.builder()
      .label(label)
      .iud(iud)
      .iuv(iuv)
      .iur(iur)
      .lastClassificationDate(lastClassificationDate)
      .payDate(payDate)
      .paymentDateTime(paymentDate)
      .regulationDate(regulationDate)
      .billDate(billDate)
      .regionValueDate(regionValueDate)
      .pspCompanyName(pspCompanyName)
      .pspLastName(pspLastName)
      .iuf(iuf)
      .regulationUniqueIdentifier(regulationUniqueIdentifier)
      .accountRegistryCode(accountRegistryCode)
      .billAmountCents(billAmountCents)
      .remittanceInformation(remittanceInformation)
      .build();
  }
}
