package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;

public interface ReceiptService {
  ReceiptNoPII getById(Long receiptId, String accessToken);
}
