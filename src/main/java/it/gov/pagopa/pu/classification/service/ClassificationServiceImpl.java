package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.repository.view.ClassificationViewPIIRepository;
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

  public ClassificationServiceImpl(DebtPositionTypeOrgService debtPositionTypeOrgService, ClassificationViewPIIRepository classificationViewPIIRepository) {
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.classificationViewPIIRepository = classificationViewPIIRepository;
  }

  @Override
  public PagedClassificationView getPagedClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken) {
    log.info("Fetching debt position type org codes for organizationId: {} and operatorExternalUserId: {}", organizationId, operatorExternalUserId);
    List<String> debtPositionTypeOrgCodes = debtPositionTypeOrgService.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken)
      .stream().map(DebtPositionTypeOrg::getCode).toList();

    return classificationViewPIIRepository.getPagedClassificationView(organizationId, exportClassificationsFilterDTO, debtPositionTypeOrgCodes, pageable);
  }
}
