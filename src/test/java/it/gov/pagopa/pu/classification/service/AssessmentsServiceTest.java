package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentNoPIIService;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssessmentsServiceTest {
  @Mock
  private InstallmentNoPIIService installmentNoPIIServiceMock;

  private AssessmentsService service;

  @BeforeEach
  void init() {
    service = new AssessmentsService(installmentNoPIIServiceMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(installmentNoPIIServiceMock);
  }


  @Test
  void getInstallmentsByReceiptId_withValidReceiptId_returnsInstallments() {
    Long receiptId = 1L;
    List<InstallmentNoPIIResponse> expectedInstallments = List.of(new InstallmentNoPIIResponse());
    when(installmentNoPIIServiceMock.getByReceiptId(receiptId,TestUtils.getFakeAccessToken())).thenReturn(expectedInstallments);

    List<InstallmentNoPIIResponse> result = service.getInstallmentsByReceiptId(receiptId,TestUtils.getFakeAccessToken());

    assertEquals(expectedInstallments, result);
  }

  @Test
  void getInstallmentsByReceiptId_withInvalidReceiptId_returnsEmptyList() {
    Long receiptId = 2L;
    when(installmentNoPIIServiceMock.getByReceiptId(receiptId,TestUtils.getFakeAccessToken())).thenReturn(Collections.emptyList());

    List<InstallmentNoPIIResponse> result = service.getInstallmentsByReceiptId(receiptId, TestUtils.getFakeAccessToken());

    assertTrue(result.isEmpty());
  }

  @Test
  void getInstallmentsByReceiptId_withNullReceiptId_returnsEmptyList() {
    Long receiptId = null;
    when(installmentNoPIIServiceMock.getByReceiptId(receiptId,TestUtils.getFakeAccessToken())).thenReturn(Collections.emptyList());

    List<InstallmentNoPIIResponse> result = service.getInstallmentsByReceiptId(receiptId,TestUtils.getFakeAccessToken());

    assertTrue(result.isEmpty());
  }
}
