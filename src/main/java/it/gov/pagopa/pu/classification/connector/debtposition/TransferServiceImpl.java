package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.TransferClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.Transfer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {
    private final TransferClient transferClient;

    public TransferServiceImpl(TransferClient transferClient) {
        this.transferClient = transferClient;
    }

    @Override
    public List<Transfer> getByInstallmentId(Long installmentId, String accessToken) {
        return transferClient.getByInstallmentId(installmentId, accessToken);
    }
}
