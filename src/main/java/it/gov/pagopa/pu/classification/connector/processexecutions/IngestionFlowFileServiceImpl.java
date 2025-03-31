package it.gov.pagopa.pu.classification.connector.processexecutions;

import it.gov.pagopa.pu.classification.connector.processexecutions.client.IngestionFlowFileClient;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.IngestionFlowFile;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Lazy
@Service
public class IngestionFlowFileServiceImpl implements IngestionFlowFileService {

  private final IngestionFlowFileClient client;

  public IngestionFlowFileServiceImpl(IngestionFlowFileClient client) {
    this.client = client;
  }

  @Override
  public IngestionFlowFile getIngestionFlowFile(Long ingestionFlowFileId, String accessToken) {
    return client.getIngestionFlowFile(ingestionFlowFileId, accessToken);
  }
}
