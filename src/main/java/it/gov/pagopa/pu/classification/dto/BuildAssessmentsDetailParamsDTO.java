package it.gov.pagopa.pu.classification.dto;

import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptNoPII;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
public class BuildAssessmentsDetailParamsDTO {
  private ReceiptNoPII receipt;
  private InstallmentNoPII installment;
  private Assessments assessment;
  private String officeCode;
  private String sectionCode;
  private String assessmentCode;
  private Long amountCents;
  private String officeDescription;
  private String sectionDescription;
  private String assessmentDescription;
}
