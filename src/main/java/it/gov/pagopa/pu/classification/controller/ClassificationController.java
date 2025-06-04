package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.ClassificationsApi;
import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.TreasuredClassificationFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import it.gov.pagopa.pu.classification.util.DateConversionUtils;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.LocalDateIntervalFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@RestController
@Slf4j
public class ClassificationController implements ClassificationsApi {

  private final ClassificationService classificationService;

  public ClassificationController(ClassificationService classificationService) {
    this.classificationService = classificationService;
  }

  @Override
  public ResponseEntity<PagedTreasuredClassification> getTreasuredClassifications(
    Long organizationId, ClassificationsEnum label, String iud, String iuv, String iur,
    LocalDate lastClassificationDateFrom, LocalDate lastClassificationDateTo,
    OffsetDateTime payDateFrom, OffsetDateTime payDateTo, OffsetDateTime paymentDateTimeFrom,
    OffsetDateTime paymentDateTimeTo, LocalDate regulationDateFrom, LocalDate regulationDateTo,
    LocalDate billDateFrom, LocalDate billDateTo, LocalDate regionValueDateFrom,
    LocalDate regionValueDateTo, String pspCompanyName, String pspLastName, String iuf,
    String regulationUniqueIdentifier, String accountRegistryCode, Long billAmountCents,
    String remittanceInformation, String debtorFiscalCode, String debtPositionTypeOrgCode,
    String billYear, String billCode, String documentYear, String documentCode,
    String provisionalAe, String provisionalCode, Pageable pageable) {
    log.info("Retrieving Classifications having organizationId: {}",
      organizationId);

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

    TreasuredClassificationFilterDTO filter = TreasuredClassificationFilterDTO.builder()
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
      .debtorFiscalCode(debtorFiscalCode)
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .billYear(billYear)
      .billCode(billCode)
      .documentYear(documentYear)
      .documentCode(documentCode)
      .provisionalAe(provisionalAe)
      .provisionalCode(provisionalCode)
      .build();

    return ResponseEntity.ok(
      classificationService.getPagedTreasuredClassification(organizationId,
        filter,
        pageable));
  }

  @Override
  public ResponseEntity<ClassificationDetailViewDTO> getClassificationDetail(
    Long organizationId, Long classificationId) {
    return ResponseEntity.ok(
      classificationService.getClassificationDetailView(organizationId,
        classificationId));
  }

}
