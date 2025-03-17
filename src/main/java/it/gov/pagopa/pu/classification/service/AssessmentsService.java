package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentNoPIIService;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Slf4j
@Service
public class AssessmentsService {

  private final InstallmentNoPIIService installmentNoPIIService;


  public AssessmentsService(InstallmentNoPIIService installmentNoPIIService) {
    this.installmentNoPIIService = installmentNoPIIService;
  }

  public List<InstallmentNoPIIResponse> getInstallmentsByReceiptId(Long receiptId) {
    return installmentNoPIIService.getByReceiptId(receiptId);
  }

}
