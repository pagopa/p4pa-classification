package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.DebtPositionTypeOrgBalanceCostClient;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelDebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrgBalanceCost;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrgBalanceCostType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgBalanceCostServiceImplTest {
  @Mock
  private DebtPositionTypeOrgBalanceCostClient debtPositionTypeOrgBalanceCostClientMock;

  private DebtPositionTypeOrgBalanceCostService service;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    service = new DebtPositionTypeOrgBalanceCostServiceImpl(debtPositionTypeOrgBalanceCostClientMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(debtPositionTypeOrgBalanceCostClientMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYearThenInvokeClient() {
    long dptoId = 1L;
    String opYear = "2026";
    String accessToken = "accessToken";

    CollectionModelDebtPositionTypeOrgBalanceCost clientResponse = podamFactory.manufacturePojo(CollectionModelDebtPositionTypeOrgBalanceCost.class);
    List<DebtPositionTypeOrgBalanceCost> expectedList = clientResponse.getEmbedded().getDebtPositionTypeOrgBalanceCosts();

    when(debtPositionTypeOrgBalanceCostClientMock.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(dptoId, opYear, accessToken))
      .thenReturn(clientResponse);

    List<DebtPositionTypeOrgBalanceCost> result = service.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(dptoId, opYear, accessToken);

    assertSame(expectedList, result);
  }

  @Test
  void whenGetDptoBalanceCostByInstallmentIdAndTypeAndOperatingYearThenInvokeClient() {
    long installmentId = 1L;
    DebtPositionTypeOrgBalanceCostType debtPositionTypeOrgBalanceCostType = DebtPositionTypeOrgBalanceCostType.NOTIFICATION_COST;
    String operatingYear = "2026";
    String accessToken = "accessToken";

    DebtPositionTypeOrgBalanceCost expectedResult = podamFactory.manufacturePojo(DebtPositionTypeOrgBalanceCost.class);

    when(debtPositionTypeOrgBalanceCostClientMock.getByInstallmentIdAndTypeAndOperatingYear(installmentId, debtPositionTypeOrgBalanceCostType, operatingYear, accessToken))
      .thenReturn(expectedResult);

    DebtPositionTypeOrgBalanceCost result = service.getDptoBalanceCostByInstallmentIdAndTypeAndOperatingYear(installmentId, debtPositionTypeOrgBalanceCostType, operatingYear, accessToken);

    assertSame(expectedResult, result);
  }
}
