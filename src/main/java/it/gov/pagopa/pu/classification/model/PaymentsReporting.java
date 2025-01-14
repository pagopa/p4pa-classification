package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.config.semanticids.PaymentsReportingSemanticIdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
  private Long ingestionFlowFileId;
  private Long organizationId;
  private String iuv;
  private String iur;
  private Integer transferIndex;
  private String pspIdentifier;
  private String iuf;
  private LocalDateTime flowDateTime;
  private String regulationUniqueIdentifier;
  private LocalDate regulationDate;
  private String senderPspType;
  private String senderPspCode;
  private String senderPspName;
  private String receiverOrganizationType;
  private String receiverOrganizationCode;
  private String receiverOrganizationName;
  private Long totalPayments;
  private Long totalAmountCents;
  private Long amountPaidCents;
  private String paymentOutcomeCode;
  private LocalDate payDate;
  private LocalDate acquiringDate;
  private String bicCodePouringBank;
}
