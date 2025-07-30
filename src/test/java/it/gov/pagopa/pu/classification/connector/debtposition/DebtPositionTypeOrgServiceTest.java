package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.DebtPositionTypeOrgClient;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgServiceTest {

	@Mock
	private DebtPositionTypeOrgClient clientMock;

	@InjectMocks
	private DebtPositionTypeOrgServiceImpl service;

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(clientMock);
  }


	@Test
	void whenGetDebtPositionTypeOrgByInstallmentIdThenInvokeClient() {
		// Given
		String accessToken = "ACCESSTOKEN";
		Long installmentId = 1L;
    DebtPositionTypeOrg expected = new DebtPositionTypeOrg();

		when(clientMock.getDebtPositionTypeOrgByInstallmentId(installmentId, accessToken)).thenReturn(expected);

		// When
		DebtPositionTypeOrg result = service.getDebtPositionTypeOrgByInstallmentId(installmentId, accessToken);

		// Then
		assertSame(expected, result);
	}

  @Test
  void whenFindDebtPositionTypeOrgsThenInvokeClient() {
    // Given
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String operatorExternalUserId = "OPERATOR_EXTERNAL_USER_ID";
    List<DebtPositionTypeOrg> expected = List.of();

    when(clientMock.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId,true,  accessToken)).thenReturn(expected);
    // When
    List<DebtPositionTypeOrg> result = service.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, true, accessToken);
    // Then
    assertSame(expected, result);
  }

  @Test
  void whenGetDebtPositionTypeOrgByDebtPositionTypeOrgIdThenInvokeClient() {
    // Given
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 3L;
    DebtPositionTypeOrg expected = new DebtPositionTypeOrg();

    when(clientMock.getDebtPositionTypeOrgByDebtPositionTypeOrgId(organizationId, debtPositionTypeOrgId, accessToken)).thenReturn(expected);

    // When
    DebtPositionTypeOrg result = service.getDebtPositionTypeOrgByDebtPositionTypeOrgId(organizationId, debtPositionTypeOrgId, accessToken);

    // Then
    assertSame(expected, result);
  }
}
