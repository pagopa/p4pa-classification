package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.ValidateBalanceRequest;
import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.exception.custom.IllegalStateBusinessException;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.veneto.regione.schemas._2012.pagamenti.ente.Bilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.BilancioDefault;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

  @Mock
  private BalanceMarshallingService balanceMarshallingServiceMock;
  @Mock
  private BalanceDefaultMarshallingService balanceDefaultMarshallingServiceMock;
  @Mock
  private AssessmentsRegistryRepository assessmentsRegistryRepositoryMock;

  private BalanceService balanceService;

  @BeforeEach
  void init() {
    balanceService = new BalanceService(balanceMarshallingServiceMock, balanceDefaultMarshallingServiceMock, assessmentsRegistryRepositoryMock);
  }

  @AfterEach
  void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(
      balanceMarshallingServiceMock,
      balanceDefaultMarshallingServiceMock,
      assessmentsRegistryRepositoryMock);
  }

  @Test
  void givenValidBalanceDefaultWhenValidateThenSuccess(){
    ValidateBalanceRequest validateBalanceRequest = ValidateBalanceRequest.builder().balance("balance").amountCents(1L).build();

    when(balanceDefaultMarshallingServiceMock.unmarshal(validateBalanceRequest.getBalance())).thenReturn(new BilancioDefault());

    Boolean result = balanceService.isBalanceValid(validateBalanceRequest);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
  void givenValidBalanceWhenValidateThenSuccess(){
    ValidateBalanceRequest validateBalanceRequest = ValidateBalanceRequest.builder().balance("balance").amountCents(1L).build();

    when(balanceDefaultMarshallingServiceMock.unmarshal(validateBalanceRequest.getBalance())).thenThrow(InvalidValueException.class);
    when(balanceMarshallingServiceMock.unmarshal(validateBalanceRequest.getBalance(),validateBalanceRequest.getAmountCents())).thenReturn(new Bilancio());

    Boolean result = balanceService.isBalanceValid(validateBalanceRequest);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
  void givenNotValidBalanceWhenValidateThenUnsuccessful(){
    ValidateBalanceRequest validateBalanceRequest = ValidateBalanceRequest.builder().balance("balanceNotValid").amountCents(1L).build();

    when(balanceDefaultMarshallingServiceMock.unmarshal(validateBalanceRequest.getBalance())).thenThrow(InvalidValueException.class);
    when(balanceMarshallingServiceMock.unmarshal(validateBalanceRequest.getBalance(),validateBalanceRequest.getAmountCents())).thenThrow(InvalidValueException.class);

    Boolean result = balanceService.isBalanceValid(validateBalanceRequest);

    assertEquals(Boolean.FALSE, result);
  }

  @Test
  void givenNullObjectBalanceWhenValidateThenUnsuccessful(){
    ValidateBalanceRequest validateBalanceRequest = ValidateBalanceRequest.builder().balance("balanceNotValid").amountCents(1L).build();

    when(balanceDefaultMarshallingServiceMock.unmarshal(validateBalanceRequest.getBalance())).thenReturn(null);

    Boolean result = balanceService.isBalanceValid(validateBalanceRequest);

    assertEquals(Boolean.FALSE, result);
  }

  @Test
  void givenNoAssessmentRegistryWhenGetBalanceThenNull(){
    Long orgId = 1L;
    String debtPositionTypeOrgCode = "CODE";
    String operatingYear = String.valueOf(LocalDate.now().getYear());

    Mockito.when(assessmentsRegistryRepositoryMock.findAssessmentsRegistriesByFilters(orgId, Set.of(debtPositionTypeOrgCode), null, null, null, null, null, null, operatingYear, AssessmentsRegistryStatus.ACTIVE, PageRequest.of(0, 5)))
      .thenReturn(Page.empty());

    String result = balanceService.getBalanceByAssessmentRegistry(orgId, debtPositionTypeOrgCode);

    assertNull(result);
  }

  @Test
  void givenMoreAssessmentRegistryWhenGetBalanceThenException(){
    Long orgId = 1L;
    String debtPositionTypeOrgCode = "CODE";
    String operatingYear = String.valueOf(LocalDate.now().getYear());

    AssessmentsRegistry assessmentRegistry1 = AssessmentsRegistry.builder().organizationId(orgId).debtPositionTypeOrgCode(debtPositionTypeOrgCode).sectionCode("SEC01").operatingYear(operatingYear).build();
    AssessmentsRegistry assessmentRegistry2 = AssessmentsRegistry.builder().organizationId(orgId).debtPositionTypeOrgCode(debtPositionTypeOrgCode).sectionCode("SEC02").operatingYear(operatingYear).build();
    Page<AssessmentsRegistry> assessmentsRegistryPage = new PageImpl<>(List.of(assessmentRegistry1, assessmentRegistry2), PageRequest.of(0,2), 2);

    Mockito.when(assessmentsRegistryRepositoryMock.findAssessmentsRegistriesByFilters(orgId, Set.of(debtPositionTypeOrgCode), null, null, null, null, null, null, operatingYear, AssessmentsRegistryStatus.ACTIVE, PageRequest.of(0, 5)))
      .thenReturn(assessmentsRegistryPage);

    IllegalStateBusinessException exception = assertThrows(IllegalStateBusinessException.class, () -> balanceService.getBalanceByAssessmentRegistry(orgId, debtPositionTypeOrgCode));

    assertEquals("TOO_MANY_ASSESSMENT_REGISTRY",exception.getCode());
    assertEquals("Expected exactly one assessment registry result, but found 2.", exception.getMessage());
  }

  @Test
  void givenAssessmentRegistryWhenGetBalanceThenSuccess(){
    Long orgId = 1L;
    String debtPositionTypeOrgCode = "CODE";
    String operatingYear = String.valueOf(LocalDate.now().getYear());

    String balance = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/BilancioDefault/\">" +
      "<capitolo>" +
      "<codCapitolo>CAP1</codCapitolo>" +
      "<accertamento>" +
      "<importo>TOTALE</importo>" +
      "</accertamento>" +
      "</capitolo>" +
      "</bilancio>";

    AssessmentsRegistry assessmentRegistry = AssessmentsRegistry.builder().organizationId(orgId).debtPositionTypeOrgCode(debtPositionTypeOrgCode).sectionCode("CAP1").operatingYear(operatingYear).build();
    Page<AssessmentsRegistry> assessmentsRegistryPage = new PageImpl<>(List.of(assessmentRegistry), PageRequest.of(0,1), 1);

    Mockito.when(assessmentsRegistryRepositoryMock.findAssessmentsRegistriesByFilters(orgId, Set.of(debtPositionTypeOrgCode), null, null, null, null, null, null, operatingYear, AssessmentsRegistryStatus.ACTIVE, PageRequest.of(0, 5)))
      .thenReturn(assessmentsRegistryPage);
    Mockito.when(balanceDefaultMarshallingServiceMock.marshal(any())).thenReturn(balance);

    String result = balanceService.getBalanceByAssessmentRegistry(orgId, debtPositionTypeOrgCode);

    assertEquals(balance, result);
  }

}
