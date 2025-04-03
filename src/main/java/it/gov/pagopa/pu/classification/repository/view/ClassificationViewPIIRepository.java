package it.gov.pagopa.pu.classification.repository.view;

import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClassificationViewPIIRepository {
  PagedClassificationView getPagedClassificationView(Long organizationId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, List<String> debtPositionTypeOrgCodes, Pageable pageable);
}
