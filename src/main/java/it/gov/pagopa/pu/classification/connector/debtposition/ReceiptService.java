package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptNoPII;

public interface ReceiptService {
  ReceiptNoPII getById(Long receiptId, String accessToken);
  ReceiptNoPII getByReceiptIdAndDebtPositionTypeOrgCode(Long receiptId, String debtPositionTypeOrgCode, String accessToken);
}
