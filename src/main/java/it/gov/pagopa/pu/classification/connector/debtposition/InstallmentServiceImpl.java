package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.InstallmentNoPIIClient;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy
public class InstallmentServiceImpl implements InstallmentService {
  private final InstallmentNoPIIClient installmentNoPIIClient;

  public InstallmentServiceImpl(InstallmentNoPIIClient installmentNoPIIClient) {
    this.installmentNoPIIClient = installmentNoPIIClient;
  }

  @Override
  public List<InstallmentNoPIIResponse> getByReceiptId(Long receiptId, String accessToken) {
    return installmentNoPIIClient.getByReceiptId(receiptId, accessToken);
  }
}
