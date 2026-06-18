package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgBalanceCostService;
import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;
import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.exception.custom.InvalidRequestBodyException;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.service.BalanceMarshallingService;
import it.gov.pagopa.pu.classification.util.ErrorCodeConstants;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.*;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static it.gov.pagopa.pu.classification.util.Constants.DEFAULT_SEND_DPTOBC_CODE;

@Service
public class AssessmentsRegistryServiceImpl implements AssessmentsRegistryService{
  private final AssessmentsRegistryRepository assessmentsRegistryRepository;
  private final BalanceMarshallingService balanceMarshallingService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostService;

  public AssessmentsRegistryServiceImpl(
    AssessmentsRegistryRepository assessmentsRegistryRepository,
    BalanceMarshallingService balanceMarshallingService,
    DebtPositionTypeOrgService debtPositionTypeOrgService,
    DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostService
  ) {
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
    this.balanceMarshallingService = balanceMarshallingService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.debtPositionTypeOrgBalanceCostService = debtPositionTypeOrgBalanceCostService;
  }

  @Transactional
  @Override
  public void createAssessmentsRegistryByDebtPositionDTOAndIud(
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request, String accessToken) {
    DebtPositionDTO debtPositionDTO = request.getDebtPositionDTO();
    Long organizationId = debtPositionDTO.getOrganizationId();

    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService
      .getDebtPositionTypeOrgByDebtPositionTypeOrgId(organizationId, debtPositionDTO.getDebtPositionTypeOrgId(), accessToken);

    Map<String, List<DebtPositionTypeOrgBalanceCost>> debtPositionTypeOrgBalanceCostMap = new HashMap<>();

    debtPositionDTO.getPaymentOptions().stream()
      .flatMap(paymentOptionDTO -> paymentOptionDTO.getInstallments().stream())
      .filter(installmentDTO -> request.getIudList()==null || request.getIudList().contains(installmentDTO.getIud()))
      .forEach(i -> {
        if(StringUtils.hasLength(i.getBalance())) {
          CtBilancio balance = balanceMarshallingService.unmarshal(i.getBalance(), null);
          List<CtCapitolo> capitoloList = balance.getCapitolo();

          String opYear = String.valueOf(i.getUpdateDate().getYear());

          List<DebtPositionTypeOrgBalanceCost> debtPositionTypeOrgBalanceCosts = debtPositionTypeOrgBalanceCostMap.computeIfAbsent(
            opYear,
            year -> debtPositionTypeOrgBalanceCostService.getDebtPositionTypeOrgBalanceCostsByDptoIdAndOpYear(
              debtPositionTypeOrg.getDebtPositionTypeOrgId(), year, accessToken)
          );

          capitoloList.forEach(capitolo -> {
            String codCapitolo = capitolo.getCodCapitolo();
            String codUfficio = capitolo.getCodUfficio();

            capitolo.getAccertamento().forEach(accertamento -> {
              String codAccertamento = accertamento.getCodAccertamento();

              if (!shouldSkipAssessmentInsertion(debtPositionTypeOrgBalanceCosts, codCapitolo, codUfficio, codAccertamento)) {
                assessmentsRegistryRepository.insertIfNotExists(
                  organizationId,
                  debtPositionTypeOrg.getCode(),
                  capitolo.getCodCapitolo(),
                  null,
                  capitolo.getCodUfficio(),
                  null,
                  accertamento.getCodAccertamento(),
                  null,
                  opYear,
                  SecurityUtils.getCurrentUserExternalId(),
                  Utilities.getTraceId()
                );
              }
            });
          });
        }
      });
  }

  private static boolean shouldSkipAssessmentInsertion(
    List<DebtPositionTypeOrgBalanceCost> debtPositionTypeOrgBalanceCosts,
    String codCapitolo,
    String codUfficio,
    String codAccertamento
  ) {
    if (Objects.equals(DEFAULT_SEND_DPTOBC_CODE, codCapitolo) &&
      Objects.equals(DEFAULT_SEND_DPTOBC_CODE, codUfficio) &&
      Objects.equals(DEFAULT_SEND_DPTOBC_CODE, codAccertamento)) {
      return true;
    }

    return debtPositionTypeOrgBalanceCosts.stream()
      .anyMatch(dptobc ->
        Objects.equals(dptobc.getSectionCode(), codCapitolo) &&
          Objects.equals(dptobc.getOfficeCode(), codUfficio) &&
          Objects.equals(dptobc.getAssessmentCode(), codAccertamento)
      );
  }

  @Transactional
  @Override
  public AssessmentsRegistry createAssessmentsRegistry(AssessmentsRegistry assessmentsRegistry) {
    validateAssessmentRegistry(assessmentsRegistry);
    assessmentsRegistry.setStatus(AssessmentsRegistryStatus.ACTIVE);
    assessmentsRegistryRepository.updateStatus(AssessmentsRegistryStatus.INACTIVE, assessmentsRegistry.getOrganizationId(),assessmentsRegistry.getDebtPositionTypeOrgCode(), assessmentsRegistry.getOperatingYear());
    return assessmentsRegistryRepository.save(assessmentsRegistry);
  }

  private static void validateAssessmentRegistry(AssessmentsRegistry assessmentsRegistry) {
    if(assessmentsRegistry.getAssessmentRegistryId()!=null){
      throw new InvalidRequestBodyException(ErrorCodeConstants.ERROR_CODE_INVALID_ASSESSMENT_REGISTRY, "assessmentRegistryId should not be provided");
    }
  }
}
