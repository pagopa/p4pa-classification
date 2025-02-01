package it.gov.pagopa.pu.classification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "classification")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class Classification extends BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classification_generator")
  @SequenceGenerator(name = "classification_generator", sequenceName = "classification_id_seq", allocationSize = 1)
  @NotNull
  private Long classificationId;
  @NotNull
  private Long organizationId;
  private Long transferId;
  private Long paymentNotifyId;
  private String paymentsReportingId;
  private String treasuryId;
  private String iuf;
  private String iud;
  private String iuv;
  private String iur;
  private int transferIndex;
  @NotNull
  private String label;
}
