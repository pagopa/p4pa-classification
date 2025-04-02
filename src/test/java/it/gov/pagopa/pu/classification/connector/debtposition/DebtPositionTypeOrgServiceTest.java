package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

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

  @Test
  void whenFindDebtPositionTypeOrgsThenInvokeClient() {
    // Given
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String operatorExternalUserId = "OPERATOR_EXTERNAL_USER_ID";
    List<DebtPositionTypeOrg> expected = List.of(mock(DebtPositionTypeOrg.class));

    when(debtPositionTypeOrgClientMock.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken)).thenReturn(expected);
    // When
    List<DebtPositionTypeOrg> result = debtPositionTypeOrgService.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);
    // Then
    assertEquals(expected, result);
    verify(debtPositionTypeOrgClientMock, times(1)).findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);
  }
}
