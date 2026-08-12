package it.gov.pagopa.pu.classification.connector.workflowhub.config;

import it.gov.pagopa.pu.classification.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.pu.classification.connector.workflowhub.mapper.WorkflowErrorDTOMapper;
import it.gov.pagopa.pu.workflowhub.client.generated.ClassificationApi;
import it.gov.pagopa.pu.workflowhub.dto.generated.WorkflowErrorDTO;
import it.gov.pagopa.pu.workflowhub.generated.ApiClient;
import it.gov.pagopa.pu.workflowhub.generated.BaseApi;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

@Service
public class WorkflowHubApisHolder {

  private final ClassificationApi classificationApi;

  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public WorkflowHubApisHolder(
    WorkflowHubApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder,
    JsonMapper jsonMapper
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    restTemplate.setErrorHandler(new HttpClientErrorJsonBodyHandler<>(jsonMapper, "WORKFLOW-HUB", clientConfig.isPrintBodyWhenError(),
      WorkflowErrorDTO.class, WorkflowErrorDTOMapper::map)
    );

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
