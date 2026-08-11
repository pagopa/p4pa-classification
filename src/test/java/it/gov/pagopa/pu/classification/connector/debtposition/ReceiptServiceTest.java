package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.ReceiptNoPIIClient;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptNoPII;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

	@Mock
	private ReceiptNoPIIClient clientMock;

	@InjectMocks
	private ReceiptServiceImpl service;

	@Test
	void whenGetByReceiptIdThenInvokeClient() {
		// Given
		String accessToken = "ACCESSTOKEN";
		Long receiptId = 1L;
    ReceiptNoPII expected = new ReceiptNoPII();

		when(clientMock.getById(receiptId, accessToken)).thenReturn(expected);

		// When
		ReceiptNoPII result = service.getById(receiptId, accessToken);

		// Then
		assertSame(expected, result);
	}

	@Test
	void whengetByReceiptIdAndDebtPositionTypeOrgCodeThenInvokeClient() {
		// Given
		String accessToken = "ACCESSTOKEN";
		Long receiptId = 1L;
		String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    	ReceiptNoPII expected = new ReceiptNoPII();

		when(clientMock.getByReceiptIdAndDebtPositionTypeOrgCode(receiptId, debtPositionTypeOrgCode, accessToken)).thenReturn(expected);

		// When
		ReceiptNoPII result = service.getByReceiptIdAndDebtPositionTypeOrgCode(receiptId, debtPositionTypeOrgCode, accessToken);

		// Then
		assertSame(expected, result);
	}
}
