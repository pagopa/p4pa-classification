package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(path = "assessments-registries")
public interface AssessmentsRegistryRepository extends JpaRepository<AssessmentsRegistry,Long> {

}
