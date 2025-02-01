package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.config.semanticids.PaymentsReportingSemanticIdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments_reporting")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "paymentsReportingId", callSuper = false)
public class PaymentsReporting extends BaseEntity implements Serializable{

  @Id
  @PaymentsReportingSemanticIdGenerator
  private String paymentsReportingId;
  @NotNull
  private Long ingestionFlowFileId;
  @NotNull
  private Long organizationId;
  @NotNull
  private String iuv;
  @NotNull
  private String iur;
  @NotNull
  private Integer transferIndex;
  @NotNull
  private String pspIdentifier;
  @NotNull
  private String iuf;
  @NotNull
  private LocalDateTime flowDateTime;
  @NotNull
  private String regulationUniqueIdentifier;
  @NotNull
  private LocalDate regulationDate;
  @NotNull
  private String senderPspType;
  @NotNull
  private String senderPspCode;
  private String senderPspName;
  private String receiverOrganizationType;
  private String receiverOrganizationCode;
  private String receiverOrganizationName;
  @NotNull
  private Long totalPayments;
  @NotNull
  private Long totalAmountCents;
  @NotNull
  private Long amountPaidCents;
  @NotNull
  private String paymentOutcomeCode;
  @NotNull
  private LocalDate payDate;
  @NotNull
  private LocalDate acquiringDate;
  private String bicCodePouringBank;
}
