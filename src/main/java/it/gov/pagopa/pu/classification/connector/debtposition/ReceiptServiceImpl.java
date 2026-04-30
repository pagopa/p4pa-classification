package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.ReceiptNoPIIClient;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import org.springframework.stereotype.Service;

@Service
public class ReceiptServiceImpl implements ReceiptService {

  private final ReceiptNoPIIClient client;

  public ReceiptServiceImpl(ReceiptNoPIIClient client) {
    this.client = client;
  }

  @Override
  public ReceiptNoPII getById(Long receiptId, String accessToken) {
    return client.getById(receiptId, accessToken);
  }

  @Override
  public ReceiptNoPII getByReceiptIdAndDebtPositionTypeOrgCode(Long receiptId, String debtPositionTypeOrgCode, String accessToken) {
    return client.getByReceiptIdAndDebtPositionTypeOrgCode(receiptId, debtPositionTypeOrgCode, accessToken);
  }
}
