package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import it.gov.pagopa.pu.classification.repository.PaymentsReportingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PaymentsReportingService {

  private final PaymentsReportingRepository repository;

  public PaymentsReportingService(PaymentsReportingRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public List<PaymentsReporting> findAndDeleteByOrgIdAndIufAndIngestionFlowFileIdNot(Long organizationId, String iuf, Long ingestionFlowFileId) {
    List<PaymentsReporting> paymentsReportingList = repository.findByOrganizationIdAndIufAndIngestionFlowFileIdNot(organizationId, iuf, ingestionFlowFileId);
    paymentsReportingList.forEach(paymentsReporting -> paymentsReporting.setDeleted(true));
    return repository.saveAll(paymentsReportingList);
  }
}
