package it.gov.pagopa.pu.classification.connector.debtposition;

import it.gov.pagopa.pu.classification.connector.debtposition.client.InstallmentNoPIIClient;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstallmentServiceTest {

	@Mock
	private InstallmentNoPIIClient clientMock;

	@InjectMocks
	private InstallmentServiceImpl service;

	private static final PodamFactory podamFactory = TestUtils.getPodamFactory();

  @AfterEach
  void verifyNoMoreInteractions(){
    Mockito.verifyNoMoreInteractions(clientMock);
  }

	@Test
	void whenGetByReceiptIdThenInvokeClient() {
		// Given
		String accessToken = "ACCESSTOKEN";
		Long receiptId = 1L;
    List<InstallmentNoPII> expected = List.of();

		when(clientMock.getByReceiptId(receiptId, accessToken)).thenReturn(expected);

		// When
		List<InstallmentNoPII> result = service.getByReceiptId(receiptId, accessToken);

		// Then
		assertSame(expected, result);
	}

	@Test
	void whenFindByOrganizationIdAndIudsThenInvokeClient() {
		// Given
		String accessToken = "ACCESSTOKEN";
		Long organizationId = 1L;
		Set<String> iudSet = Collections.singleton("iud");
		List<InstallmentNoPII> expectedResult = podamFactory.manufacturePojo(List.class,InstallmentNoPII.class);

		when(clientMock.findByOrganizationIdAndIuds(organizationId,iudSet, accessToken)).thenReturn(expectedResult);

		// When
		List<InstallmentNoPII> result = service.findByOrganizationIdAndIuds(organizationId,iudSet,accessToken);

		// Then
		assertSame(expectedResult, result);
	}
}
