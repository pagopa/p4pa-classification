package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.ClassificationPaidInstallmentsFilterDTO;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.TreasuredClassificationFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.model.Classification;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ClassificationService {
  PagedClassificationView getPagedClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken);

  PagedFullClassificationView getPagedFullClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken);

  PagedTreasuredClassification getPagedTreasuredClassification(Long organizationId, TreasuredClassificationFilterDTO treasuredClassificationFilterDTO, Pageable pageable);

  ClassificationDetailViewDTO getClassificationDetailView(Long organizationId, Long classificationId);

  PagedClassificationPaidInstallmentsView getPaidInstallmentsView(Long organizationId, ClassificationPaidInstallmentsFilterDTO filter, Pageable pageable);

  List<Classification> saveAll(List<Classification> classifications);
}
