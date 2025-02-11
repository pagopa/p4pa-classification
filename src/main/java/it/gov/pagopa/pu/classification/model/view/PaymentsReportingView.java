package it.gov.pagopa.pu.classification.model.view;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
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
  private Long ingestionFlowFileId;
  private Long organizationId;
  @Id
  private String iuf;
  @Id
  private String regulationUniqueIdentifier;
  private LocalDate regulationDate;
  private LocalDateTime flowDateTime;
  private Long totalPayments;
  private Long totalAmountCents;
}
