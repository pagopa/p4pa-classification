package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelInstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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

    public List<InstallmentNoPII> findByOrganizationIdAndIuds(Long organizationId, Set<String> iuds, String accessToken) {
        CollectionModelInstallmentNoPII collectionModelInstallmentNoPII = debtPositionApisHolder.getInstallmentNoPIISearchControllerApi(accessToken)
                .crudInstallmentsFindByOrganizationIdAndIuds(organizationId,iuds);
        return collectionModelInstallmentNoPII!=null && collectionModelInstallmentNoPII.getEmbedded() !=null?
                collectionModelInstallmentNoPII.getEmbedded().getInstallmentNoPIIs(): Collections.emptyList();
    }
}
