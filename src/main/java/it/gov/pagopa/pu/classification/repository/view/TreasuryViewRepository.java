package it.gov.pagopa.pu.classification.repository.view;

import io.swagger.v3.oas.annotations.Parameter;
import it.gov.pagopa.pu.classification.model.view.TreasuryView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

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
        t.documentYear as documentYear,
        t.orgBtCode as orgBtCode,
        t.orgIstatCode as orgIstatCode
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
    @Parameter(required = true) @Param("organizationId") Long organizationId,
    @RequestParam(required = false) @Param("iuv") String iuv,
    @RequestParam(required = false) @Param("iuf") String iuf,
    @RequestParam(required = false) @Param("billAmountCents") Long billAmountCents,
    @RequestParam(required = false) @Param("billDateFrom") LocalDate billDateFrom,
    @RequestParam(required = false) @Param("billDateTo") LocalDate billDateTo,
    @RequestParam(required = false) @Param("provisionalCode") String provisionalCode,
    @RequestParam(required = false) @Param("provisionalAe") String provisionalAe,
    @RequestParam(required = false) @Param("billCode") String billCode,
    @RequestParam(required = false) @Param("billYear") String billYear,
    @RequestParam(required = false) @Param("pspLastName") String pspLastName,
    @RequestParam(required = false) @Param("regionValueDateFrom") LocalDate regionValueDateFrom,
    @RequestParam(required = false) @Param("regionValueDateTo") LocalDate regionValueDateTo,
    @RequestParam(required = false) @Param("documentCode") String documentCode,
    @RequestParam(required = false) @Param("documentYear") String documentYear,
    Pageable pageable);

}
