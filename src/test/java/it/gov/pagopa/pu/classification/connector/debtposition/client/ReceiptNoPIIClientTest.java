package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.debtposition.client.generated.ReceiptNoPiiEntityControllerApi;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptNoPIIClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private ReceiptNoPiiEntityControllerApi receiptNoPiiEntityControllerApiMock;

  @InjectMocks
  private ReceiptNoPIIClient client;

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, receiptNoPiiEntityControllerApiMock);
  }

  @Test
  void whenGetDebtPositionTypeOrgByInstallmentIdThenReturnIt() {
    String accessToken = "ACCESSTOKEN";
    long receiptId = 1L;
    ReceiptNoPII expectedResult = new ReceiptNoPII();

    when(debtPositionApisHolderMock.getReceiptNoPiiEntityControllerApi(accessToken))
      .thenReturn(receiptNoPiiEntityControllerApiMock);
    when(receiptNoPiiEntityControllerApiMock.crudGetReceiptnopii(String.valueOf(receiptId)))
      .thenReturn(expectedResult);

    ReceiptNoPII result = client.getById(receiptId, accessToken);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNotExistentInstallmentIdWhenGetDebtPositionTypeOrgByInstallmentIdThenReturnNull() {
    String accessToken = "ACCESSTOKEN";
    long receiptId = 1L;

    when(debtPositionApisHolderMock.getReceiptNoPiiEntityControllerApi(accessToken))
      .thenReturn(receiptNoPiiEntityControllerApiMock);
    when(receiptNoPiiEntityControllerApiMock.crudGetReceiptnopii(String.valueOf(receiptId)))
      .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "NotFound", null, null, null));

    ReceiptNoPII result = client.getById(receiptId, accessToken);

    Assertions.assertNull(result);
  }
}
