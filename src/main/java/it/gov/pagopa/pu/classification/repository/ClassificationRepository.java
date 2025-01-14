package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;


@RepositoryRestResource(path = "classifications")
public interface ClassificationRepository extends JpaRepository<Classification,String> {

  @Transactional
  @Modifying
  long deleteByOrganizationIdAndIufAndLabel(Long organizationId, String iuf, String label);

  @Transactional
  @Modifying
  long deleteByOrganizationIdAndIuvAndIurAndTransferIndex(Long organizationId, String iuv, String iur, int transferIndex);

}
