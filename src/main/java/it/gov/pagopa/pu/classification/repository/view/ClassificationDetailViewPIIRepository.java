package it.gov.pagopa.pu.classification.repository.view;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;

public interface ClassificationDetailViewPIIRepository {
  ClassificationDetailViewDTO getClassificationDetailView(Long organizationId, Long classificationId);
}
