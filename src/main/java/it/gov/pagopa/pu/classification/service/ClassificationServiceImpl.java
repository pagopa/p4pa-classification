package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.TreasuredClassificationFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.mapper.TreasuredClassificationMapper;
import it.gov.pagopa.pu.classification.repository.view.ClassificationDetailViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.ClassificationViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.FullClassificationViewPIIRepository;
import it.gov.pagopa.pu.classification.repository.view.TreasuredClassificationViewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ClassificationServiceImpl implements ClassificationService {
  private final ClassificationViewPIIRepository classificationViewPIIRepository;
  private final FullClassificationViewPIIRepository fullClassificationViewPIIRepository;
  private final TreasuredClassificationViewRepository treasuredClassificationViewRepository;
  private final TreasuredClassificationMapper treasuredClassificationMapper;
  private final ClassificationDetailViewPIIRepository classificationDetailViewPIIRepository;

  public ClassificationServiceImpl(
    ClassificationViewPIIRepository classificationViewPIIRepository,
    FullClassificationViewPIIRepository fullClassificationViewPIIRepository,
    TreasuredClassificationViewRepository treasuredClassificationViewRepository,
    TreasuredClassificationMapper treasuredClassificationMapper,
    ClassificationDetailViewPIIRepository classificationDetailViewPIIRepository) {
    this.classificationViewPIIRepository = classificationViewPIIRepository;
    this.fullClassificationViewPIIRepository = fullClassificationViewPIIRepository;
    this.treasuredClassificationViewRepository = treasuredClassificationViewRepository;
    this.treasuredClassificationMapper = treasuredClassificationMapper;
    this.classificationDetailViewPIIRepository = classificationDetailViewPIIRepository;
  }

  @Override
  public PagedClassificationView getPagedClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken) {
    return classificationViewPIIRepository.getPagedClassificationView(organizationId, exportClassificationsFilterDTO, pageable);
  }

  @Override
  public PagedFullClassificationView getPagedFullClassificationView(Long organizationId, String operatorExternalUserId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, Pageable pageable, String accessToken) {
    return fullClassificationViewPIIRepository.getPagedFullClassificationView(organizationId, exportClassificationsFilterDTO, pageable);
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

}
