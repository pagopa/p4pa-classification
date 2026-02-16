package it.gov.pagopa.pu.classification.repository.view.classification;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;

public interface ClassificationDetailViewPIIRepository {
  ClassificationDetailViewDTO getClassificationDetailView(Long organizationId, Long classificationId);
}
