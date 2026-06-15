package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.client.generated.DebtPositionTypeOrgBalanceCostSearchControllerApi;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgBalanceCostClientTest {
  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgBalanceCostSearchControllerApi debtPositionTypeOrgBalanceCostSearchControllerApiMock;

  private  DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClient;

  @BeforeEach
  void setUp() {
    debtPositionTypeOrgBalanceCostClient = new DebtPositionTypeOrgBalanceCostClient(debtPositionApisHolderMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, debtPositionTypeOrgBalanceCostSearchControllerApiMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearThenInvokeWithAccessToken() {
    long dptoId = 1L;
    String opYear = "2025";
    String accessToken = "accessToken";

    CollectionModelDebtPositionTypeOrgBalanceCost expectedResult = new CollectionModelDebtPositionTypeOrgBalanceCost();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgBalanceCostSearchControllerApiMock);
    when(debtPositionTypeOrgBalanceCostSearchControllerApiMock.crudDebtPositionTypeOrgBalanceCostsGetByDebtPositionTypeOrgIdAndOperatingYear(dptoId, opYear))
      .thenReturn(expectedResult);

    CollectionModelDebtPositionTypeOrgBalanceCost result = debtPositionTypeOrgBalanceCostClient.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(dptoId, opYear, accessToken);

    assertSame(expectedResult, result);
  }
}
