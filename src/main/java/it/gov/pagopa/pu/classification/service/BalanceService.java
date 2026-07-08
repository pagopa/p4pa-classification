package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.generated.ValidateBalanceRequest;
import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.exception.custom.IllegalStateBusinessException;
import it.gov.pagopa.pu.classification.exception.custom.InvalidValueException;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.util.Constants;
import it.gov.pagopa.pu.classification.util.ErrorCodeConstants;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtAccertamentoDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.BilancioDefault;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitoloDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class BalanceService {

  private static final AssessmentsRegistryStatus ASSESSMENTS_REGISTRY_STATUS = AssessmentsRegistryStatus.ACTIVE;
  private static final String BALANCE_AMOUNT_DEFAULT_VALUE = "TOTALE";

  private final BalanceMarshallingService balanceMarshallingService;
  private final BalanceDefaultMarshallingService balanceDefaultMarshallingService;
  private final AssessmentsRegistryRepository assessmentsRegistryRepository;

  public BalanceService(BalanceMarshallingService balanceMarshallingService, BalanceDefaultMarshallingService balanceDefaultMarshallingService, AssessmentsRegistryRepository assessmentsRegistryRepository) {
    this.balanceMarshallingService = balanceMarshallingService;
    this.balanceDefaultMarshallingService = balanceDefaultMarshallingService;
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
  }

  public Boolean isBalanceValid(ValidateBalanceRequest validateBalanceRequest) {
    try {
      if (!Objects.isNull(unmarshalBalance(validateBalanceRequest.getBalance(), validateBalanceRequest.getAmountCents()))) {
        log.info("The balance value is formally valid");
        return Boolean.TRUE;
      }
      return Boolean.FALSE;
    } catch (InvalidValueException invalidValueException) {
      log.info("The balance value is not valid: {}", invalidValueException.getMessage());
      return Boolean.FALSE;
    }
  }

  public Object unmarshalBalance(String balance, Long amountCents) {
    try {
      log.info("Validating balance value with default structure");
      return balanceDefaultMarshallingService.unmarshal(balance);
    } catch (InvalidValueException invalidValueException) {
      log.info("Validating balance value with actual structure");
      return balanceMarshallingService.unmarshal(balance, amountCents);
    }
  }

  public String getBalanceByAssessmentRegistry(Long organizationId, String debtPositionTypeOrgCode) {
    String operatingYear = String.valueOf(LocalDate.now(Constants.ZONEID).getYear());
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
      throw new IllegalStateBusinessException(ErrorCodeConstants.ERROR_CODE_TOO_MANY_ASSESSMENT_REGISTRY, "Expected exactly one assessment registry result, but found " + assessmentRegistriesSize + ".");
    }

    AssessmentsRegistry assessmentRegistry = assessmentsRegistries.get().findFirst().orElse(null);
    if (assessmentRegistry == null) {
      return null;
    }

    BilancioDefault bilancio = getCtBilancioDefault(assessmentRegistry);

    return balanceDefaultMarshallingService.marshal(bilancio);
  }

  private static BilancioDefault getCtBilancioDefault(AssessmentsRegistry assessmentRegistry) {
    BilancioDefault bilancio = new BilancioDefault();

    CtCapitoloDefault capitolo = new CtCapitoloDefault();
    capitolo.setCodCapitolo(assessmentRegistry.getSectionCode());
    capitolo.setCodUfficio(assessmentRegistry.getOfficeCode());

    CtAccertamentoDefault accertamento = new CtAccertamentoDefault();
    accertamento.setCodAccertamento(assessmentRegistry.getAssessmentCode());
    accertamento.setImporto(BALANCE_AMOUNT_DEFAULT_VALUE);

    capitolo.getAccertamentos().add(accertamento);

    bilancio.getCapitolos().add(capitolo);
    return bilancio;
  }


}
