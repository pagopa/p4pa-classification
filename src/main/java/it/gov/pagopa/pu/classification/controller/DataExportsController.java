package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.DataExportsApi;
import it.gov.pagopa.pu.classification.dto.filters.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.filters.OffsetDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.service.ClassificationService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.LocalDateIntervalFilter;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@RestController
public class DataExportsController implements DataExportsApi {
  private final ClassificationService classificationService;

  public DataExportsController(ClassificationService classificationService) {
    this.classificationService = classificationService;
  }

  @Override
  public ResponseEntity<PagedClassificationView> exportClassifications(Long organizationId,
                                                                       String operatorExternalUserId,
                                                                       Set<ClassificationsEnum> label,
                                                                       List<String> iufs,
                                                                       String iud,
                                                                       List<String> iuv,
                                                                       List<String> iur,
                                                                       LocalDate lastClassificationDateFrom,
                                                                       LocalDate lastClassificationDateTo,
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
    LocalDateIntervalFilter lastClassificationDate = new LocalDateIntervalFilter(lastClassificationDateFrom, lastClassificationDateTo);
    LocalDateIntervalFilter payDate = new LocalDateIntervalFilter(payDateFrom, payDateTo);
    OffsetDateTimeIntervalFilter paymentDate = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);
    LocalDateIntervalFilter regulationDate = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);
    LocalDateIntervalFilter billDate = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDate = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    ExportClassificationsFilterDTO exportClassificationsFilterDTO =
      buildExportClassificationsFilterDTO(label, lastClassificationDate, iufs, iud, iuv, iur, payDate, paymentDate, regulationDate, billDate, regionValueDate, regulationUniqueIdentifier, accountRegistryCode, billAmountCents, remittanceInformation, pspCompanyName, pspLastName, debtPositionTypeOrgCodes);

    return ResponseEntity.ok(classificationService.getPagedClassificationView(organizationId, operatorExternalUserId, exportClassificationsFilterDTO, pageable, accessToken));
  }

  @Override
  public ResponseEntity<PagedFullClassificationView> exportFullClassifications(Long organizationId,
                                                                               String operatorExternalUserId,
                                                                               Set<ClassificationsEnum> label,
                                                                               List<String> iufs,
                                                                               String iud,
                                                                               List<String> iuv,
                                                                               List<String> iur,
                                                                               LocalDate lastClassificationDateFrom,
                                                                               LocalDate lastClassificationDateTo,
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
    LocalDateIntervalFilter lastClassificationDate = new LocalDateIntervalFilter(lastClassificationDateFrom, lastClassificationDateTo);
    LocalDateIntervalFilter payDate = new LocalDateIntervalFilter(payDateFrom, payDateTo);
    OffsetDateTimeIntervalFilter paymentDate = new OffsetDateTimeIntervalFilter(paymentDateTimeFrom, paymentDateTimeTo);
    LocalDateIntervalFilter regulationDate = new LocalDateIntervalFilter(regulationDateFrom, regulationDateTo);
    LocalDateIntervalFilter billDate = new LocalDateIntervalFilter(billDateFrom, billDateTo);
    LocalDateIntervalFilter regionValueDate = new LocalDateIntervalFilter(regionValueDateFrom, regionValueDateTo);

    ExportClassificationsFilterDTO exportClassificationsFilterDTO =
      buildExportClassificationsFilterDTO(label, lastClassificationDate, iufs, iud, iuv, iur, payDate, paymentDate, regulationDate, billDate, regionValueDate, regulationUniqueIdentifier, accountRegistryCode, billAmountCents, remittanceInformation, pspCompanyName, pspLastName, debtPositionTypeOrgCodes);

    return ResponseEntity.ok(classificationService.getPagedFullClassificationView(organizationId, operatorExternalUserId, exportClassificationsFilterDTO, pageable, accessToken));
  }

  @SuppressWarnings("squid:S107")
  private ExportClassificationsFilterDTO buildExportClassificationsFilterDTO(Set<ClassificationsEnum> label,
                                                                             LocalDateIntervalFilter lastClassificationDate,
                                                                             List<String> iufs,
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
      .iufs(iufs)
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
