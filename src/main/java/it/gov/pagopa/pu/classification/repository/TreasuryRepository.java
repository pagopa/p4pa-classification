package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.Treasury;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "treasury")
public interface TreasuryRepository extends JpaRepository<Treasury,Long> {

  @EntityGraph(value = "completeTreasury")
  Treasury findOneWithAllDataByTreasuryId(Long treasuryId);
}
