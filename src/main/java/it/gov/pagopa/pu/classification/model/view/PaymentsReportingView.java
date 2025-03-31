package it.gov.pagopa.pu.classification.model.view;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments_reporting")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@IdClass(PaymentsReportingView.class)
public class PaymentsReportingView implements Serializable {

  @NotNull
  private Long ingestionFlowFileId;
  @NotNull
  private Long organizationId;
  @Id
  @NotNull
  private String iuf;
  @Id
  @NotNull
  private String regulationUniqueIdentifier;
  @NotNull
  private LocalDate regulationDate;
  @NotNull
  private LocalDateTime flowDateTime;
  @NotNull
  private Long totalPayments;
  @NotNull
  private Long totalAmountCents;

}
