package it.gov.pagopa.pu.classification.service.assessments;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.service.BalanceUnmashallerService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentDTO;
import it.gov.pagopa.pu.debtposition.dto.generated.PaymentOptionDTO;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamento;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryServiceImplTest {

  @Mock
  private AssessmentsRegistryRepository assessmentsRegistryRepositoryMock;
  @Mock
  private BalanceUnmashallerService balanceUnmashallerServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;

  @InjectMocks
  private AssessmentsRegistryServiceImpl assessmentsRegistryService;

  private static final String BALANCE =
    "<bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/\">>" +
      "<capitolo>" +
      "<codCapitolo>CAP1</codCapitolo>" +
      "<codUfficio>UFF1</codUfficio>" +
      "<accertamento>" +
      "<codAccertamento>ACC1</codAccertamento>" +
      "<importo>100.00</importo>" +
      "</accertamento>" +
      "</capitolo>" +
      "</bilancio>";

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      assessmentsRegistryRepositoryMock,
      balanceUnmashallerServiceMock,
      debtPositionTypeOrgServiceMock
      );
  }

  @Test
  void givenValidRequestWhenCreateAssessmentsRegistryByDebtPositionDTOAndIudThenOk() {
    // Given
    String externalUserId = "USERID";
    String traceId = "TRACEID";
    String accessToken = "token";
    Long installmentId = 1L;
    String iud = "IUD";
    OffsetDateTime creationDate = OffsetDateTime.now();


    // Mock InstallmentDTO
    InstallmentDTO installmentDTO = TestUtils.getPodamFactory().manufacturePojo(InstallmentDTO.class);
    installmentDTO.setInstallmentId(installmentId);
    installmentDTO.setBalance(BALANCE);
    installmentDTO.setIud(iud);
    installmentDTO.setCreationDate(creationDate);

    // Mock PaymentOptionDTO
    PaymentOptionDTO paymentOptionDTO = TestUtils.getPodamFactory().manufacturePojo(PaymentOptionDTO.class);
    paymentOptionDTO.setInstallments(List.of(installmentDTO));

    // Mock DebtPositionDTO
    DebtPositionDTO debtPositionDTO = TestUtils.getPodamFactory().manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setPaymentOptions(List.of(paymentOptionDTO));
    debtPositionDTO.setOrganizationId(1L);

    // Mock request
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request =
      TestUtils.getPodamFactory().manufacturePojo(CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest.class);
    request.setDebtPositionDTO(debtPositionDTO);
    request.setIudList(List.of(iud));

    DebtPositionTypeOrg debtPositionTypeOrg = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setCode("CODE01");

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByInstallmentId(installmentId, accessToken)).thenReturn(debtPositionTypeOrg);

    CtBilancio bilancio = new CtBilancio();

    CtCapitolo capitolo = new CtCapitolo();
    capitolo.setCodCapitolo("CAP1");
    capitolo.setCodUfficio("UFF1");

    CtAccertamento accertamento = new CtAccertamento();
    accertamento.setCodAccertamento("ACC1");
    accertamento.setImporto(new BigDecimal("100.00"));

    capitolo.getAccertamento().add(accertamento);
    bilancio.getCapitolo().add(capitolo);

    when(balanceUnmashallerServiceMock.unmarshal(BALANCE)).thenReturn(bilancio);


    try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class);
         MockedStatic<Utilities> utilMock = mockStatic(Utilities.class)) {

      securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserExternalId).thenReturn(externalUserId);
      utilMock.when(Utilities::getTraceId).thenReturn(traceId);

      // When
      assessmentsRegistryService.createAssessmentsRegistryByDebtPositionDTOAndIud(request, accessToken);

      // Then
      verify(assessmentsRegistryRepositoryMock, times(1))
        .insertIfNotExists(
          eq(debtPositionDTO.getOrganizationId()),
          eq(debtPositionTypeOrg.getCode()),
          eq(capitolo.getCodCapitolo()),
          isNull(),
          eq(capitolo.getCodUfficio()),
          isNull(),
          eq(accertamento.getCodAccertamento()),
          isNull(),
          eq(String.valueOf(installmentDTO.getCreationDate().getYear())),
          eq(externalUserId),
          eq(traceId)
        );
    }
  }
}
