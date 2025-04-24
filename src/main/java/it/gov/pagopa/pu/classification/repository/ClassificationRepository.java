package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.model.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RepositoryRestResource(path = "classifications")
public interface ClassificationRepository extends JpaRepository<Classification, String> {
  @Transactional
  @Modifying
  @RestResource(exported = false)
  long deleteByOrganizationIdAndIudAndLabel(Long organizationId, String iud, ClassificationsEnum label);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  long deleteByOrganizationIdAndIufAndLabel(Long organizationId, String iuf, ClassificationsEnum label);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  long deleteByOrganizationIdAndIuvAndIurAndTransferIndex(Long organizationId, String iuv, String iur, int transferIndex);

  Optional<Classification> findByOrganizationIdAndClassificationId(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("classificationId") Long classificationId);
}
