package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.model.PaymentsReporting;
import it.gov.pagopa.pu.classification.repository.PaymentsReportingRepository;
import it.gov.pagopa.pu.classification.util.TestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PaymentsReportingServiceTest {

  @Mock
  private PaymentsReportingRepository repositoryMock;

  private PaymentsReportingService service;

  private final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @BeforeEach
  void setUp() {
    service = new PaymentsReportingService(repositoryMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      repositoryMock);
  }

  @Test
  void whenFindAndDeleteByOrgIdAndIufAndIngestionFlowFileIdThenInvokeRepository() {
    // Given
    Long organizationId = 1L;
    String iuf = "IUF";
    Long ingestionFlowFileId = 1L;

    PaymentsReporting paymentsReporting = podamFactory.manufacturePojo(PaymentsReporting.class);
    List<PaymentsReporting> entities = List.of(paymentsReporting);

    paymentsReporting.setDeleted(true);
    List<PaymentsReporting> entitiesDeleted = List.of(paymentsReporting);

    Mockito.when(repositoryMock.findByOrganizationIdAndIufAndIngestionFlowFileIdNot(organizationId, iuf, ingestionFlowFileId))
      .thenReturn(entities);
    Mockito.when(repositoryMock.saveAll(entities)).thenReturn(entitiesDeleted);

    // When
    List<PaymentsReporting> actualResult = service.findAndDeleteByOrgIdAndIufAndIngestionFlowFileIdNot(organizationId, iuf, ingestionFlowFileId);

    // Then
    Assertions.assertEquals(entitiesDeleted, actualResult);
  }
}
