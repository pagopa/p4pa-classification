package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.ClassificationListDTO;
import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationListDTO;
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class PagedClassificationListMapper {

  public PagedClassificationListMapper() {
  }

  public PagedClassificationListDTO map2PagedClassificationListDTO(
    Page<ClassificationListDTO> pagedClassificationListDTO) {
    PagedClassificationListDTO mappedPagedClassificationList = new PagedClassificationListDTO();

    if (pagedClassificationListDTO != null) {
      if (!pagedClassificationListDTO.getContent().isEmpty()) {
        mappedPagedClassificationList.setContent(
          pagedClassificationListDTO.getContent());
      } else {
        mappedPagedClassificationList.setContent(Collections.emptyList());
      }

      if (pagedClassificationListDTO.getPageable().isPaged()) {
        mappedPagedClassificationList.setTotalPages(
          (long) pagedClassificationListDTO.getTotalPages());
        mappedPagedClassificationList.setSize(
          (long) pagedClassificationListDTO.getSize());
        mappedPagedClassificationList.setNumber(
          (long) pagedClassificationListDTO.getNumber());
        mappedPagedClassificationList.setTotalElements(
          pagedClassificationListDTO.getTotalElements());
      }
    }

    return mappedPagedClassificationList;
  }
}
