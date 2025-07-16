package it.gov.pagopa.pu.classification.model.view;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "classification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ClassificationPaidInstallmentsView.class)
public class ClassificationPaidInstallmentsView {
  @Id
  private String iud;
  @Id
  private String iuv;
  @Id
  private OffsetDateTime paymentDateTime;
  @Id
  @NotNull
  private LocalDateTime updateDate;
  @Id
  private String receiptPaymentRequestId;
  @NotNull
  private Long organizationId;
  private String debtPositionTypeOrgCode;
  private Long amount;
}
