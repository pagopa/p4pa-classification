package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(path = "payments-reporting")
public interface PaymentsReportingRepository extends JpaRepository<PaymentsReporting,String> {

  List<PaymentsReporting> findByOrganizationIdAndIuf(Long organizationId, String iuf);

  @Query("SELECT p FROM PaymentsReporting p WHERE " +
    "p.organizationId=:organizationId AND " +
    "p.iuv=:iuv AND " +
    "p.iur=:iur AND " +
    "p.transferIndex=:transferIndex")
  PaymentsReporting findBySemanticKey(Long organizationId, String iuv, String iur, int transferIndex);

}
