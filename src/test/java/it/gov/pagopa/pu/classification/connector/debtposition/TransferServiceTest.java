package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.TransferClient;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.Transfer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {
	public static final PodamFactory podamFactory = TestUtils.getPodamFactory();
	@Mock
	private TransferClient clientMock;
	@InjectMocks
	private TransferServiceImpl service;

	@Test
	void whenGetByReceiptIdThenInvokeClient() {
		String accessToken = "ACCESSTOKEN";
		Long installmentId = 1L;
    	List<Transfer> expectedResult = podamFactory.manufacturePojo(List.class,Transfer.class);

		when(clientMock.getByInstallmentId(installmentId, accessToken)).thenReturn(expectedResult);

		List<Transfer> result = service.getByInstallmentId(installmentId, accessToken);

		assertSame(expectedResult, result);
	}
}
