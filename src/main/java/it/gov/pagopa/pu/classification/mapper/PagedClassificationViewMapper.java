package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationView;
import it.gov.pagopa.pu.classification.model.view.ClassificationViewNoPII;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class PagedClassificationViewMapper {
  private final ClassificationViewPIIMapper classificationViewMapper;

  public PagedClassificationViewMapper(ClassificationViewPIIMapper classificationViewMapper) {
    this.classificationViewMapper = classificationViewMapper;
  }

  public PagedClassificationView map2PagedClassificationView(Page<ClassificationViewNoPII> pagedClassificationViewNoPII) {
    PagedClassificationView mappedPagedClassificationView = new PagedClassificationView();

    if(pagedClassificationViewNoPII != null){
      if (!pagedClassificationViewNoPII.getContent().isEmpty()){
        mappedPagedClassificationView.setContent(pagedClassificationViewNoPII.stream().map(classificationViewMapper::map).toList());
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
