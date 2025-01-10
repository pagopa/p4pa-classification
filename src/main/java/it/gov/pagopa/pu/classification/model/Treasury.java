package it.gov.pagopa.pu.classification.model;

import it.gov.pagopa.pu.classification.config.semanticids.TreasurySemanticIdGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "treasury")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Treasury implements Serializable{

  @Id
  @TreasurySemanticIdGenerator
  private String treasuryId;

  private String billYear;
  private String billCode;
  private String accountCode;
  private String domainIdCode;
  private String transactionTypeCode;
  private String remittanceCode;
  private String remittanceInformation;

  //@Column(precision = 20, scale = 2)
  private BigDecimal billIpNumber;

  //@Temporal(TemporalType.DATE)
  private Date billDate;

  //@Temporal(TemporalType.DATE)
  private Date receptionDate;

  private String documentYear;
  private String documentCode;
  private String sealCode;
  private String lastName;
  private String firstName;
  private String address;
  private String postalCode;
  private String city;
  private String fiscalCode;
  private String vatNumber;
  private String abiCode;
  private String cabCode;
  private String accountRegistryCode;
  private String provisionalAe;
  private String provisionalCode;
  private String ibanCode;

  private Character accountTypeCode;
  private String processCode;
  private String executionPgCode;
  private String transferPgCode;
  private Long processPgNumber;

  //@Temporal(TemporalType.DATE)
  private Date regionValueDate;

  private Long organizationId;
  private String iuf;
  private String iuv;

  //@Temporal(TemporalType.TIMESTAMP)
  private Date creationDate;

  //@Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdateDate;

  private boolean isRegularized;
  private Long ingestionFlowFileId;

  //@Temporal(TemporalType.DATE)
  private Date actualSuspensionDate;

  private String managementProvisionalCode;
  private String endToEndId;

  //@Lob
  private byte[] taxCodeHash;

 // @Lob
  private byte[] vatNumberHash;

  //@Lob
  private byte[] lastNameHash;

  private Long personalDataId;

}
