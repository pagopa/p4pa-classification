package it.gov.pagopa.pu.classification.connector.debtposition.client;

import it.gov.pagopa.pu.classification.connector.debtposition.config.DebtPositionApisHolder;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.debtposition.client.generated.InstallmentNoPiiSearchControllerApi;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelInstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.CollectionModelInstallmentNoPIIEmbedded;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstallmentNoPIIClientTest {

  @Mock
  private DebtPositionApisHolder debtPositionApisHolderMock;
  @Mock
  private InstallmentNoPiiSearchControllerApi installmentNoPiiSearchControllerApiMock;

  @InjectMocks
  private InstallmentNoPIIClient installmentNoPIIClient;

  @AfterEach
  void v(){
    Mockito.verifyNoMoreInteractions(
      debtPositionApisHolderMock,
      installmentNoPiiSearchControllerApiMock
    );
  }

  private static final PodamFactory podamFactory = TestUtils.getPodamFactory();

	@Test
	void getByReceiptId_withValidReceiptId_returnsInstallments() {
		String accessToken = "ACCESSTOKEN";
    Long organizationId = 0L;
		Long receiptId = 1L;
		List<InstallmentNoPII> expectedInstallments = List.of(new InstallmentNoPII());
		CollectionModelInstallmentNoPIIEmbedded embedded = CollectionModelInstallmentNoPIIEmbedded.builder()
				.installmentNoPIIs(expectedInstallments).build();
		CollectionModelInstallmentNoPII collectionModel = CollectionModelInstallmentNoPII.builder()
				.embedded(embedded)
				.build();

		when(debtPositionApisHolderMock.getInstallmentNoPIISearchControllerApi(accessToken))
				.thenReturn(installmentNoPiiSearchControllerApiMock);
		when(installmentNoPiiSearchControllerApiMock.crudInstallmentsGetByOrganizationIdAndReceiptId(organizationId, receiptId, null))
				.thenReturn(collectionModel);

		List<InstallmentNoPII> result = installmentNoPIIClient.getByReceiptId(organizationId, receiptId, accessToken);

		Assertions.assertEquals(expectedInstallments, result);
	}

	@Test
	void whenFindByOrganizationIdAndIudsThenOk(){
		String accessToken = "ACCESSTOKEN";
		Long organizationId = 1L;
		Set<String> iudSet = Collections.singleton("iud");
		CollectionModelInstallmentNoPII collectionModelInstallmentNoPII = podamFactory.manufacturePojo(CollectionModelInstallmentNoPII.class);

		when(debtPositionApisHolderMock.getInstallmentNoPIISearchControllerApi(accessToken))
				.thenReturn(installmentNoPiiSearchControllerApiMock);
		when(installmentNoPiiSearchControllerApiMock.crudInstallmentsFindByOrganizationIdAndIuds(organizationId,iudSet))
				.thenReturn(collectionModelInstallmentNoPII);

		List<InstallmentNoPII> result = installmentNoPIIClient.findByOrganizationIdAndIuds(organizationId,iudSet,accessToken);

		Assertions.assertEquals(collectionModelInstallmentNoPII.getEmbedded().getInstallmentNoPIIs(),result);
	}

	@Test
	void givenNoInstallmentsWhenFindByOrganizationIdAndIudsThenEmptyList(){
		String accessToken = "ACCESSTOKEN";
		Long organizationId = 1L;
		Set<String> iudSet = Collections.singleton("iud");

		when(debtPositionApisHolderMock.getInstallmentNoPIISearchControllerApi(accessToken))
				.thenReturn(installmentNoPiiSearchControllerApiMock);
		when(installmentNoPiiSearchControllerApiMock.crudInstallmentsFindByOrganizationIdAndIuds(organizationId,iudSet))
				.thenReturn(null);

		List<InstallmentNoPII> result = installmentNoPIIClient.findByOrganizationIdAndIuds(organizationId,iudSet,accessToken);

		Assertions.assertTrue(result.isEmpty());
	}
}
