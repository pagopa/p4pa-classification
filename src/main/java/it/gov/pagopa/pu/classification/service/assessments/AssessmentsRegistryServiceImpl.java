package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;
import it.gov.pagopa.pu.classification.enums.AssessmentsRegistryStatus;
import it.gov.pagopa.pu.classification.exception.custom.InvalidRequestBodyException;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.service.BalanceUnmarshallerService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class AssessmentsRegistryServiceImpl implements AssessmentsRegistryService{

  private final AssessmentsRegistryRepository assessmentsRegistryRepository;
  private final BalanceUnmarshallerService balanceUnmashallerService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;

  public AssessmentsRegistryServiceImpl(
    AssessmentsRegistryRepository assessmentsRegistryRepository,
    BalanceUnmarshallerService balanceUnmashallerService,
    DebtPositionTypeOrgService debtPositionTypeOrgService) {
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
    this.balanceUnmashallerService = balanceUnmashallerService;
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
      .forEach(i -> {
        if(StringUtils.hasLength(i.getBalance())) {
          CtBilancio balance = balanceUnmashallerService.unmarshal(i.getBalance());
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
      throw new InvalidRequestBodyException("assessmentRegistryId should not be provided");
    }
  }
}
