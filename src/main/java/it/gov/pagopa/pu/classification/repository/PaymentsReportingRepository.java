package it.gov.pagopa.pu.classification.repository;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(path = "payments-reporting")
public interface PaymentsReportingRepository extends JpaRepository<PaymentsReporting,String> {

  List<PaymentsReporting> findByOrganizationIdAndIuf(Long organizationId, String iuf);
  List<PaymentsReporting> findByOrganizationIdAndIuvAndIurAndTransferIndex(Long organizationId, String iuv, String iur, int transferIndex);

}
