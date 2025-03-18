package it.gov.pagopa.pu.classification.connector.debtposition.config;

import it.gov.pagopa.pu.classification.config.RestTemplateConfig;
import it.gov.pagopa.pu.debtposition.client.generated.InstallmentNoPiiSearchControllerApi;
import it.gov.pagopa.pu.debtposition.generated.ApiClient;
import it.gov.pagopa.pu.debtposition.generated.BaseApi;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Lazy
@Service
public class DebtPositionApisHolder {
  private final InstallmentNoPiiSearchControllerApi installmentNoPiiSearchControllerApi;
  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public DebtPositionApisHolder(
    DebtPositionApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    if (clientConfig.isPrintBodyWhenError()) {
      restTemplate.setErrorHandler(RestTemplateConfig.bodyPrinterWhenError("DEBT-POSITIONS"));
    }

    this.installmentNoPiiSearchControllerApi = new InstallmentNoPiiSearchControllerApi(apiClient);
  }

  @PreDestroy
  public void unload() {
    bearerTokenHolder.remove();
  }

  public InstallmentNoPiiSearchControllerApi getInstallmentNoPIISearchControllerApi(String accessToken) {
    return getApi(accessToken, installmentNoPiiSearchControllerApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }
}
