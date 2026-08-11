package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.classification.exception.common.RestInvokeNotFoundException;
import it.gov.pagopa.pu.debtpositions.client.generated.ReceiptNoPiiEntityControllerApi;
import it.gov.pagopa.pu.debtpositions.client.generated.ReceiptNoPiiSearchControllerApi;
import it.gov.pagopa.pu.debtpositions.dto.generated.ReceiptNoPII;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReceiptNoPIIClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private ReceiptNoPiiEntityControllerApi receiptNoPiiEntityControllerApiMock;
  @Mock
  private ReceiptNoPiiSearchControllerApi receiptNoPiiSearchControllerApiMock;

  @InjectMocks
  private ReceiptNoPIIClient client;

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(debtPositionApisHolderMock, receiptNoPiiEntityControllerApiMock, receiptNoPiiSearchControllerApiMock);
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
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    ReceiptNoPII result = client.getById(receiptId, accessToken);

    Assertions.assertNull(result);
  }

  @Test
  void whenGetByReceiptIdAndDebtPositionTypeOrgCodeThenOk() {
    String accessToken = "ACCESSTOKEN";
    long receiptId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";
    ReceiptNoPII expectedResult = new ReceiptNoPII();

    when(debtPositionApisHolderMock.getReceiptNoPiiSearchControllerApi(accessToken))
      .thenReturn(receiptNoPiiSearchControllerApiMock);
    when(receiptNoPiiSearchControllerApiMock.crudReceiptsGetByReceiptIdAndDebtPositionTypeOrgCode(receiptId,debtPositionTypeOrgCode))
      .thenReturn(expectedResult);

    ReceiptNoPII result = client.getByReceiptIdAndDebtPositionTypeOrgCode(receiptId, debtPositionTypeOrgCode, accessToken);

    Assertions.assertSame(expectedResult, result);
  }

  @Test
  void givenNotExistentReceiptWhenGetByReceiptIdAndDebtPositionTypeOrgCodeThenReturnNull() {
    String accessToken = "ACCESSTOKEN";
    long receiptId = 1L;
    String debtPositionTypeOrgCode = "debtPositionTypeOrgCode";

    when(debtPositionApisHolderMock.getReceiptNoPiiSearchControllerApi(accessToken))
            .thenReturn(receiptNoPiiSearchControllerApiMock);
    when(receiptNoPiiSearchControllerApiMock.crudReceiptsGetByReceiptIdAndDebtPositionTypeOrgCode(receiptId,debtPositionTypeOrgCode))
      .thenThrow(new RestInvokeNotFoundException("APPNAME", HttpStatus.NOT_FOUND, "ERROR", "ERRORCODE", "ERRORMESSAGE"));

    ReceiptNoPII result = client.getByReceiptIdAndDebtPositionTypeOrgCode(receiptId, debtPositionTypeOrgCode, accessToken);

    Assertions.assertNull(result);
  }
}
