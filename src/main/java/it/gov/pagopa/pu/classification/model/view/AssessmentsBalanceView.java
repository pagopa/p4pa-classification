package it.gov.pagopa.pu.classification.model.view;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "assessments_detail")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AssessmentsBalanceView implements Serializable {
  @Id
  private String officeCode;
  private String debtPositionTypeOrgCode;
  private String sectionCode;
  private String assessmentCode;
  private Long amountCents;
}
