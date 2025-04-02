package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.client.generated.DebtPositionTypeOrgSearchControllerApi;
import it.gov.pagopa.pu.debtposition.dto.generated.*;
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
    List<DebtPositionTypeOrg> expectedDebtPositionTypeOrgs = List.of(new DebtPositionTypeOrg());
    PagedModelDebtPositionTypeOrgEmbedded embedded = PagedModelDebtPositionTypeOrgEmbedded.builder()
      .debtPositionTypeOrgs(expectedDebtPositionTypeOrgs)
      .build();
    CollectionModelDebtPositionTypeOrg collectionModel = CollectionModelDebtPositionTypeOrg.builder()
      .embedded(embedded)
      .build();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(String.valueOf(organizationId), operatorExternalUserId))
      .thenReturn(collectionModel);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgClient.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken);

    Assertions.assertNotNull(result);
    Assertions.assertEquals(collectionModel.getEmbedded().getDebtPositionTypeOrgs(), result);
  }
}
