package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgServiceTest {

	@Mock
	private DebtPositionTypeOrgClient debtPositionTypeOrgClientMock;
	@InjectMocks
	private DebtPositionTypeOrgServiceImpl debtPositionTypeOrgService;


	@Test
	void whenGetDebtPositionTypeOrgByInstallmentIdThenInvokeClient() {
		// Given
		String accessToken = "ACCESSTOKEN";
		DebtPositionTypeOrg expected = mock(DebtPositionTypeOrg.class);
		Long installmentId = 1L;

		when(debtPositionTypeOrgClientMock.getDebtPositionTypeOrgByInstallmentId(installmentId, accessToken)).thenReturn(expected);

		// When
		DebtPositionTypeOrg result = debtPositionTypeOrgService.getDebtPositionTypeOrgByInstallmentId(installmentId, accessToken);

		// Then
		assertEquals(expected, result);
		verify(debtPositionTypeOrgClientMock, times(1)).getDebtPositionTypeOrgByInstallmentId(installmentId, accessToken);
	}
}
