package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.classification.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.debtpositions.client.generated.DebtPositionTypeOrgBalanceCostSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrgBalanceCostType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertNull;
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

  @Test
  void whenGetByInstallmentIdAndTypeAndOperatingYearThenInvokeWithAccessToken() {
    long installmentId = 1L;
    DebtPositionTypeOrgBalanceCostType debtPositionTypeOrgBalanceCostType = DebtPositionTypeOrgBalanceCostType.NOTIFICATION_COST;
    String operatingYear = "2025";
    String accessToken = "accessToken";

    DebtPositionTypeOrgBalanceCost expectedResult = new DebtPositionTypeOrgBalanceCost();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgBalanceCostSearchControllerApiMock);
    when(debtPositionTypeOrgBalanceCostSearchControllerApiMock.crudDebtPositionTypeOrgBalanceCostsGetByInstallmentIdAndTypeAndOperatingYear(installmentId, debtPositionTypeOrgBalanceCostType, operatingYear))
      .thenReturn(expectedResult);

    DebtPositionTypeOrgBalanceCost result = debtPositionTypeOrgBalanceCostClient.getByInstallmentIdAndTypeAndOperatingYear(installmentId, debtPositionTypeOrgBalanceCostType, operatingYear, accessToken);

    assertSame(expectedResult, result);
  }

  @Test
  void givenNotFoundWhenGetByInstallmentIdAndTypeAndOperatingYearThenNull() {
    long installmentId = 1L;
    DebtPositionTypeOrgBalanceCostType debtPositionTypeOrgBalanceCostType = DebtPositionTypeOrgBalanceCostType.NOTIFICATION_COST;
    String operatingYear = "2025";
    String accessToken = "accessToken";


    when(debtPositionApisHolderMock.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgBalanceCostSearchControllerApiMock);
    when(debtPositionTypeOrgBalanceCostSearchControllerApiMock.crudDebtPositionTypeOrgBalanceCostsGetByInstallmentIdAndTypeAndOperatingYear(installmentId, debtPositionTypeOrgBalanceCostType, operatingYear))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    DebtPositionTypeOrgBalanceCost result = debtPositionTypeOrgBalanceCostClient.getByInstallmentIdAndTypeAndOperatingYear(installmentId, debtPositionTypeOrgBalanceCostType, operatingYear, accessToken);

    assertNull(result);
  }
}
