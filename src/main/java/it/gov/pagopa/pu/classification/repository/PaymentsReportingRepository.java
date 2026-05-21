package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.OffsetDateTime;
import java.util.List;


@RepositoryRestResource(path = "payments-reporting")
public interface PaymentsReportingRepository extends JpaRepository<PaymentsReporting,String> {

  @Query("""
    SELECT p
    FROM PaymentsReporting p
    WHERE
      p.organizationId = :organizationId AND
      p.iuf = :iuf AND
      p.deleted = false
  """)
  List<PaymentsReporting> findByOrganizationIdAndIuf(Long organizationId, String iuf);

  @Query("""
    SELECT p
    FROM PaymentsReporting p
    WHERE
      p.organizationId = :organizationId AND
      p.paymentsReportingId = :paymentsReportingId AND
      p.deleted = false
  """)
  PaymentsReporting findByOrganizationIdAndPaymentsReportingId(Long organizationId, String paymentsReportingId);

  @Query("SELECT p FROM PaymentsReporting p WHERE " +
    "p.organizationId=:organizationId AND " +
    "p.iuv=:iuv AND " +
    "p.iur=:iur AND " +
    "p.transferIndex=:transferIndex AND " +
    "p.deleted=false")
  List<PaymentsReporting> findByTransferSemanticKey(Long organizationId, String iuv, String iur, Integer transferIndex);

  @Query("SELECT p FROM PaymentsReporting p WHERE " +
    "p.organizationId=:organizationId AND " +
    "p.iuv=:iuv AND " +
    "p.iur=:iur AND " +
    "p.transferIndex=:transferIndex")
  List<PaymentsReporting> findByTransferSemanticKeyIncludedDeleted(Long organizationId, String iuv, String iur, Integer transferIndex);

  @Query("SELECT DISTINCT pr1 FROM PaymentsReporting pr1, PaymentsReporting pr2 WHERE " +
    // Input filters
    "pr1.organizationId = :organizationId AND " +
    "pr1.iuv = :iuv AND " +
    "pr1.transferIndex = :transferIndex AND " +
    "pr1.receiverOrganizationCode = :receiverOrganizationCode AND " +
    // Find duplicates
    "pr1.organizationId = pr2.organizationId AND " +
    "pr1.iuv = pr2.iuv AND " +
    "pr1.transferIndex = pr2.transferIndex AND " +
    "pr1.receiverOrganizationCode = pr2.receiverOrganizationCode AND " +
    "pr1.amountPaidCents = pr2.amountPaidCents AND " +
    "pr1.paymentOutcomeCode != pr2.paymentOutcomeCode AND " +
    "pr1.deleted=false")
  List<PaymentsReporting> findDuplicates(Long organizationId, String iuv,
                                         Integer transferIndex, String receiverOrganizationCode);

  @Query("SELECT pr1.flowDateTime FROM PaymentsReporting pr1 WHERE " +
    "pr1.organizationId = :organizationId AND " +
    "pr1.deleted=false " +
    "ORDER BY pr1.flowDateTime DESC " +
    "LIMIT 1")
  OffsetDateTime findLatestFlowDate(Long organizationId);

  @Query("""
    SELECT p
    FROM PaymentsReporting p
    WHERE
      p.organizationId = :organizationId AND
      p.iuf = :iuf AND
      p.ingestionFlowFileId <> :ingestionFlowFileId
  """)
  List<PaymentsReporting> findByOrganizationIdAndIufAndIngestionFlowFileIdNot(Long organizationId, String iuf, Long ingestionFlowFileId);
}
