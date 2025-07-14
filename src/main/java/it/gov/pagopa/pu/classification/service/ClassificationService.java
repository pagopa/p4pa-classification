package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.*;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface ClassificationService {
  PagedClassificationView getPagedClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken);

  PagedFullClassificationView getPagedFullClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken);

  PagedTreasuredClassification getPagedTreasuredClassification(Long organizationId, TreasuredClassificationFilterDTO treasuredClassificationFilterDTO, Pageable pageable);

  ClassificationDetailViewDTO getClassificationDetailView(Long organizationId, Long classificationId);

  PagedClassificationPaidInstallmentsView getPaidInstallmentsView(Long organizationId, String iuv, OffsetDateTimeIntervalFilter paymentDateTimeIntervalFilter, LocalDateTimeIntervalFilter updateDateTimeIntervalFilter, Set<String> iuds, Pageable pageable);
}
