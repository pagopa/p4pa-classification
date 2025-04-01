package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;

import java.time.OffsetDateTime;

public interface ClassificationService {
  PagedClassificationView getPagedClassificationView(Long organizationId, String operatorExternalUserId, String label, String iuf, String iud, String iuv, String iur, OffsetDateTime lastClassificationDateFrom, OffsetDateTime lastClassificationDateTo, OffsetDateTime payDateFrom, OffsetDateTime payDateTo, OffsetDateTime paymentDateTimeFrom, OffsetDateTime paymentDateTimeTo, OffsetDateTime regulationDateFrom, OffsetDateTime regulationDateTo, OffsetDateTime billDateFrom, OffsetDateTime billDateTo, OffsetDateTime regionValueDateFrom, OffsetDateTime regionValueDateTo, String regulationUniqueIdentifier, String accountRegistryCode, Long billAmountCents, String remittanceInformation, String pspCompanyName, String pspLastName);
}
