package it.gov.pagopa.pu.classification.event.dto;

import it.gov.pagopa.pu.classification.dto.PaymentAssessmentsDataDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class PaymentAssessmentsDataEventDTO extends DataEventDTO<PaymentAssessmentsDataDTO>{

}
