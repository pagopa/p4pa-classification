package it.gov.pagopa.pu.classification.repository.view;

import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FullClassificationViewPIIRepository {
  PagedFullClassificationView getPagedFullClassificationView(Long organizationId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, List<String> debtPositionTypeOrgCodes, Pageable pageable);
}
