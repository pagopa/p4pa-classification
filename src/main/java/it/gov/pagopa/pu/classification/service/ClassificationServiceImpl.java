package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.dto.filters.ClassificationPaidInstallmentsFilterDTO;
import it.gov.pagopa.pu.classification.dto.filters.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.filters.TreasuredClassificationFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.enums.DataEventType;
import it.gov.pagopa.pu.classification.event.dto.DataEventRequestDTO;
import it.gov.pagopa.pu.classification.event.producer.DataEventsProducerService;
import it.gov.pagopa.pu.classification.mapper.PagedClassificationPaidInstallmentsViewMapper;
import it.gov.pagopa.pu.classification.mapper.TreasuredClassificationMapper;
import it.gov.pagopa.pu.classification.model.Classification;
import it.gov.pagopa.pu.classification.model.view.ClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.repository.ClassificationRepository;
import it.gov.pagopa.pu.classification.repository.view.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClassificationServiceImpl implements ClassificationService {
  private final ClassificationViewPIIRepository classificationViewPIIRepository;
  private final FullClassificationViewPIIRepository fullClassificationViewPIIRepository;
  private final TreasuredClassificationViewRepository treasuredClassificationViewRepository;
  private final TreasuredClassificationMapper treasuredClassificationMapper;
  private final ClassificationDetailViewPIIRepository classificationDetailViewPIIRepository;
  private final ClassificationPaidInstallmentsViewRepository classificationPaidInstallmentsViewRepository;
  private final PagedClassificationPaidInstallmentsViewMapper pagedClassificationPaidInstallmentsViewMapper;
  private final ClassificationRepository classificationRepository;
  private final DataEventsProducerService dataEventsProducerService;

  public ClassificationServiceImpl(
    ClassificationViewPIIRepository classificationViewPIIRepository,
    FullClassificationViewPIIRepository fullClassificationViewPIIRepository,
    TreasuredClassificationViewRepository treasuredClassificationViewRepository,
    TreasuredClassificationMapper treasuredClassificationMapper,
    ClassificationDetailViewPIIRepository classificationDetailViewPIIRepository,
    ClassificationPaidInstallmentsViewRepository classificationPaidInstallmentsViewRepository,
    PagedClassificationPaidInstallmentsViewMapper pagedClassificationPaidInstallmentsViewMapper,
    ClassificationRepository classificationRepository,
    DataEventsProducerService dataEventsProducerService) {
    this.classificationViewPIIRepository = classificationViewPIIRepository;
    this.fullClassificationViewPIIRepository = fullClassificationViewPIIRepository;
    this.treasuredClassificationViewRepository = treasuredClassificationViewRepository;
    this.treasuredClassificationMapper = treasuredClassificationMapper;
    this.classificationDetailViewPIIRepository = classificationDetailViewPIIRepository;
    this.classificationPaidInstallmentsViewRepository = classificationPaidInstallmentsViewRepository;
    this.pagedClassificationPaidInstallmentsViewMapper = pagedClassificationPaidInstallmentsViewMapper;
    this.classificationRepository = classificationRepository;
    this.dataEventsProducerService = dataEventsProducerService;
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

  @Override
  public PagedClassificationPaidInstallmentsView getPaidInstallmentsView(Long organizationId, ClassificationPaidInstallmentsFilterDTO filter, Pageable pageable) {
    Page<ClassificationPaidInstallmentsView> pagedPaidInstallments = classificationPaidInstallmentsViewRepository.findPaidInstallments(organizationId, filter, pageable);
    return pagedClassificationPaidInstallmentsViewMapper.map(pagedPaidInstallments);
  }

  @Override
  public List<Classification> saveAll(List<Classification> classifications) {
    List<Classification> saved = classificationRepository.saveAll(classifications);
    dataEventsProducerService.notifyClassificationEvent(saved, new DataEventRequestDTO(DataEventType.TRANSFER_CLASSIFICATION_LABELS,
      String.format("List of classifications with IUD: %s and transferIndex: %d",classifications.getFirst().getIud(), classifications.getFirst().getTransferIndex())));
    return saved;
  }
}
