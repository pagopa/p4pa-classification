package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.client.generated.DebtPositionTypeOrgSearchControllerApi;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApiMock;

  @InjectMocks
  private DebtPositionTypeOrgClient debtPositionTypeOrgClient;


  @Test
  void getDebtPositionTypeOrgByInstallmentId_withValidInstallmentId_returnsDebtPositionTypeOrg() {
    String accessToken = "ACCESSTOKEN";
    Long installmentId = 1L;
    DebtPositionTypeOrg expectedDebtPositionTypeOrg = new DebtPositionTypeOrg();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsGetDebtPositionTypeOrgByInstallmentId(installmentId))
      .thenReturn(expectedDebtPositionTypeOrg);

    DebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrgByInstallmentId(installmentId, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedDebtPositionTypeOrg, result);
  }

  @Test
  void givenValidInputWhenFindDebtPositionTypeOrgsThenReturnsDebtPositionTypeOrgs() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String operatorExternalUserId = "OPERATOR_EXTERNAL_USER_ID";
    CollectionModelDebtPositionTypeOrg expectedDebtPositionTypeOrgs = mock(CollectionModelDebtPositionTypeOrg.class);

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(String.valueOf(organizationId), operatorExternalUserId))
      .thenReturn(expectedDebtPositionTypeOrgs);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgClient.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(expectedDebtPositionTypeOrgs.getEmbedded().getDebtPositionTypeOrgs(), result);
  }
}
