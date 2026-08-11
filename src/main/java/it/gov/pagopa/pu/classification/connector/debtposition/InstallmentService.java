package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.debtpositions.dto.generated.InstallmentNoPII;

import java.util.List;
import java.util.Set;

/**
 * Service for handling InstallmentNoPII operations.
 */
public interface InstallmentService {

  /**
   * Finds a list of InstallmentNoPII by the given receipt ID.
   *
   * @param organizationId the unique identifier of the organization.
   * @param receiptId the unique identifier of the receipt.
   * @return a list of InstallmentNoPII associated with the given receipt ID.
   */
  List<InstallmentNoPII> getByReceiptId(Long organizationId, Long receiptId, String accessToken);

  List<InstallmentNoPII> findByOrganizationIdAndIuds(Long organizationId, Set<String> iuds, String accessToken);
}
