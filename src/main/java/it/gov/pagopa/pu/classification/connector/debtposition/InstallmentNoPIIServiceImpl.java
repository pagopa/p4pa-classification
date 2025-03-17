package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.auth.AuthnService;
import it.gov.pagopa.pu.classification.connector.debtposition.client.InstallmentNoPIIClient;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Lazy
public class InstallmentNoPIIServiceImpl implements InstallmentNoPIIService{
  private final AuthnService authnService;
  private final InstallmentNoPIIClient installmentNoPIIClient;

  public InstallmentNoPIIServiceImpl(AuthnService authnService,
      InstallmentNoPIIClient installmentNoPIIClient) {
    this.authnService = authnService;
    this.installmentNoPIIClient = installmentNoPIIClient;
  }

  @Override
  public List<InstallmentNoPIIResponse> getByReceiptId(Long receiptId) {
    return installmentNoPIIClient.getByReceiptId(authnService.getAccessToken(), receiptId);
  }
}
