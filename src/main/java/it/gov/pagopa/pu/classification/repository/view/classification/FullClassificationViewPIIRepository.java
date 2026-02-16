package it.gov.pagopa.pu.classification.repository.view.classification;

import it.gov.pagopa.pu.classification.dto.filters.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import org.springframework.data.domain.Pageable;

public interface FullClassificationViewPIIRepository {
  PagedFullClassificationView getPagedFullClassificationView(Long organizationId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable);
}
