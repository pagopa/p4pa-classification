package it.gov.pagopa.pu.classification.repository;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;


@RepositoryRestResource(path = "assessments-details")
public interface AssessmentsDetailRepository extends JpaRepository<AssessmentsDetail, Long> {

  @RestResource(exported = false)
  AssessmentsDetail findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(String debtPositionTypeOrgCode, String iuv, String iud, String officeCode, String sectionCode, String assessmentCode);

  @RestResource(exported = false)
  void deleteAllByDebtPositionTypeOrgCodeAndIuvAndIud(String debtPositionTypeOrgCode, String iuv, String iud);

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
    @Param("iud") String iud,
    @Param("iuv") String iuv,
    @Parameter(schema = @Schema(type = "LocalDateTime")) @Param("updateDateTimeFrom") LocalDateTime updateDateTimeFrom,
    @Parameter(schema = @Schema(type = "LocalDateTime")) @Param("updateDateTimeTo") LocalDateTime updateDateTimeTo,
    @Param("paymentDateTimeFrom") OffsetDateTime paymentDateTimeFrom,
    @Param("paymentDateTimeTo") OffsetDateTime paymentDateTimeTo,
    @Param("fiscalCode") String fiscalCode,
    Pageable pageable
  );

}
