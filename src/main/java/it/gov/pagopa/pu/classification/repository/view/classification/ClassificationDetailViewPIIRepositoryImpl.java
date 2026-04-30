package it.gov.pagopa.pu.classification.repository.view.classification;

import it.gov.pagopa.pu.classification.dto.ClassificationDetailViewDTO;
import it.gov.pagopa.pu.classification.exception.custom.NotFoundException;
import it.gov.pagopa.pu.classification.mapper.pii.view.ClassificationDetailViewPIIMapper;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationDetailViewNoPII;
import it.gov.pagopa.pu.classification.util.ErrorCodeConstants;
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
      throw new NotFoundException(ErrorCodeConstants.ERROR_CODE_CLASSIFICATION_NOT_FOUND, "Classification detail not found for organizationId: " + organizationId + " and classificationId: " + classificationId);
    }
    return classificationDetailViewPIIMapper.map(classificationDetailViewNoPII);
  }
}
