package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Slf4j
@Service
public class ReceiptNoPIIClient {
    private final DebtPositionApisHolder debtPositionApisHolder;

    public ReceiptNoPIIClient(DebtPositionApisHolder debtPositionApisHolder) {
        this.debtPositionApisHolder = debtPositionApisHolder;
    }

    public ReceiptNoPII getById(Long receiptId, String accessToken) {
      try{
        return debtPositionApisHolder.getReceiptNoPiiEntityControllerApi(accessToken)
          .crudGetReceiptnopii(String.valueOf(receiptId));
      } catch (HttpClientErrorException.NotFound e) {
        log.info("Cannot find ReceiptNoPII having id {}", receiptId);
        return null;
      }
    }
}
