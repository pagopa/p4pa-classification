package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.classification.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.debtpositions.client.generated.DebtPositionTypeOrgSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.CollectionModelDebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtpositions.dto.generated.PagedModelDebtPositionTypeOrgEmbedded;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionTypeOrgClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApiMock;

  @InjectMocks
  private DebtPositionTypeOrgClient debtPositionTypeOrgClient;

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, debtPositionTypeOrgSearchControllerApiMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgByInstallmentIdThenReturnIt() {
    String accessToken = "ACCESSTOKEN";
    Long installmentId = 1L;
    DebtPositionTypeOrg expectedDebtPositionTypeOrg = new DebtPositionTypeOrg();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsGetDebtPositionTypeOrgByInstallmentId(installmentId))
      .thenReturn(expectedDebtPositionTypeOrg);

    DebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrgByInstallmentId(installmentId, accessToken);

    Assertions.assertSame(expectedDebtPositionTypeOrg, result);
  }

  @Test
  void givenNotExistentInstallmentIdWhenGetDebtPositionTypeOrgByInstallmentIdThenReturnNull() {
    String accessToken = "ACCESSTOKEN";
    Long installmentId = 1L;

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsGetDebtPositionTypeOrgByInstallmentId(installmentId))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    DebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrgByInstallmentId(installmentId, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void whenFindDebtPositionTypeOrgsThenReturnIt() {
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
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindDebtPositionTypeOrgs(organizationId, operatorExternalUserId, true))
      .thenReturn(collectionModel);

    List<DebtPositionTypeOrg> result = debtPositionTypeOrgClient.findDebtPositionTypeOrgs(organizationId,  operatorExternalUserId, true, accessToken);

    Assertions.assertSame(expectedDebtPositionTypeOrgs, result);
  }

  @Test
  void whenGetDebtPositionTypeOrgByDebtPositionTypeOrgIdThenReturnDebtPositionTypeOrg() {
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    Long debtPositionTypeOrgId = 3L;
    DebtPositionTypeOrg expectedDebtPositionTypeOrg = new DebtPositionTypeOrg();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindByOrganizationIdAndDebtPositionTypeOrgId(organizationId, debtPositionTypeOrgId))
      .thenReturn(expectedDebtPositionTypeOrg);

    DebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrgByDebtPositionTypeOrgId(organizationId, debtPositionTypeOrgId, accessToken);

    Assertions.assertSame(expectedDebtPositionTypeOrg, result);
  }

  @Test
  void whenGetDebtPositionTypeOrgByDebtPositionTypeOrgCodeThenReturnIt() {
    // Given
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "CODE";
    DebtPositionTypeOrg expectedDebtPositionTypeOrg = new DebtPositionTypeOrg();

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindByOrganizationIdAndCode(organizationId, debtPositionTypeOrgCode))
      .thenReturn(expectedDebtPositionTypeOrg);

    // When
    DebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrgByDebtPositionTypeOrgCode(organizationId, debtPositionTypeOrgCode, accessToken);

    // Then
    Assertions.assertSame(expectedDebtPositionTypeOrg, result);
  }

  @Test
  void givenNotExistentCodeWhenGetDebtPositionTypeOrgByDebtPositionTypeOrgCodeThenReturnNull() {
    // Given
    String accessToken = "ACCESSTOKEN";
    Long organizationId = 1L;
    String debtPositionTypeOrgCode = "CODE";

    when(debtPositionApisHolderMock.getDebtPositionTypeOrgSearchControllerApi(accessToken))
      .thenReturn(debtPositionTypeOrgSearchControllerApiMock);
    when(debtPositionTypeOrgSearchControllerApiMock.crudDebtPositionTypeOrgsFindByOrganizationIdAndCode(organizationId, debtPositionTypeOrgCode))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    // When
    DebtPositionTypeOrg result = debtPositionTypeOrgClient.getDebtPositionTypeOrgByDebtPositionTypeOrgCode(organizationId, debtPositionTypeOrgCode, accessToken);

    // Then
    Assertions.assertNull(result);
  }


}
