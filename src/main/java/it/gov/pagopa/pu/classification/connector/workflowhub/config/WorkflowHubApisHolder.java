package it.gov.pagopa.pu.classification.connector.workflowhub.config;

import it.gov.pagopa.pu.classification.config.rest.RestTemplateConfig;
import it.gov.pagopa.pu.workflow.client.generated.ClassificationApi;
import it.gov.pagopa.pu.workflow.generated.ApiClient;
import it.gov.pagopa.pu.workflow.generated.BaseApi;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WorkflowHubApisHolder {

  private final ClassificationApi classificationApi;

  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public WorkflowHubApisHolder(
    WorkflowHubApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    if (clientConfig.isPrintBodyWhenError()) {
      restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("WORKFLOW-HUB"));
    }

    this.classificationApi = new ClassificationApi(apiClient);

  }

  @PreDestroy
  public void unload() {
    bearerTokenHolder.remove();
  }

  public ClassificationApi getClassificationApi(
    String accessToken) {
    return getApi(accessToken, classificationApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }
}
