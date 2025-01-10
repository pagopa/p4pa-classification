package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.Treasury;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "treasury")
public interface TreasuryRepository extends JpaRepository<Treasury,Long> {

  Long insert(Treasury treasuryDto);

  @Transactional
  @Modifying
  @Query("DELETE FROM TreasuryDTO t WHERE t.organizationId = :organizationId AND t.billCode = :billCode AND t.billYear = :billYear")
  int deleteByOrganizationIdAndBillCodeAndBillYear(Long organizationId, String billCode, String billYear);

  Treasury getByOrganizationIdAndBillCodeAndBillYear(Long organizationId, String billCode, String billYear);

  Treasury getByOrganizationIdAndIuf(Long organizationId, String iuf);

}
