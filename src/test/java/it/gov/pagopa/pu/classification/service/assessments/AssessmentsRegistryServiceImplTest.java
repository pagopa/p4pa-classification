package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgBalanceCostService;
import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;
import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.exception.custom.InvalidRequestBodyException;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.service.BalanceMarshallingService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import it.gov.pagopa.pu.classification.util.TestUtils;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.*;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamento;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentsRegistryServiceImplTest {

  @Mock
  private AssessmentsRegistryRepository assessmentsRegistryRepositoryMock;
  @Mock
  private BalanceMarshallingService balanceMarshallingServiceMock;
  @Mock
  private DebtPositionTypeOrgService debtPositionTypeOrgServiceMock;
  @Mock
  private DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostServiceMock;

  @InjectMocks
  private AssessmentsRegistryServiceImpl assessmentsRegistryService;

  private static final String IUD = "IUD";

  private static final OffsetDateTime creationDate = OffsetDateTime.now();

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
      balanceMarshallingServiceMock,
      debtPositionTypeOrgServiceMock,
      debtPositionTypeOrgBalanceCostServiceMock
    );
  }

  @Test
  void givenValidRequestWithIudListWhenCreateAssessmentsRegistryByDebtPositionDTOAndIudThenOk() {
    // Given
    String externalUserId = "USERID";
    String traceId = "TRACEID";
    String accessToken = "token";

    DebtPositionDTO debtPositionDTO = buildDebtPositionDTO();

    // Mock request
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request =
      TestUtils.getPodamFactory().manufacturePojo(CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest.class);
    request.setDebtPositionDTO(debtPositionDTO);
    request.setIudList(List.of(IUD));

    DebtPositionTypeOrg debtPositionTypeOrg = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setCode("CODE01");
    debtPositionTypeOrg.setDebtPositionTypeOrgId(debtPositionDTO.getDebtPositionTypeOrgId());

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByDebtPositionTypeOrgId(debtPositionDTO.getOrganizationId(),
      debtPositionDTO.getDebtPositionTypeOrgId(), accessToken)).thenReturn(debtPositionTypeOrg);

    when(debtPositionTypeOrgBalanceCostServiceMock.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(
      debtPositionDTO.getDebtPositionTypeOrgId(), String.valueOf(creationDate.getYear()), accessToken))
      .thenReturn(List.of());

    CtBilancio bilancio = new CtBilancio();

    CtCapitolo capitolo = new CtCapitolo();
    capitolo.setCodCapitolo("CAP1");
    capitolo.setCodUfficio("UFF1");

    CtAccertamento accertamento = new CtAccertamento();
    accertamento.setCodAccertamento("ACC1");
    accertamento.setImporto(new BigDecimal("100.00"));

    capitolo.getAccertamento().add(accertamento);
    bilancio.getCapitolo().add(capitolo);

    when(balanceMarshallingServiceMock.unmarshal(BALANCE,null)).thenReturn(bilancio);


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
          eq(String.valueOf(creationDate.getYear())),
          eq(externalUserId),
          eq(traceId)
        );
    }
  }

  @Test
  void givenValidRequestWithoutIudListWhenCreateAssessmentsRegistryByDebtPositionDTOAndIudThenOk() {
    // Given
    String externalUserId = "USERID";
    String traceId = "TRACEID";
    String accessToken = "token";

    DebtPositionDTO debtPositionDTO = buildDebtPositionDTO();

    // Mock request
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request =
      TestUtils.getPodamFactory().manufacturePojo(CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest.class);
    request.setDebtPositionDTO(debtPositionDTO);
    request.setIudList(null);

    DebtPositionTypeOrg debtPositionTypeOrg = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setCode("CODE01");
    debtPositionTypeOrg.setDebtPositionTypeOrgId(debtPositionDTO.getDebtPositionTypeOrgId());

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByDebtPositionTypeOrgId(debtPositionDTO.getOrganizationId(),
      debtPositionDTO.getDebtPositionTypeOrgId(), accessToken)).thenReturn(debtPositionTypeOrg);

    when(debtPositionTypeOrgBalanceCostServiceMock.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(
      debtPositionDTO.getDebtPositionTypeOrgId(), String.valueOf(creationDate.getYear()), accessToken))
      .thenReturn(List.of());

    CtBilancio bilancio = new CtBilancio();

    CtCapitolo capitolo = new CtCapitolo();
    capitolo.setCodCapitolo("CAP1");
    capitolo.setCodUfficio("UFF1");

    CtAccertamento accertamento = new CtAccertamento();
    accertamento.setCodAccertamento("ACC1");
    accertamento.setImporto(new BigDecimal("100.00"));

    capitolo.getAccertamento().add(accertamento);
    bilancio.getCapitolo().add(capitolo);

    when(balanceMarshallingServiceMock.unmarshal(BALANCE,null)).thenReturn(bilancio);


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
          eq(String.valueOf(creationDate.getYear())),
          eq(externalUserId),
          eq(traceId)
        );
    }
  }

  @Test
  void givenRequestWithNullBalanceWhenCreateAssessmentsRegistryByDebtPositionDTOAndIudThenVerifyNoExecute() {
    // Given
    String externalUserId = "USERID";
    String traceId = "TRACEID";
    String accessToken = "token";

    DebtPositionDTO debtPositionDTO = buildDebtPositionDTO();

    // Mock request
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request =
      TestUtils.getPodamFactory().manufacturePojo(CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest.class);
    request.setDebtPositionDTO(debtPositionDTO);
    request.setIudList(List.of(IUD));

    DebtPositionTypeOrg debtPositionTypeOrg = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setCode("CODE01");

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByDebtPositionTypeOrgId(debtPositionDTO.getOrganizationId(),
      debtPositionDTO.getDebtPositionTypeOrgId(), accessToken)).thenReturn(debtPositionTypeOrg);

    debtPositionDTO.getPaymentOptions().forEach(
      paymentOptionDTO -> paymentOptionDTO.getInstallments()
        .forEach(installmentDTO -> installmentDTO.setBalance(null)));

    try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class);
      MockedStatic<Utilities> utilMock = mockStatic(Utilities.class)) {

      securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserExternalId).thenReturn(externalUserId);
      utilMock.when(Utilities::getTraceId).thenReturn(traceId);

      // When
      assessmentsRegistryService.createAssessmentsRegistryByDebtPositionDTOAndIud(request, accessToken);

      // Then
      verifyNoInteractions(assessmentsRegistryRepositoryMock);
    }
  }

  @Test
  void givenValidRequestWhenCreateAssessmentsRegistryThenOk(){
    AssessmentsRegistry assessmentsRegistry = TestUtils.getPodamFactory().manufacturePojo(AssessmentsRegistry.class);
    assessmentsRegistry.setAssessmentRegistryId(null);
    AssessmentsRegistry expectedResponse = TestUtils.getPodamFactory().manufacturePojo(AssessmentsRegistry.class);

    doNothing().when(assessmentsRegistryRepositoryMock).updateStatus(AssessmentsRegistryStatus.INACTIVE,assessmentsRegistry.getOrganizationId(),assessmentsRegistry.getDebtPositionTypeOrgCode(),assessmentsRegistry.getOperatingYear());
    when(assessmentsRegistryRepositoryMock.save(assessmentsRegistry)).thenReturn(expectedResponse);

    AssessmentsRegistry response = assessmentsRegistryService.createAssessmentsRegistry(assessmentsRegistry);

    Assertions.assertNotNull(response);
    Assertions.assertEquals(expectedResponse,response);
  }

  @Test
  void givenInvalidRequestWhenCreateAssessmentsRegistryThenInvalidRequestBodyException(){
    AssessmentsRegistry assessmentsRegistry = TestUtils.getPodamFactory().manufacturePojo(AssessmentsRegistry.class);

    Assertions.assertThrows(InvalidRequestBodyException.class, () -> assessmentsRegistryService.createAssessmentsRegistry(assessmentsRegistry));

    verifyNoInteractions(assessmentsRegistryRepositoryMock);
  }

  @Test
  void givenRequestWithMatchedDptobcWhenCreateAssessmentsRegistryThenVerifyNoExecute() {
    String externalUserId = "externalUserId";
    String traceId = "traceId";
    String accessToken = "accessToken";

    DebtPositionDTO debtPositionDTO = buildDebtPositionDTO();

    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request =
      TestUtils.getPodamFactory().manufacturePojo(CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest.class);
    request.setDebtPositionDTO(debtPositionDTO);
    request.setIudList(null);

    DebtPositionTypeOrg debtPositionTypeOrg = TestUtils.getPodamFactory().manufacturePojo(DebtPositionTypeOrg.class);
    debtPositionTypeOrg.setCode("CODE01");
    debtPositionTypeOrg.setDebtPositionTypeOrgId(debtPositionDTO.getDebtPositionTypeOrgId());

    DebtPositionTypeOrgBalanceCost debtPositionTypeOrgBalanceCost = new DebtPositionTypeOrgBalanceCost();
    debtPositionTypeOrgBalanceCost.sectionCode("CAP1");
    debtPositionTypeOrgBalanceCost.setAssessmentCode("ACC1");
    debtPositionTypeOrgBalanceCost.setOfficeCode("UFF1");

    List<DebtPositionTypeOrgBalanceCost> debtPositionTypeOrgBalanceCosts = List.of(debtPositionTypeOrgBalanceCost);

    when(debtPositionTypeOrgServiceMock.getDebtPositionTypeOrgByDebtPositionTypeOrgId(debtPositionDTO.getOrganizationId(),
      debtPositionDTO.getDebtPositionTypeOrgId(), accessToken)).thenReturn(debtPositionTypeOrg);

    when(debtPositionTypeOrgBalanceCostServiceMock.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(
      debtPositionDTO.getDebtPositionTypeOrgId(), String.valueOf(creationDate.getYear()), accessToken))
      .thenReturn(debtPositionTypeOrgBalanceCosts);

    CtBilancio bilancio = new CtBilancio();

    CtCapitolo capitolo = new CtCapitolo();
    capitolo.setCodCapitolo("CAP1");
    capitolo.setCodUfficio("UFF1");

    CtAccertamento accertamento = new CtAccertamento();
    accertamento.setCodAccertamento("ACC1");
    accertamento.setImporto(new BigDecimal("100.00"));

    capitolo.getAccertamento().add(accertamento);
    bilancio.getCapitolo().add(capitolo);

    when(balanceMarshallingServiceMock.unmarshal(BALANCE,null)).thenReturn(bilancio);

    try (MockedStatic<SecurityUtils> securityUtilsMockedStatic = mockStatic(SecurityUtils.class);
         MockedStatic<Utilities> utilMock = mockStatic(Utilities.class)) {

      securityUtilsMockedStatic.when(SecurityUtils::getCurrentUserExternalId).thenReturn(externalUserId);
      utilMock.when(Utilities::getTraceId).thenReturn(traceId);

      assessmentsRegistryService.createAssessmentsRegistryByDebtPositionDTOAndIud(request, accessToken);

      verifyNoInteractions(assessmentsRegistryRepositoryMock);
    }
  }

  private DebtPositionDTO buildDebtPositionDTO() {
    // Mock InstallmentDTO
    InstallmentDTO installmentDTO = TestUtils.getPodamFactory().manufacturePojo(InstallmentDTO.class);
    installmentDTO.setBalance(BALANCE);
    installmentDTO.setIud(IUD);
    installmentDTO.setCreationDate(creationDate);

    // Mock PaymentOptionDTO
    PaymentOptionDTO paymentOptionDTO = TestUtils.getPodamFactory().manufacturePojo(PaymentOptionDTO.class);
    paymentOptionDTO.setInstallments(List.of(installmentDTO));


    // Mock DebtPositionDTO
    DebtPositionDTO debtPositionDTO = TestUtils.getPodamFactory().manufacturePojo(DebtPositionDTO.class);
    debtPositionDTO.setPaymentOptions(List.of(paymentOptionDTO));
    debtPositionDTO.setOrganizationId(1L);
    debtPositionDTO.setDebtPositionTypeOrgId(2L);

    return debtPositionDTO;
  }
}
