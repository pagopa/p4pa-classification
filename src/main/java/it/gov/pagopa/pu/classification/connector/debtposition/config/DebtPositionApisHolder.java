package it.gov.pagopa.pu.classification.connector.debtposition.config;

import it.gov.pagopa.pu.classification.config.rest.HttpClientErrorJsonBodyHandler;
import it.gov.pagopa.pu.classification.connector.debtposition.mapper.DebtPositionErrorDTOMapper;
import it.gov.pagopa.pu.debtpositions.client.generated.*;
import it.gov.pagopa.pu.debtpositions.dto.generated.DebtPositionErrorDTO;
import it.gov.pagopa.pu.debtpositions.generated.ApiClient;
import it.gov.pagopa.pu.debtpositions.generated.BaseApi;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.json.JsonMapper;

@Lazy
@Service
public class DebtPositionApisHolder {

  private final InstallmentNoPiiSearchControllerApi installmentNoPiiSearchControllerApi;
  private final ReceiptNoPiiEntityControllerApi receiptNoPiiEntityControllerApi;
  private final DebtPositionTypeOrgSearchControllerApi debtPositionTypeOrgSearchControllerApi;
  private final ReceiptNoPiiSearchControllerApi receiptNoPiiSearchControllerApi;
  private final TransferSearchControllerApi transferSearchControllerApi;
  private final DebtPositionTypeOrgBalanceCostSearchControllerApi debtPositionTypeOrgBalanceCostSearchControllerApi;

  private final ThreadLocal<String> bearerTokenHolder = new ThreadLocal<>();

  public DebtPositionApisHolder(
    DebtPositionApiClientConfig clientConfig,
    RestTemplateBuilder restTemplateBuilder,
    JsonMapper jsonMapper
  ) {
    RestTemplate restTemplate = restTemplateBuilder.build();
    ApiClient apiClient = new ApiClient(restTemplate);
    apiClient.setBasePath(clientConfig.getBaseUrl());
    apiClient.setBearerToken(bearerTokenHolder::get);
    apiClient.setMaxAttemptsForRetry(Math.max(1, clientConfig.getMaxAttempts()));
    apiClient.setWaitTimeMillis(clientConfig.getWaitTimeMillis());
    restTemplate.setErrorHandler(new HttpClientErrorJsonBodyHandler<>(jsonMapper, "DEBT-POSITIONS", clientConfig.isPrintBodyWhenError(),
      DebtPositionErrorDTO.class, DebtPositionErrorDTOMapper::map)
    );

    this.installmentNoPiiSearchControllerApi = new InstallmentNoPiiSearchControllerApi(apiClient);
    this.receiptNoPiiEntityControllerApi = new ReceiptNoPiiEntityControllerApi(apiClient);
    this.debtPositionTypeOrgSearchControllerApi = new DebtPositionTypeOrgSearchControllerApi(apiClient);
    this.receiptNoPiiSearchControllerApi = new ReceiptNoPiiSearchControllerApi(apiClient);
    this.transferSearchControllerApi = new TransferSearchControllerApi(apiClient);
    this.debtPositionTypeOrgBalanceCostSearchControllerApi = new DebtPositionTypeOrgBalanceCostSearchControllerApi(apiClient);
  }

  @PreDestroy
  public void unload() {
    bearerTokenHolder.remove();
  }

  public InstallmentNoPiiSearchControllerApi getInstallmentNoPIISearchControllerApi(String accessToken) {
    return getApi(accessToken, installmentNoPiiSearchControllerApi);
  }

  public ReceiptNoPiiEntityControllerApi getReceiptNoPiiEntityControllerApi(String accessToken) {
    return getApi(accessToken, receiptNoPiiEntityControllerApi);
  }

  public DebtPositionTypeOrgSearchControllerApi getDebtPositionTypeOrgSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgSearchControllerApi);
  }

  public ReceiptNoPiiSearchControllerApi getReceiptNoPiiSearchControllerApi(String accessToken) {
    return getApi(accessToken, receiptNoPiiSearchControllerApi);
  }

  public TransferSearchControllerApi getTransferSearchControllerApi(String accessToken) {
    return getApi(accessToken, transferSearchControllerApi);
  }

  public DebtPositionTypeOrgBalanceCostSearchControllerApi getDebtPositionTypeOrgBalanceCostSearchControllerApi(String accessToken) {
    return getApi(accessToken, debtPositionTypeOrgBalanceCostSearchControllerApi);
  }

  private <T extends BaseApi> T getApi(String accessToken, T api) {
    bearerTokenHolder.set(accessToken);
    return api;
  }
}
