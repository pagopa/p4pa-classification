package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.dto.*;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.mapper.PagedClassificationPaidInstallmentsViewMapper;
import it.gov.pagopa.pu.classification.mapper.TreasuredClassificationMapper;
import it.gov.pagopa.pu.classification.model.view.ClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.repository.view.*;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class ClassificationServiceImpl implements ClassificationService {
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final ClassificationViewPIIRepository classificationViewPIIRepository;
  private final FullClassificationViewPIIRepository fullClassificationViewPIIRepository;
  private final TreasuredClassificationViewRepository treasuredClassificationViewRepository;
  private final TreasuredClassificationMapper treasuredClassificationMapper;
  private final ClassificationDetailViewPIIRepository classificationDetailViewPIIRepository;
  private final ClassificationPaidInstallmentsViewRepository classificationPaidInstallmentsViewRepository;
  private final PagedClassificationPaidInstallmentsViewMapper pagedClassificationPaidInstallmentsViewMapper;

  public ClassificationServiceImpl(
    DebtPositionTypeOrgService debtPositionTypeOrgService,
    ClassificationViewPIIRepository classificationViewPIIRepository,
    FullClassificationViewPIIRepository fullClassificationViewPIIRepository,
    TreasuredClassificationViewRepository treasuredClassificationViewRepository,
    TreasuredClassificationMapper treasuredClassificationMapper,
    ClassificationDetailViewPIIRepository classificationDetailViewPIIRepository,
    ClassificationPaidInstallmentsViewRepository classificationPaidInstallmentsViewRepository,
    PagedClassificationPaidInstallmentsViewMapper pagedClassificationPaidInstallmentsViewMapper) {
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.classificationViewPIIRepository = classificationViewPIIRepository;
    this.fullClassificationViewPIIRepository = fullClassificationViewPIIRepository;
    this.treasuredClassificationViewRepository = treasuredClassificationViewRepository;
    this.treasuredClassificationMapper = treasuredClassificationMapper;
    this.classificationDetailViewPIIRepository = classificationDetailViewPIIRepository;
    this.classificationPaidInstallmentsViewRepository = classificationPaidInstallmentsViewRepository;
    this.pagedClassificationPaidInstallmentsViewMapper = pagedClassificationPaidInstallmentsViewMapper;
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

  @Override
  public PagedTreasuredClassification getPagedTreasuredClassification(
    Long organizationId,
    TreasuredClassificationFilterDTO treasuredClassificationFilterDTO,
    Pageable pageable) {
    return treasuredClassificationMapper.map2PagedTreasuredClassification(
      treasuredClassificationViewRepository.getTreasuredClassifications(organizationId,
        treasuredClassificationFilterDTO, pageable));
  }

  @Override
  public ClassificationDetailViewDTO getClassificationDetailView(Long organizationId, Long classificationId) {
    return classificationDetailViewPIIRepository.getClassificationDetailView(organizationId, classificationId);
  }

  private List<String> fetchDebtPositionTypeOrgCodes(Long organizationId, String operatorExternalUserId, String accessToken) {
    log.info("Fetching debt position type org codes for organizationId: {} and operatorExternalUserId: {}", organizationId, operatorExternalUserId);
    return debtPositionTypeOrgService.findDebtPositionTypeOrgs(organizationId, operatorExternalUserId, accessToken)
      .stream().map(DebtPositionTypeOrg::getCode).toList();
  }

  @Override
  public PagedClassificationPaidInstallmentsView getPaidInstallmentsView(Long organizationId, String iuv, OffsetDateTimeIntervalFilter paymentDateTimeIntervalFilter, LocalDateTimeIntervalFilter updateDateTimeIntervalFilter, Set<String> iuds, Pageable pageable) {
    Page<ClassificationPaidInstallmentsView> pagedPaidInstallments = classificationPaidInstallmentsViewRepository.findPaidInstallments(organizationId, iuv, paymentDateTimeIntervalFilter, updateDateTimeIntervalFilter, iuds, pageable);
    return pagedClassificationPaidInstallmentsViewMapper.map(pagedPaidInstallments);
  }
}
