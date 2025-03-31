package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.dto.PIIDTO;

public interface NoPIIEntity<P extends PIIDTO> {
  void setPersonalDataId(Long personalDataId);
  Long getPersonalDataId();
}
