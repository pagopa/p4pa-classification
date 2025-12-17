package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(path = "payments-reporting")
public interface PaymentsReportingRepository extends JpaRepository<PaymentsReporting,String> {

  List<PaymentsReporting> findByOrganizationIdAndIuf(Long organizationId, String iuf);

  PaymentsReporting findByOrganizationIdAndPaymentsReportingId(Long organizationId, String paymentsReportingId);

  @Query("SELECT p FROM PaymentsReporting p WHERE " +
    "p.organizationId=:organizationId AND " +
    "p.iuv=:iuv AND " +
    "p.iur=:iur AND " +
    "p.transferIndex=:transferIndex")
  List<PaymentsReporting> findByTransferSemanticKey (Long organizationId, String iuv, String iur, int transferIndex);

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
    "pr1.paymentOutcomeCode != pr2.paymentOutcomeCode")
  List<PaymentsReporting> findDuplicates(Long organizationId, String iuv,
    int transferIndex, String receiverOrganizationCode);
}
