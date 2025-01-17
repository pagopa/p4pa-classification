package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.Treasury;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;


@RepositoryRestResource(path = "treasury")
public interface TreasuryRepository extends JpaRepository<Treasury,String> {

  @Transactional
  @Modifying
  @RestResource(exported = false)
  long deleteByOrganizationIdAndBillCodeAndBillYear(Long organizationId, String billCode, String billYear);

  @Query("SELECT t FROM Treasury t WHERE " +
    "t.organizationId=:organizationId AND " +
    "t.billCode=:billCode AND " +
    "t.billYear=:billYear")
  Treasury findBySemanticKey(Long organizationId, String billCode, String billYear);

  Treasury getByOrganizationIdAndIuf(Long organizationId, String iuf);

}
