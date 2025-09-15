package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.enums.TreasuryOrigin;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
public class Treasury extends BaseEntity implements Serializable {

  @Id
  private String treasuryId;
  @NotNull
  private String billYear;
  @NotNull
  private String billCode;
  @NotNull
  private Long ingestionFlowFileId;
  @NotNull
  private Long organizationId;
  @NotNull
  private String orgBtCode;
  @NotNull
  private String orgIstatCode;
  private String iuf;
  private String iuv;

  private String accountCode;
  private String domainIdCode;
  private String transactionTypeCode;
  private String remittanceCode;
  private String remittanceDescription;
  @NotNull
  private Long billAmountCents;
  @NotNull
  private LocalDate billDate;
  private OffsetDateTime receptionDate;
  private String documentYear;
  private String documentCode;
  private String sealCode;

  @NotNull
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
  @NotNull
  @Enumerated(EnumType.STRING)
  private TreasuryOrigin treasuryOrigin;
  private String checkNumber;
  private String clientReference;
  private String bankReference;

  //region keep updated semanticId
  public static String buildSemanticId(Treasury treasury) {
    return treasury.getBillCode() + "-" +
      treasury.getBillYear() + "-" +
      treasury.getOrgIstatCode() + "-" +
      treasury.getOrgBtCode() + "-" +
      treasury.getOrganizationId();
  }

  private void setSemanticId() {
    this.treasuryId = buildSemanticId(this);
  }

  public void setOrganizationId(Long organizationId) {
    this.organizationId = organizationId;
    setSemanticId();
  }

  public void setBillCode(String billCode) {
    this.billCode = billCode;
    setSemanticId();
  }

  public void setBillYear(String billYear) {
    this.billYear = billYear;
    setSemanticId();
  }

  public void setOrgBtCode(String orgBtCode) {
    this.orgBtCode = orgBtCode;
    setSemanticId();
  }

  public void setOrgIstatCode(String orgIstatCode) {
    this.orgIstatCode = orgIstatCode;
    setSemanticId();
  }
  //endregion
}
