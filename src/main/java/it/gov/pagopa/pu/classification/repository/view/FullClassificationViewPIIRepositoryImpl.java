package it.gov.pagopa.pu.classification.repository.view;

import it.gov.pagopa.pu.classification.dto.ExportClassificationsFilterDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.exception.custom.ExportTooManyRecordsException;
import it.gov.pagopa.pu.classification.mapper.PagedFullClassificationViewMapper;
import it.gov.pagopa.pu.classification.model.view.FullClassificationViewNoPII;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullClassificationViewPIIRepositoryImpl implements FullClassificationViewPIIRepository {
  private final int maxTotalElements;
  private final FullClassificationViewNoPIIDTORepository fullClassificationViewNoPIIDTORepository;
  private final PagedFullClassificationViewMapper pagedFullClassificationViewMapper;

  public FullClassificationViewPIIRepositoryImpl(@Value("${data-export.classification-view.max-total-elements}") int maxTotalElements,
                                                 FullClassificationViewNoPIIDTORepository classificationViewNoPIIDTORepository,
                                                 PagedFullClassificationViewMapper pagedClassificationViewMapper) {
    this.maxTotalElements = maxTotalElements;
    this.fullClassificationViewNoPIIDTORepository = classificationViewNoPIIDTORepository;
    this.pagedFullClassificationViewMapper = pagedClassificationViewMapper;
  }

  @Override
  public PagedFullClassificationView getPagedFullClassificationView(Long organizationId, ExportClassificationsFilterDTO exportClassificationsFilterDTO, List<String> debtPositionTypeOrgCodes, Pageable pageable) {

    Page<FullClassificationViewNoPII> pagedClassificationViewNoPIIDTO = fullClassificationViewNoPIIDTORepository.findFullClassificationViewNoPIIDTO(organizationId, exportClassificationsFilterDTO, debtPositionTypeOrgCodes, pageable);

    if (pagedClassificationViewNoPIIDTO.getTotalElements() > maxTotalElements) {
      throw new ExportTooManyRecordsException("The number of elements returned: %d exceeds the maximum limit of %d".formatted(pagedClassificationViewNoPIIDTO.getTotalElements(), maxTotalElements));
    }

    return pagedFullClassificationViewMapper.map2PagedClassificationView(pagedClassificationViewNoPIIDTO);
  }
}
