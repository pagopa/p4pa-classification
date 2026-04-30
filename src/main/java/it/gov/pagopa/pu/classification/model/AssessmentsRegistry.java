package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
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

  private String sectionDescription;

  private String officeCode;

  private String officeDescription;

  private String assessmentCode;

  private String assessmentDescription;
  @NotNull
  private String operatingYear;
  @NotNull
  @Enumerated(EnumType.STRING)
  private AssessmentsRegistryStatus status;
}
