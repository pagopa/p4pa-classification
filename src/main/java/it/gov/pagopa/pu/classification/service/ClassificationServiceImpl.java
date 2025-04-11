package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.repository.view.ClassificationViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.FullClassificationViewPIIRepository;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClassificationServiceImpl implements ClassificationService {
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final ClassificationViewPIIRepository classificationViewPIIRepository;
  private final FullClassificationViewPIIRepository fullClassificationViewPIIRepository;

  public ClassificationServiceImpl(DebtPositionTypeOrgService debtPositionTypeOrgService,
                                   ClassificationViewPIIRepository classificationViewPIIRepository,
                                   FullClassificationViewPIIRepository fullClassificationViewPIIRepository) {
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.classificationViewPIIRepository = classificationViewPIIRepository;
    this.fullClassificationViewPIIRepository = fullClassificationViewPIIRepository;
  }

  @Override
  public PagedClassificationView getPagedClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken) {
    List<String> debtPositionTypeOrgCodes = fetchDebtPositionTypeOrgCodes(organizationId, operatorExternalUserId, accessToken);

    return classificationViewPIIRepository.getPagedClassificationView(organizationId, exportClassificationsFilterDTO, debtPositionTypeOrgCodes, pageable);
  }


  @Override
  public PagedFullClassificationView getPagedFullClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken) {
    List<String> debtPositionTypeOrgCodes = fetchDebtPositionTypeOrgCodes(organizationId, operatorExternalUserId, accessToken);

    return fullClassificationViewPIIRepository.getPagedFullClassificationView(organizationId, exportClassificationsFilterDTO, debtPositionTypeOrgCodes, pageable);
  }

  private List<String> fetchDebtPositionTypeOrgCodes(Long organizationId, String operatorExternalUserId, String accessToken) {
    log.info("Fetching debt position type org codes for organizationId: {} and operatorExternalUserId: {}", organizationId, operatorExternalUserId);
    return debtPositionTypeOrgService.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken)
      .stream().map(DebtPositionTypeOrg::getCode).toList();
  }
}
