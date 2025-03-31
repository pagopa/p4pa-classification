package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.gov.pagopa.pu.classification.model.view.TreasuryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;

@RepositoryRestResource(path = "treasuries-view")
public interface TreasuryViewRepository extends Repository<TreasuryView, String> {

  @SuppressWarnings("squid:S107")
  // Suppressing too many parameters warning: it's allowed in query methods
  @Query("""
        SELECT new TreasuryView(
        t.treasuryId as treasuryId,
        t.organizationId as organizationId,
        t.billYear as billYear,
        t.billCode as billCode,
        t.regionValueDate as regionValueDate,
        t.billDate as billDate,
        t.iuf as iuf,
        t.billAmountCents as billAmountCents,
        t.iuv as iuv,
        t.provisionalCode as provisionalCode,
        t.provisionalAe as provisionalAe,
        t.pspLastName as pspLastName,
        t.documentCode as documentCode,
        t.documentYear as documentYear
        )
        FROM TreasuryView t
        WHERE t.organizationId = :organizationId
        AND (:iuv IS NULL OR t.iuv = :iuv)
        AND (:iuf IS NULL OR t.iuf = :iuf)
        AND (:billAmountCents IS NULL OR t.billAmountCents = :billAmountCents)
        AND (cast(:billDateFrom AS DATE) IS NULL OR t.billDate >= :billDateFrom)
        AND (cast(:billDateTo AS DATE) IS NULL OR t.billDate <= :billDateTo)
        AND (:provisionalCode IS NULL OR t.provisionalCode = :provisionalCode)
        AND (:provisionalAe IS NULL OR t.provisionalAe = :provisionalAe)
        AND (:billCode IS NULL OR t.billCode = :billCode)
        AND (:billYear IS NULL OR t.billYear = :billYear)
        AND (:pspLastName IS NULL OR t.pspLastName = :pspLastName)
        AND (cast(:regionValueDateFrom AS DATE) IS NULL OR t.regionValueDate >= :regionValueDateFrom)
        AND (cast(:regionValueDateTo AS DATE) IS NULL OR t.regionValueDate <= :regionValueDateTo)
        AND (:documentCode IS NULL OR t.documentCode = :documentCode)
        AND (:documentYear IS NULL OR t.documentYear = :documentYear)
    """)
  Page<TreasuryView> findTreasuriesByFilters(
    @Parameter(required = true, schema = @Schema(type = "integer", format = "int64")) @Param("organizationId") Long organizationId,
    @Param("iuv") String iuv,
    @Param("iuf") String iuf,
    @Param("billAmountCents") Long billAmountCents,
    @Param("billDateFrom") LocalDate billDateFrom,
    @Param("billDateTo") LocalDate billDateTo,
    @Param("provisionalCode") String provisionalCode,
    @Param("provisionalAe") String provisionalAe,
    @Param("billCode") String billCode,
    @Param("billYear") String billYear,
    @Param("pspLastName") String pspLastName,
    @Param("regionValueDateFrom") LocalDate regionValueDateFrom,
    @Param("regionValueDateTo") LocalDate regionValueDateTo,
    @Param("documentCode") String documentCode,
    @Param("documentYear") String documentYear,
    Pageable pageable);

}
