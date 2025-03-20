package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.InstallmentNoPIIClient;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstallmentNoPIIServiceTest {

	@Mock
	private InstallmentNoPIIClient installmentNoPIIClientMock;
	@InjectMocks
	private InstallmentNoPIIServiceImpl installmentNoPIIService;


	@Test
	void whenGetByReceiptIdThenInvokeClient() {
		// Given
		String accessToken = "ACCESSTOKEN";
		List<InstallmentNoPIIResponse> expected = mock(List.class);
		Long receiptId = 1L;

		when(installmentNoPIIClientMock.getByReceiptId(receiptId, accessToken)).thenReturn(expected);

		// When
		List<InstallmentNoPIIResponse> result = installmentNoPIIService.getByReceiptId(receiptId, accessToken);

		// Then
		assertEquals(expected, result);
		verify(installmentNoPIIClientMock, times(1)).getByReceiptId(receiptId, accessToken);
	}
}
