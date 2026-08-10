package it.gov.pagopa.pu.classification.connector.processexecutions.config;

import it.gov.pagopa.pu.classification.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.pu.processexecutions.generated.ApiClient;
import it.gov.pagopa.pu.processexecutions.generated.BaseApi;
import it.gov.pagopa.pu.processexecutions.client.generated.IngestionFlowFileEntityControllerApi;
import it.gov.pagopa.pu.processexecutions.dto.generated.ProcessExecutionsErrorDTO;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

@Service
public class ProcessExecutionsApisHolder {

  private final IngestionFlowFileEntityControllerApi ingestionFlowFileEntityControllerApi;

  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public ProcessExecutionsApisHolder(
    ProcessExecutionsApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder,
    JsonMapper jsonMapper
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    restTemplate.setErrorHandler(new HttpClientErrorJsonBodyHandler<>(jsonMapper, "PROCESS-EXECUTIONs", clientConfig.isPrintBodyWhenError(),
      ProcessExecutionsErrorDTO.class, ProcessExecutionsErrorDTO::getCode, ProcessExecutionsErrorDTO::getMessage)
    );

    this.ingestionFlowFileEntityControllerApi = new IngestionFlowFileEntityControllerApi(
      apiClient);

  }

  @PreDestroy
  public void unload() {
    bearerTokenHolder.remove();
  }

  public IngestionFlowFileEntityControllerApi getIngestionFlowFileEntityControllerApi(
    String accessToken) {
    return getApi(accessToken, ingestionFlowFileEntityControllerApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }
}
