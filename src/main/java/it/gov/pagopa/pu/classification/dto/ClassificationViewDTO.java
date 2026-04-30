package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.classification.dto.pii.ReceiptPIIDTO;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationViewNoPII;
import it.gov.pagopa.pu.common.pii.dto.FullPIIDTO;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class ClassificationViewDTO extends BaseClassificationViewDTO implements FullPIIDTO<ClassificationViewNoPII, ReceiptPIIDTO> {
}
