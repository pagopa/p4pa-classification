package it.gov.pagopa.pu.classification.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.OffsetDateTime;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationDTO {

  private String iuf;
  private String iud;
  private String iuv;
  private String iur;
  @NotNull
  private String label;

  private OffsetDateTime lastClassificationDateFrom;
  private OffsetDateTime lastClassificationDateTo;

  private OffsetDateTime payDateFrom;
  private OffsetDateTime payDateTo;

  private OffsetDateTime paymentDateTimeFrom;
  private OffsetDateTime paymentDateTimeTo;

  private OffsetDateTime regulationDateFrom;
  private OffsetDateTime regulationDateTo;

  private OffsetDateTime billDateFrom;
  private OffsetDateTime billDateTo;

  private OffsetDateTime regionValueDateFrom;
  private OffsetDateTime regionValueDateTo;

  private String regulationUniqueIdentifier;
  private String accountRegistryCode;
  private Long billAmountCents;
  private String remittanceInformation;
  private String pspCompanyName;
  private String pspLastName;
}
