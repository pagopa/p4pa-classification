package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.debtposition.dto.generated.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class PaymentNotificationPIIDTO implements PIIDTO {
  private Person debtor;
}
