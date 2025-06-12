package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsRegistryByDebtPositionDTOAndIudRequest;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.service.BalanceUnmashallerService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionDTO;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AssessmentsRegistryServiceImpl implements AssessmentsRegistryService{

  private final AssessmentsRegistryRepository assessmentsRegistryRepository;
  private final BalanceUnmashallerService balanceUnmashallerService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;

  public AssessmentsRegistryServiceImpl(
    AssessmentsRegistryRepository assessmentsRegistryRepository,
    BalanceUnmashallerService balanceUnmashallerService,
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
      .filter(installmentDTO -> request.getIudList().contains(installmentDTO.getIud()))
      .forEach(i -> {
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
      });
  }
}
