package it.gov.pagopa.pu.classification.repository.view;

import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.exception.custom.ExportTooManyRecordsException;
import it.gov.pagopa.pu.classification.mapper.PagedClassificationViewMapper;
import it.gov.pagopa.pu.classification.model.view.ClassificationViewNoPII;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassificationViewPIIRepositoryImpl implements ClassificationViewPIIRepository {
  private final int maxTotalElements;
  private final ClassificationViewNoPIIDTORepository classificationViewNoPIIDTORepository;
  private final PagedClassificationViewMapper pagedClassificationViewMapper;

  public ClassificationViewPIIRepositoryImpl(@Value("${data-export.classification-view.max-total-elements}") int maxTotalElements,
                                             ClassificationViewNoPIIDTORepository classificationViewNoPIIDTORepository,
                                             PagedClassificationViewMapper pagedClassificationViewMapper) {
    this.maxTotalElements = maxTotalElements;
    this.classificationViewNoPIIDTORepository = classificationViewNoPIIDTORepository;
    this.pagedClassificationViewMapper = pagedClassificationViewMapper;
  }

  @Override
  public PagedClassificationView getPagedClassificationView(Long organizationId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, List<String> debtPositionTypeOrgCodes, Pageable pageable) {

    Page<ClassificationViewNoPII> pagedClassificationViewNoPIIDTO = classificationViewNoPIIDTORepository.findClassificationViewNoPIIDTO(organizationId, exportClassificationsFilterDTO, debtPositionTypeOrgCodes, pageable);

    if (pagedClassificationViewNoPIIDTO.getTotalElements() > maxTotalElements) {
      throw new ExportTooManyRecordsException("The number of elements returned: %d exceeds the maximum limit of %d".formatted(pagedClassificationViewNoPIIDTO.getTotalElements(), maxTotalElements));
    }

    return pagedClassificationViewMapper.map2PagedClassificationView(pagedClassificationViewNoPIIDTO);
  }
}
