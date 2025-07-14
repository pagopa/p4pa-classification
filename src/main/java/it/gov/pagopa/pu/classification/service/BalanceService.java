package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtBilancioDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.bilanciodefault.CtCapitoloDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@Slf4j
public class BalanceService {

  private static final AssessmentsRegistryStatus ASSESSMENTS_REGISTRY_STATUS = AssessmentsRegistryStatus.ACTIVE;
  private static final String BALANCE_AMOUNT_DEFAULT_VALUE = "TOTALE";

  private final BalanceUnmarshallerService balanceUnmashallerService;
  private final BalanceDefaultMarshallingService balanceDefaultMarshallingService;
  private final AssessmentsRegistryRepository assessmentsRegistryRepository;

  public BalanceService(BalanceUnmarshallerService balanceUnmashallerService, BalanceDefaultMarshallingService balanceDefaultMarshallingService, AssessmentsRegistryRepository assessmentsRegistryRepository) {
    this.balanceUnmashallerService = balanceUnmashallerService;
    this.balanceDefaultMarshallingService = balanceDefaultMarshallingService;
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
  }

  public Boolean isBalanceValid(String balance) {
    try {
      validate(balance);
      log.info("The balance value is formally valid");
      return Boolean.TRUE;
    } catch (InvalidValueException invalidValueException) {
      log.info("The balance value is not valid: {}", invalidValueException.getMessage());
      return Boolean.FALSE;
    }
  }

  private void validate(String balance) {
    try {
      log.info("Validating balance value with default structure");
      balanceDefaultMarshallingService.unmarshal(balance);
    } catch (InvalidValueException invalidValueException) {
      log.info("Validating balance value with actual structure");
      balanceUnmashallerService.unmarshal(balance);
    }
  }

  public String getBalanceByAssessmentRegistry(Long organizationId, String debtPositionTypeOrgCode) {
    String operatingYear = String.valueOf(LocalDate.now().getYear());
    log.info("Retrieving balance from AssessmentsRegistry with orgId[{}], debtPositionTypeCode[{}], operatingYear [{}], status [{}]",
      organizationId, debtPositionTypeOrgCode, operatingYear, ASSESSMENTS_REGISTRY_STATUS);
    Page<AssessmentsRegistry> assessmentsRegistries = assessmentsRegistryRepository.findAssessmentsRegistriesByFilters(
      organizationId,
      Set.of(debtPositionTypeOrgCode),
      null, null, null, null, null, null,
      operatingYear,
      ASSESSMENTS_REGISTRY_STATUS,
      PageRequest.of(0, 5));

    long assessmentRegistriesSize = assessmentsRegistries.getTotalElements();
    if (assessmentRegistriesSize > 1) {
      throw new IllegalStateException("Expected exactly one assessment registry result, but found " + assessmentRegistriesSize + ".");
    }

    AssessmentsRegistry assessmentRegistry = assessmentsRegistries.get().findFirst().orElse(null);
    if (assessmentRegistry == null) {
      return null;
    }

    CtBilancioDefault bilancio = getCtBilancioDefault(assessmentRegistry);

    return balanceDefaultMarshallingService.marshal(bilancio);
  }

  private static CtBilancioDefault getCtBilancioDefault(AssessmentsRegistry assessmentRegistry) {
    CtBilancioDefault bilancio = new CtBilancioDefault();

    CtCapitoloDefault capitolo = new CtCapitoloDefault();
    capitolo.setCodCapitolo(assessmentRegistry.getSectionCode());
    capitolo.setCodUfficio(assessmentRegistry.getOfficeCode());

    CtAccertamentoDefault accertamento = new CtAccertamentoDefault();
    accertamento.setCodAccertamento(assessmentRegistry.getAssessmentCode());
    accertamento.setImporto(BALANCE_AMOUNT_DEFAULT_VALUE);

    capitolo.getAccertamento().add(accertamento);

    bilancio.getCapitolo().add(capitolo);
    return bilancio;
  }

}
