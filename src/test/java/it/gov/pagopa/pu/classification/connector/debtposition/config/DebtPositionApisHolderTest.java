package it.gov.pagopa.pu.classification.connector.debtposition.config;

import it.gov.pagopa.pu.classification.config.json.JsonConfig;
import it.gov.pagopa.pu.classification.connector.BaseApiHolderTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DebtPositionApisHolderTest extends BaseApiHolderTest {
  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private DebtPositionApisHolder apisHolder;
  private DebtPositionApiClientConfig apiClientConfig;

  @BeforeEach
  void setUp() {
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

    apiClientConfig = DebtPositionApiClientConfig.builder()
      .baseUrl("http://example.com")
      .maxAttempts(3)
      .build();
    apisHolder = new DebtPositionApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

    verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getInstallmentNoPIISearchControllerApi(null));
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      restTemplateBuilderMock,
      restTemplateMock
    );
  }

  @Test
  void testRetryConfiguration() {
    assertRetry(apiClientConfig,
      accessToken -> apisHolder.getInstallmentNoPIISearchControllerApi(accessToken)
        .crudInstallmentsGetByOrganizationIdAndReceiptId(0L, 1L, null),
      new ParameterizedTypeReference<>() {}
    );
  }

  @Test
  void whenGetInstallmentNoPiiSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getInstallmentNoPIISearchControllerApi(accessToken)
        .crudInstallmentsGetByOrganizationIdAndReceiptId(0L, 1L, null),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetReceiptNoPiiEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getReceiptNoPiiEntityControllerApi(accessToken)
        .crudGetReceiptnopii("1"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgsGetDebtPositionTypeOrgByInstallmentId(1L),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetReceiptNoPiiSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getReceiptNoPiiSearchControllerApi(accessToken)
        .crudReceiptsGetByReceiptIdAndDebtPositionTypeOrgCode(1L,"debtPositionTypeOrgCode"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetTransferSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getTransferSearchControllerApi(accessToken)
        .crudTransfersFindByInstallmentId(1L),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }

  @Test
  void whenGetDebtPositionTypeOrgBalanceCostSearchControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> apisHolder.getDebtPositionTypeOrgBalanceCostSearchControllerApi(accessToken)
        .crudDebtPositionTypeOrgBalanceCostsGetByDebtPositionTypeOrgIdAndOperatingYear(1L, "2026"),
      new ParameterizedTypeReference<>() {
      },
      apisHolder::unload);
  }
}
