package it.gov.pagopa.pu.classification.connector.workflowhub.config;

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

@ExtendWith(MockitoExtension.class)
class WorkflowHubApisHolderTest extends BaseApiHolderTest {

  @Mock
  private RestTemplateBuilder restTemplateBuilderMock;

  private WorkflowHubApisHolder workflowHubApisHolder;

  @BeforeEach
  void setUp() {
    Mockito.when(restTemplateBuilderMock.build())
      .thenReturn(restTemplateMock);
    Mockito.when(restTemplateMock.getUriTemplateHandler())
      .thenReturn(new DefaultUriBuilderFactory());
    WorkflowHubApiClientConfig clientConfig = WorkflowHubApiClientConfig.builder()
      .baseUrl("http://example.com")
      .build();
    workflowHubApisHolder = new WorkflowHubApisHolder(clientConfig, restTemplateBuilderMock);
  }

  @AfterEach
  void verifyNoMoreInteraction() {
    Mockito.verifyNoMoreInteractions(
      restTemplateBuilderMock,
      restTemplateMock
    );
  }

  @Test
  void whenGetClassificationApiThenAuthenticationShouldBeSetInThreadSafeMode() throws InterruptedException {
    assertAuthenticationShouldBeSetInThreadSafeMode(
      accessToken -> workflowHubApisHolder.getClassificationApi(accessToken)
        .assessmentsClassification(1L, "IUV", "IUD"),
      new ParameterizedTypeReference<>() {},
      workflowHubApisHolder::unload);
  }

}
