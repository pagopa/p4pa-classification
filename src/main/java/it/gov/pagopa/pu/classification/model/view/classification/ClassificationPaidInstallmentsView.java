package it.gov.pagopa.pu.classification.model.view.classification;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import java.time.OffsetDateTime;

@Entity
@Table(name = "classification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ClassificationPaidInstallmentsViewId.class)
public class ClassificationPaidInstallmentsView {
  @Id
  private String iud;
  @Id
  private String iuv;
  @Id
  private OffsetDateTime paymentDateTime;
  @Id
  private OffsetDateTime receiptCreationDate;
  @Id
  private String receiptPaymentRequestId;
  @NotNull
  private Long organizationId;
  private String debtPositionTypeOrgCode;
  @Formula(value = "(select sum(c.transfer_amount) " +
          "          from classification c " +
          "          where c.iud = iud " +
          "          group by (c.label) " +
          "          order by sum(c.transfer_amount) desc " +
          "          limit 1) ")
  private Long amount;
}
