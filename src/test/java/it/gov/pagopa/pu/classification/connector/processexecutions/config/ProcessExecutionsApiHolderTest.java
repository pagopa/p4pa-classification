package it.gov.pagopa.pu.classification.connector.processexecutions.config;

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
class ProcessExecutionsApiHolderTest extends BaseApiHolderTest {
  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private ProcessExecutionsApisHolder apisHolder;
  private ProcessExecutionsApiClientConfig apiClientConfig;

  @BeforeEach
  void setUp() {
    when(restTemplateBuilderMock.build()).thenReturn(restTemplateMock);
    when(restTemplateMock.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory());

    apiClientConfig = ProcessExecutionsApiClientConfig.builder()
      .baseUrl("http://example.com")
      .maxAttempts(3)
      .build();
    apisHolder = new ProcessExecutionsApisHolder(apiClientConfig, restTemplateBuilderMock, new JsonConfig().objectMapperJackson3());

    verifyHttpClientErrorJsonBodyHandlerConfiguration(apisHolder.getIngestionFlowFileEntityControllerApi(null));
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
      accessToken ->
        apisHolder.getIngestionFlowFileEntityControllerApi(accessToken)
          .crudGetIngestionflowfile("123"),
      new ParameterizedTypeReference<>() {}
    );
  }

  @Test
  void whenGetIngestionFlowFileEntityControllerApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken ->
        apisHolder.getIngestionFlowFileEntityControllerApi(accessToken)
          .crudGetIngestionflowfile("123"),
      new ParameterizedTypeReference<>() {},
      apisHolder::unload);
  }
}
