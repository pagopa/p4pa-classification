package it.gov.pagopa.pu.classification.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "assessments_registry")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class AssessmentsRegistry extends BaseEntity implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assessments_registry_generator")
  @SequenceGenerator(name = "assessments_registry_generator", sequenceName = "assessment_registry_id_seq", allocationSize = 1)
  private Long assessmentRegistryId;
  @NotNull
  private Long organizationId;
  @NotNull
  private String debtPositionTypeOrgCode;
  @NotNull
  private String sectionCode;
  @NotNull
  private String sectionDescription;
  @NotNull
  private String officeCode;
  @NotNull
  private String officeDescription;
  @NotNull
  private String assessmentCode;
  @NotNull
  private String assessmentDescription;
  @NotNull
  private String operatingYear;
  @NotNull
  private String status;
}
