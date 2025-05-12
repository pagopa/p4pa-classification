package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class InstallmentNoPIIClient {
    private final DebtPositionApisHolder debtPositionApisHolder;

    public InstallmentNoPIIClient(DebtPositionApisHolder debtPositionApisHolder) {
        this.debtPositionApisHolder = debtPositionApisHolder;
    }

    public List<InstallmentNoPII> getByReceiptId(Long receiptId, String accessToken) {
      return debtPositionApisHolder.getInstallmentNoPIISearchControllerApi(accessToken)
        .crudInstallmentsFindByReceiptId(receiptId).getEmbedded().getInstallmentNoPIIs();
    }
}
