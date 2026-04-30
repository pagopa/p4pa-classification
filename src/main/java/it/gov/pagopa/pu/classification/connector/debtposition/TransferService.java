package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.debtposition.dto.generated.Transfer;

import java.util.List;

public interface TransferService {
  List<Transfer> getByInstallmentId(Long installmentId, String accessToken);
}
