package it.gov.pagopa.pu.classification.mapper.pages;

import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.mapper.pii.view.ClassificationViewPIIMapper;
import it.gov.pagopa.pu.classification.model.view.classification.ClassificationViewNoPII;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class PagedClassificationViewMapper {
  private final ClassificationViewPIIMapper mapper;

  public PagedClassificationViewMapper(ClassificationViewPIIMapper mapper) {
    this.mapper = mapper;
  }

  public PagedClassificationView map2PagedClassificationView(Page<ClassificationViewNoPII> pagedClassificationViewNoPII) {
    PagedClassificationView mappedPagedClassificationView = new PagedClassificationView();

    if(pagedClassificationViewNoPII != null){
      if (!pagedClassificationViewNoPII.getContent().isEmpty()){
        mappedPagedClassificationView.setContent(mapper.mapAll(pagedClassificationViewNoPII.getContent()));
      }else {
        mappedPagedClassificationView.setContent(Collections.emptyList());
      }

      if(pagedClassificationViewNoPII.getPageable().isPaged()){
        mappedPagedClassificationView.setTotalPages((long) pagedClassificationViewNoPII.getTotalPages());
        mappedPagedClassificationView.setSize((long) pagedClassificationViewNoPII.getSize());
        mappedPagedClassificationView.setNumber((long) pagedClassificationViewNoPII.getNumber());
        mappedPagedClassificationView.setTotalElements(pagedClassificationViewNoPII.getTotalElements());
      }
    }
    return mappedPagedClassificationView;
  }
}
