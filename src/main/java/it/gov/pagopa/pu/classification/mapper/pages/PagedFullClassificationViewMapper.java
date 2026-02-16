package it.gov.pagopa.pu.classification.mapper.pages;

import it.gov.pagopa.pu.classification.dto.generated.PagedFullClassificationView;
import it.gov.pagopa.pu.classification.mapper.pii.view.FullClassificationViewPIIMapper;
import it.gov.pagopa.pu.classification.model.view.classification.FullClassificationViewNoPII;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class PagedFullClassificationViewMapper {
  private final FullClassificationViewPIIMapper fullClassificationViewPIIMapper;

  public PagedFullClassificationViewMapper(FullClassificationViewPIIMapper fullClassificationViewPIIMapper) {
    this.fullClassificationViewPIIMapper = fullClassificationViewPIIMapper;
  }

  public PagedFullClassificationView map2PagedClassificationView(Page<FullClassificationViewNoPII> pagedFullClassificationViewNoPII) {
    PagedFullClassificationView mappedPagedClassificationView = new PagedFullClassificationView();

    if(pagedFullClassificationViewNoPII != null){
      if (!pagedFullClassificationViewNoPII.getContent().isEmpty()){
        mappedPagedClassificationView.setContent(pagedFullClassificationViewNoPII.stream()
          .map(fullClassificationViewPIIMapper::map)
          .toList());
      }else {
        mappedPagedClassificationView.setContent(Collections.emptyList());
      }

      if(pagedFullClassificationViewNoPII.getPageable().isPaged()){
        mappedPagedClassificationView.setTotalPages((long) pagedFullClassificationViewNoPII.getTotalPages());
        mappedPagedClassificationView.setSize((long) pagedFullClassificationViewNoPII.getSize());
        mappedPagedClassificationView.setNumber((long) pagedFullClassificationViewNoPII.getNumber());
        mappedPagedClassificationView.setTotalElements(pagedFullClassificationViewNoPII.getTotalElements());
      }
    }
    return mappedPagedClassificationView;
  }
}
