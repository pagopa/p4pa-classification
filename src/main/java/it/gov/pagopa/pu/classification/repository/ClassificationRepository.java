package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.model.Classification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


@RepositoryRestResource(path = "classifications")
public interface ClassificationRepository extends JpaRepository<Classification, String> {
  @Transactional
  @Modifying
  @RestResource(exported = false)
  Integer deleteByOrganizationIdAndIudAndLabel(Long organizationId, String iud, ClassificationsEnum label);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  Integer deleteByOrganizationIdAndIufAndLabel(Long organizationId, String iuf, ClassificationsEnum label);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  Integer deleteByOrganizationIdAndIuvAndIurAndTransferIndex(Long organizationId, String iuv, String iur, int transferIndex);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  Integer deleteByOrganizationIdAndTreasuryId(Long organizationId, String treasuryId);

  @Query("""
    SELECT c
    FROM Classification c
    WHERE c.organizationId = :organizationId
    AND c.iuv = :iuv
    AND (:debtPositionTypeOrgCodes IS NULL OR c.debtPositionTypeOrgCode IN :debtPositionTypeOrgCodes)
    AND (:labels IS NULL OR c.label IN :labels)
    """)
  Page<Classification> findByFilters(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Parameter(required = true) @Param("iuv") String iuv,
    @Param("debtPositionTypeOrgCodes") List<String> debtPositionTypeOrgCodes,
    @Param("labels") List<ClassificationsEnum> labels,
    Pageable pageable);

  @Override
  @RestResource(exported = false)
  <S extends Classification> S save(S entity);
}
