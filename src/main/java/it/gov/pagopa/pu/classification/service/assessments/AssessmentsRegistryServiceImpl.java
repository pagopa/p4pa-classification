package it.gov.pagopa.pu.classification.service.assessments;

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
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentStatus;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AssessmentsRegistryServiceImpl implements AssessmentsRegistryService{

  private final AssessmentsRegistryRepository assessmentsRegistryRepository;
  private final BalanceMarshallingService balanceMarshallingService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;

  public AssessmentsRegistryServiceImpl(
    AssessmentsRegistryRepository assessmentsRegistryRepository,
    BalanceMarshallingService balanceMarshallingService,
    DebtPositionTypeOrgService debtPositionTypeOrgService) {
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
    this.balanceMarshallingService = balanceMarshallingService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
  }

  @Transactional
  @Override
  public void createAssessmentsRegistryByDebtPositionDTOAndIud(
    CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest request, String accessToken) {
    DebtPositionDTO debtPositionDTO = request.getDebtPositionDTO();
    Long organizationId = debtPositionDTO.getOrganizationId();

    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService
      .getDebtPositionTypeOrgByDebtPositionTypeOrgId(organizationId, debtPositionDTO.getDebtPositionTypeOrgId(), accessToken);

    debtPositionDTO.getPaymentOptions().stream()
      .flatMap(paymentOptionDTO -> paymentOptionDTO.getInstallments().stream())
      .filter(installmentDTO -> request.getIudList()==null || request.getIudList().contains(installmentDTO.getIud()))
      .filter(installmentDTO -> !InstallmentStatus.PAID.equals(installmentDTO.getStatus()))
      .forEach(i -> {
        if(StringUtils.hasLength(i.getBalance())) {
          CtBilancio balance = balanceMarshallingService.unmarshal(i.getBalance(), null);
          List<CtCapitolo> capitoloList = balance.getCapitolo();

          capitoloList.forEach(capitolo ->
            capitolo.getAccertamento().forEach(accertamento ->
              assessmentsRegistryRepository.insertIfNotExists(
                organizationId,
                debtPositionTypeOrg.getCode(),
                capitolo.getCodCapitolo(),
                null,
                capitolo.getCodUfficio(),
                null,
                accertamento.getCodAccertamento(),
                null,
                String.valueOf(i.getCreationDate().getYear()),
                SecurityUtils.getCurrentUserExternalId(),
                Utilities.getTraceId()
              )));
        }
      });
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
