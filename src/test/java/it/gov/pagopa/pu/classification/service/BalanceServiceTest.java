package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtBilancioDefault;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

  @Mock
  private BalanceUnmarshallerService balanceUnmarshallerServiceMock;
  @Mock
  private BalanceDefaultMarshallingService balanceDefaultMarshallingServiceMock;
  @Mock
  private AssessmentsRegistryRepository assessmentsRegistryRepositoryMock;

  private BalanceService balanceService;

  private static final String OPERATING_YEAR = String.valueOf(LocalDate.now().getYear());

  @BeforeEach
  void init() {
    balanceService = new BalanceService(balanceUnmarshallerServiceMock, balanceDefaultMarshallingServiceMock, assessmentsRegistryRepositoryMock);
  }

  @Test
  void givenValidBalanceDefaultWhenValidateThenSuccess(){
    String balance = "balance";

    when(balanceDefaultMarshallingServiceMock.unmarshal(balance)).thenReturn(new CtBilancioDefault());

    Boolean result = balanceService.isBalanceValid(balance);

    assertEquals(Boolean.TRUE, result);
    verify(balanceUnmarshallerServiceMock, times(0)).unmarshal(balance);
  }

  @Test
  void givenValidBalanceWhenValidateThenSuccess(){
    String balance = "balance";

    when(balanceDefaultMarshallingServiceMock.unmarshal(balance)).thenThrow(InvalidValueException.class);
    when(balanceUnmarshallerServiceMock.unmarshal(balance)).thenReturn(new CtBilancio());

    Boolean result = balanceService.isBalanceValid(balance);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
  void givenNotValidBalanceWhenValidateThenUnsuccessful(){
    String balance = "balanceNotValid";

    when(balanceDefaultMarshallingServiceMock.unmarshal(balance)).thenThrow(InvalidValueException.class);
    when(balanceUnmarshallerServiceMock.unmarshal(balance)).thenThrow(InvalidValueException.class);

    Boolean result = balanceService.isBalanceValid(balance);

    assertEquals(Boolean.FALSE, result);
  }

  @Test
  void givenNoAssessmentRegistryWhenGetBalanceThenNull(){
    Long orgId = 1L;
    String debtPositionTypeOrgCode = "CODE";

    Mockito.when(assessmentsRegistryRepositoryMock.findAssessmentsRegistriesByFilters(orgId, Set.of(debtPositionTypeOrgCode), null, null, null, null, null, null, OPERATING_YEAR, AssessmentsRegistryStatus.ACTIVE, PageRequest.of(0, 5)))
      .thenReturn(Page.empty());

    String result = balanceService.getBalanceByAssessmentRegistry(orgId, debtPositionTypeOrgCode);

    assertNull(result);
  }

  @Test
  void givenMoreAssessmentRegistryWhenGetBalanceThenException(){
    Long orgId = 1L;
    String debtPositionTypeOrgCode = "CODE";

    AssessmentsRegistry assessmentRegistry1 = AssessmentsRegistry.builder().organizationId(orgId).debtPositionTypeOrgCode(debtPositionTypeOrgCode).sectionCode("SEC01").operatingYear(OPERATING_YEAR).build();
    AssessmentsRegistry assessmentRegistry2 = AssessmentsRegistry.builder().organizationId(orgId).debtPositionTypeOrgCode(debtPositionTypeOrgCode).sectionCode("SEC02").operatingYear(OPERATING_YEAR).build();
    Page<AssessmentsRegistry> assessmentsRegistryPage = new PageImpl<>(List.of(assessmentRegistry1, assessmentRegistry2), PageRequest.of(0,2), 2);

    Mockito.when(assessmentsRegistryRepositoryMock.findAssessmentsRegistriesByFilters(orgId, Set.of(debtPositionTypeOrgCode), null, null, null, null, null, null, OPERATING_YEAR, AssessmentsRegistryStatus.ACTIVE, PageRequest.of(0, 5)))
      .thenReturn(assessmentsRegistryPage);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> balanceService.getBalanceByAssessmentRegistry(orgId, debtPositionTypeOrgCode));

    assertEquals("Expected exactly one assessment registry result, but found 2.", exception.getMessage());
  }

  @Test
  void givenAssessmentRegistryWhenGetBalanceThenSuccess(){
    Long orgId = 1L;
    String debtPositionTypeOrgCode = "CODE";

    String balance = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><bilancio xmlns=\"http://www.regione.veneto.it/schemas/2012/Pagamenti/Ente/BilancioDefault/\">" +
      "<capitolo>" +
      "<codCapitolo>CAP1</codCapitolo>" +
      "<accertamento>" +
      "<importo>TOTALE</importo>" +
      "</accertamento>" +
      "</capitolo>" +
      "</bilancio>";

    AssessmentsRegistry assessmentRegistry = AssessmentsRegistry.builder().organizationId(orgId).debtPositionTypeOrgCode(debtPositionTypeOrgCode).sectionCode("CAP1").operatingYear(OPERATING_YEAR).build();
    Page<AssessmentsRegistry> assessmentsRegistryPage = new PageImpl<>(List.of(assessmentRegistry), PageRequest.of(0,1), 1);

    Mockito.when(assessmentsRegistryRepositoryMock.findAssessmentsRegistriesByFilters(orgId, Set.of(debtPositionTypeOrgCode), null, null, null, null, null, null, OPERATING_YEAR, AssessmentsRegistryStatus.ACTIVE, PageRequest.of(0, 5)))
      .thenReturn(assessmentsRegistryPage);
    Mockito.when(balanceDefaultMarshallingServiceMock.marshal(any())).thenReturn(balance);

    String result = balanceService.getBalanceByAssessmentRegistry(orgId, debtPositionTypeOrgCode);

    assertEquals(result, balance);
  }

}
