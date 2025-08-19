package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.model.Treasury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RepositoryRestResource(path = "treasury")
public interface TreasuryRepository extends JpaRepository<Treasury, String> {

  @Query("""
    SELECT t
    FROM Treasury t
    WHERE t.organizationId = :organizationId
    AND t.treasuryId = :treasuryId
    """)
  Optional<Treasury> findByOrganizationIdAndTreasuryId(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("treasuryId") String treasuryId);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  long deleteByOrganizationIdAndBillCodeAndBillYearAndOrgBtCodeAndOrgIstatCode(Long organizationId, String billCode, String billYear, String orgBtCode, String orgIstatCode);

  @Query("SELECT t FROM Treasury t WHERE " +
    "t.organizationId=:organizationId AND " +
    "t.billCode=:billCode AND " +
    "t.billYear=:billYear")
  Treasury findBySemanticKey(Long organizationId, String billCode, String billYear, String orgBtCode, String orgIstatCode);

  Treasury getByOrganizationIdAndIuf(Long organizationId, String iuf);

}
