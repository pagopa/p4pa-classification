package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.config.semanticids.TreasurySemanticIdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "treasury")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(of = "treasuryId", callSuper = false)
public class Treasury extends BaseEntity implements Serializable{

  @Id
  @TreasurySemanticIdGenerator
  private String treasuryId;
  private String billYear;
  private String billCode;

  private Long ingestionFlowFileId;
  private Long organizationId;
  private String iuf;
  private String iuv;

  private String accountCode;
  private String domainIdCode;
  private String transactionTypeCode;
  private String remittanceCode;
  private String remittanceInformation;
  private Long billAmountCents;
  private LocalDate billDate;
  private OffsetDateTime receptionDate;
  private String documentYear;
  private String documentCode;
  private String sealCode;

  private String pspLastName;
  private String pspFirstName;
  private String pspAddress;
  private String pspPostalCode;
  private String pspCity;
  private String pspFiscalCode;
  private String pspVatNumber;

  private String abiCode;
  private String cabCode;
  private String ibanCode;

  private String accountRegistryCode;
  private String provisionalAe;
  private String provisionalCode;
  private Character accountTypeCode;
  private String processCode;
  private String executionPgCode;
  private String transferPgCode;
  private Long processPgNumber;
  private LocalDate regionValueDate;
  private boolean isRegularized;
  private LocalDate actualSuspensionDate;
  private String managementProvisionalCode;
  private String endToEndId;
}
