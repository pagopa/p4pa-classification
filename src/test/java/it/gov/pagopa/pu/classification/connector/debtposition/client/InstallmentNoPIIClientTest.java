package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.client.generated.InstallmentNoPiiSearchControllerApi;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelInstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelInstallmentNoPIIEmbedded;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstallmentNoPIIClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private InstallmentNoPiiSearchControllerApi installmentNoPiiSearchControllerApiMock;

  @InjectMocks
  private InstallmentNoPIIClient installmentNoPIIClient;


	@Test
	void getByReceiptId_withValidReceiptId_returnsInstallments() {
		String accessToken = "ACCESSTOKEN";
		Long receiptId = 1L;
		List<InstallmentNoPII> expectedInstallments = List.of(new InstallmentNoPII());
		CollectionModelInstallmentNoPIIEmbedded embedded = CollectionModelInstallmentNoPIIEmbedded.builder()
				.installmentNoPIIs(expectedInstallments).build();
		CollectionModelInstallmentNoPII collectionModel = CollectionModelInstallmentNoPII.builder()
				.embedded(embedded)
				.build();

		when(debtPositionApisHolderMock.getInstallmentNoPIISearchControllerApi(accessToken))
				.thenReturn(installmentNoPiiSearchControllerApiMock);
		when(installmentNoPiiSearchControllerApiMock.crudInstallmentsFindByReceiptId(receiptId))
				.thenReturn(collectionModel);

		List<InstallmentNoPII> result = installmentNoPIIClient.getByReceiptId(receiptId, accessToken);

		Assertions.assertEquals(expectedInstallments, result);
		verify(debtPositionApisHolderMock, times(1)).getInstallmentNoPIISearchControllerApi(accessToken);
		verify(installmentNoPiiSearchControllerApiMock, times(1)).crudInstallmentsFindByReceiptId(receiptId);
	}

}
