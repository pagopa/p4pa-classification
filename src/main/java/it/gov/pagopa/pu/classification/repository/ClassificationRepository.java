package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.enums.ClassificationsEnum;
import it.gov.pagopa.pu.classification.model.Classification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RepositoryRestResource(path = "classifications")
public interface ClassificationRepository extends
  JpaRepository<Classification, String> {

  @Transactional
  @Modifying
  @RestResource(exported = false)
  Integer deleteByOrganizationIdAndIudAndLabel(Long organizationId, String iud,
    ClassificationsEnum label);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  Integer deleteByOrganizationIdAndIufAndLabel(Long organizationId, String iuf,
    ClassificationsEnum label);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  Integer deleteByOrganizationIdAndIuvAndIurAndTransferIndex(
    Long organizationId, String iuv, String iur, Integer transferIndex);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  Integer deleteByOrganizationIdAndIuvAndIurAndTransferIndexAndLabelNot(Long organizationId, String iuv, String iur, Integer transferIndex, ClassificationsEnum label);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  @Query("""
    DELETE FROM Classification c
    WHERE c.organizationId = :organizationId
    AND c.iuv = :iuv
    AND c.transferIndex = :transferIndex
    AND c.label = :label
    """)
  Integer deleteDuplicates(Long organizationId, String iuv, Integer transferIndex,
    ClassificationsEnum label);

  @Transactional
  @Modifying
  @RestResource(exported = false)
  Integer deleteByOrganizationIdAndTreasuryId(Long organizationId,
    String treasuryId);

  @Query("""
    SELECT c
    FROM Classification c
    WHERE c.organizationId = :organizationId
    AND ( (:iuv IS NOT NULL AND c.iuv = :iuv) OR (:iuf IS NOT NULL AND c.iuf = :iuf) )
    AND (:debtPositionTypeOrgCodes IS NULL OR c.debtPositionTypeOrgCode IN :debtPositionTypeOrgCodes)
    AND (:labels IS NULL OR c.label IN :labels)
    """)
  Page<Classification> findByFilters(
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @RequestParam(required = false) @Param("iuv") String iuv,
    @RequestParam(required = false) @Param("iuf") String iuf,
    @RequestParam(required = false) @Param("debtPositionTypeOrgCodes") List<String> debtPositionTypeOrgCodes,
    @RequestParam(required = false) @Param("labels") List<ClassificationsEnum> labels,
    Pageable pageable);

  @Override
  @RestResource(exported = false)
  <S extends Classification> S save(S entity);

  List<Classification> findAllByOrganizationIdAndIuvAndIud(Long organizationId,
    String iuv, String iud);
}
