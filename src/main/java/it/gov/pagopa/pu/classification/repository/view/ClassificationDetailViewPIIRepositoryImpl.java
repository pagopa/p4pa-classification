package it.gov.pagopa.pu.classification.repository.view;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.exception.custom.NotFoundException;
import it.gov.pagopa.pu.classification.mapper.ClassificationDetailViewPIIMapper;
import it.gov.pagopa.pu.classification.model.view.ClassificationDetailViewNoPII;
import org.springframework.stereotype.Service;

@Service
public class ClassificationDetailViewPIIRepositoryImpl implements ClassificationDetailViewPIIRepository {
  private final ClassificationDetailViewNoPIIRepository classificationDetailViewNoPIIRepository;
  private final ClassificationDetailViewPIIMapper classificationDetailViewPIIMapper;

  public ClassificationDetailViewPIIRepositoryImpl(ClassificationDetailViewNoPIIRepository classificationDetailViewNoPIIRepository,
                                                   ClassificationDetailViewPIIMapper classificationDetailViewPIIMapper) {
    this.classificationDetailViewNoPIIRepository = classificationDetailViewNoPIIRepository;
    this.classificationDetailViewPIIMapper = classificationDetailViewPIIMapper;
  }

  @Override
  public ClassificationDetailViewDTO getClassificationDetailView(Long organizationId, Long classificationId) {
    ClassificationDetailViewNoPII classificationDetailViewNoPII = classificationDetailViewNoPIIRepository.findByOrganizationIdAndClassificationId(organizationId, classificationId);
    if (classificationDetailViewNoPII == null) {
      throw new NotFoundException("[CLASSIFICATION_NOT_FOUND] Classification detail not found for organizationId: " + organizationId + " and classificationId: " + classificationId);
    }
    return classificationDetailViewPIIMapper.map(classificationDetailViewNoPII);
  }
}
