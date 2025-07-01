package it.gov.pagopa.pu.classification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Table(name = "assessments_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class AssessmentsDetail extends BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assessments_detail_generator")
  @SequenceGenerator(name = "assessments_detail_generator", sequenceName = "assessment_detail_id_seq", allocationSize = 1)
  private Long assessmentDetailId;
  @NotNull
  private Long assessmentId;
  @NotNull
  private Long organizationId;
  @NotNull
  private String debtPositionTypeOrgCode;
  @NotNull
  private String iuv;
  @NotNull
  private String iud;
  @NotNull
  private String iur;
  @NotNull
  private byte[] debtorFiscalCodeHash;
  private OffsetDateTime paymentDateTime;
  private String officeCode;
  @NotNull
  private String sectionCode;
  private String assessmentCode;
  @NotNull
  private Long amountCents;
  @NotNull
  private boolean amountSubmitted = true;
  private Long receiptId;
}
