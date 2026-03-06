package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
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

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;


@RepositoryRestResource(path = "assessments-details")
public interface AssessmentsDetailRepository extends JpaRepository<AssessmentsDetail, Long> {

  @RestResource(exported = false)
  AssessmentsDetail findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(String debtPositionTypeOrgCode, String iuv, String iud, String officeCode, String sectionCode, String assessmentCode);

  List<AssessmentsDetail> findAllByOrganizationIdAndIuvAndIud(Long organizationId, String iuv, String iud);

  @RestResource(exported = false)
  @Modifying
  @Transactional
  void deleteAllByOrganizationIdAndIuvAndIud(Long organizationId, String iuv, String iud);

  @SuppressWarnings("squid:S107") // Suppressing too many parameters warning: it's allowed in query methods
  @Query("""
    FROM AssessmentsDetail ad
    WHERE
      ad.assessmentId = :assessmentId
      AND (:iuv IS NULL OR ad.iuv = :iuv)
      AND (:iud IS NULL OR ad.iud = :iud)
      AND (cast(:updateDateTimeFrom AS STRING) IS NULL OR ad.updateDate >= :updateDateTimeFrom)
      AND (cast(:updateDateTimeTo AS STRING) IS NULL OR ad.updateDate <= :updateDateTimeTo)
      AND (cast(:paymentDateTimeFrom AS STRING) IS NULL OR ad.paymentDateTime >= :paymentDateTimeFrom)
      AND (cast(:paymentDateTimeTo AS STRING) IS NULL OR ad.paymentDateTime <= :paymentDateTimeTo)
      AND ((:fiscalCode IS NULL) OR (ad.debtorFiscalCodeHash = :#{@dataCipherService.hash(#fiscalCode)}))
    """)
  Page<AssessmentsDetail> findAssessmentsRowsDetail(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("assessmentId") Long assessmentId,
    @RequestParam(required = false) @Param("iud") String iud,
    @RequestParam(required = false) @Param("iuv") String iuv,
    @RequestParam(required = false) @Parameter(schema = @Schema(type = "LocalDateTime")) @Param("updateDateTimeFrom") LocalDateTime updateDateTimeFrom,
    @RequestParam(required = false) @Parameter(schema = @Schema(type = "LocalDateTime")) @Param("updateDateTimeTo") LocalDateTime updateDateTimeTo,
    @RequestParam(required = false) @Param("paymentDateTimeFrom") OffsetDateTime paymentDateTimeFrom,
    @RequestParam(required = false) @Param("paymentDateTimeTo") OffsetDateTime paymentDateTimeTo,
    @RequestParam(required = false) @Param("fiscalCode") String fiscalCode,
    Pageable pageable
  );

}
