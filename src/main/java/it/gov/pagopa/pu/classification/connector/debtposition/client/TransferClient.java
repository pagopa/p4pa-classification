package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelTransfer;
import it.gov.pagopa.pu.debtpositions.dto.generated.Transfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TransferClient {
    private final DebtPositionApisHolder debtPositionApisHolder;

    public TransferClient(DebtPositionApisHolder debtPositionApisHolder) {
        this.debtPositionApisHolder = debtPositionApisHolder;
    }

    public List<Transfer> getByInstallmentId(Long installmentId, String accessToken) {
        CollectionModelTransfer transfers = debtPositionApisHolder.getTransferSearchControllerApi(accessToken)
                .crudTransfersFindByInstallmentId(installmentId);
        return transfers != null && transfers.getEmbedded()!=null?transfers.getEmbedded().getTransfers() : Collections.emptyList();
    }
}
