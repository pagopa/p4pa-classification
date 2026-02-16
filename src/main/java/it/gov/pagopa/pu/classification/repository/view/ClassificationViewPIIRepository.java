package it.gov.pagopa.pu.classification.repository.view;

import it.gov.pagopa.pu.classification.dto.filters.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import org.springframework.data.domain.Pageable;

public interface ClassificationViewPIIRepository {
  PagedClassificationView getPagedClassificationView(Long organizationId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable);
}
