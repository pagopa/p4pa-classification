package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.generated.PagedTreasuredClassification;
import it.gov.pagopa.pu.classification.model.view.TreasuredClassificationView;
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class TreasuredClassificationMapper {

  public PagedTreasuredClassification map2PagedTreasuredClassification(
    Page<TreasuredClassificationView> pagedClassificationListDTO) {
    PagedTreasuredClassification mappedPagedClassificationList = new PagedTreasuredClassification();

    if (pagedClassificationListDTO != null) {
      if (!pagedClassificationListDTO.getContent().isEmpty()) {
        mappedPagedClassificationList.setContent(pagedClassificationListDTO.getContent());
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
