package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.List;

@Lazy
@Slf4j
@Service
public class InstallmentNoPIIClient {
    private final DebtPositionApisHolder debtPositionApisHolder;

    public InstallmentNoPIIClient(DebtPositionApisHolder debtPositionApisHolder) {
        this.debtPositionApisHolder = debtPositionApisHolder;
    }


    public List<InstallmentNoPIIResponse> getByReceiptId(String accessToken, Long receiptId) {
        try {
            return debtPositionApisHolder.getInstallmentNoPIISearchControllerApi(accessToken)
                .crudInstallmentsFindByReceiptId(receiptId).getEmbedded().getInstallmentNoPIIs();
        } catch (HttpClientErrorException.NotFound e) {
            log.info("IntallmentNoPII not found for receiptId: {}", receiptId);
            return Collections.emptyList();
        }
    }
}
