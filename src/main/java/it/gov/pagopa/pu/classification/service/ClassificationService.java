package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import org.springframework.data.domain.Pageable;

public interface ClassificationService {
  PagedClassificationView getPagedClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable);
}
