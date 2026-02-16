package it.gov.pagopa.pu.classification.model.view.classification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationPaidInstallmentsViewId {
  private String iud;
  private String iuv;
  private OffsetDateTime paymentDateTime;
  private OffsetDateTime receiptCreationDate;
  private String receiptPaymentRequestId;
}
