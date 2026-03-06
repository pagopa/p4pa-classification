package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.client.generated.TransferSearchControllerApi;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelTransfer;
import it.gov.pagopa.pu.debtposition.dto.generated.Transfer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.CollectionUtils;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private TransferSearchControllerApi transferSearchControllerApiMock;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @InjectMocks
  private TransferClient client;

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, transferSearchControllerApiMock);
  }

  @Test
  void whenGetByInstallmentIdThenReturnIt() {
    String accessToken = "ACCESSTOKEN";
    long installmentId = 1L;
    CollectionModelTransfer collectionModelTransfer = podamFactory.manufacturePojo(CollectionModelTransfer.class);

    when(debtPositionApisHolderMock.getTransferSearchControllerApi(accessToken))
      .thenReturn(transferSearchControllerApiMock);
    when(transferSearchControllerApiMock.crudTransfersFindByInstallmentId(installmentId))
      .thenReturn(collectionModelTransfer);

    List<Transfer> result = client.getByInstallmentId(installmentId, accessToken);

    Assertions.assertSame(collectionModelTransfer.getEmbedded().getTransfers(), result);
  }

  @Test
  void givenNullEmbeddedWhenGetByInstallmentIdThenReturnEmptyList() {
    String accessToken = "ACCESSTOKEN";
    long installmentId = 1L;
    CollectionModelTransfer collectionModelTransfer = podamFactory.manufacturePojo(CollectionModelTransfer.class);
    collectionModelTransfer.setEmbedded(null);

    when(debtPositionApisHolderMock.getTransferSearchControllerApi(accessToken))
      .thenReturn(transferSearchControllerApiMock);
    when(transferSearchControllerApiMock.crudTransfersFindByInstallmentId(installmentId))
      .thenReturn(collectionModelTransfer);

    List<Transfer> result = client.getByInstallmentId(installmentId, accessToken);

    Assertions.assertTrue(CollectionUtils.isEmpty(result));
  }

  @Test
  void givenNullCollectionModelWhenGetByInstallmentIdThenReturnEmptyList() {
    String accessToken = "ACCESSTOKEN";
    long installmentId = 1L;

    when(debtPositionApisHolderMock.getTransferSearchControllerApi(accessToken))
      .thenReturn(transferSearchControllerApiMock);
    when(transferSearchControllerApiMock.crudTransfersFindByInstallmentId(installmentId))
      .thenReturn(null);

    List<Transfer> result = client.getByInstallmentId(installmentId, accessToken);

    Assertions.assertTrue(CollectionUtils.isEmpty(result));
  }
}
