package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.ClassificationListFilterDTO;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationListDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import org.springframework.data.domain.Pageable;

public interface ClassificationService {
  PagedClassificationView getPagedClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken);
  PagedFullClassificationView getPagedFullClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken);
  PagedClassificationListDTO getPagedClassificationList(Long organizationId, ClassificationListFilterDTO classificationListFilterDTO, Pageable pageable);
}
