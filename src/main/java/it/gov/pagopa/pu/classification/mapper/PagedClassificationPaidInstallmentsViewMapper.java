package it.gov.pagopa.pu.classification.mapper;

import it.gov.pagopa.pu.classification.dto.generated.PagedClassificationPaidInstallmentsView;
import it.gov.pagopa.pu.classification.model.view.ClassificationPaidInstallmentsView;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class PagedClassificationPaidInstallmentsViewMapper {

  public PagedClassificationPaidInstallmentsView map(Page<ClassificationPaidInstallmentsView> paidInstallmentsPage) {
    PagedClassificationPaidInstallmentsView mappedPagedPaidInstallmentsView = new PagedClassificationPaidInstallmentsView();

    if (paidInstallmentsPage != null) {
      if (!paidInstallmentsPage.getContent().isEmpty()) {
        mappedPagedPaidInstallmentsView.setContent(paidInstallmentsPage.getContent());
      } else {
        mappedPagedPaidInstallmentsView.setContent(Collections.emptyList());
      }

      if (paidInstallmentsPage.getPageable().isPaged()) {
        mappedPagedPaidInstallmentsView.setTotalPages((long) paidInstallmentsPage.getTotalPages());
        mappedPagedPaidInstallmentsView.setSize((long) paidInstallmentsPage.getSize());
        mappedPagedPaidInstallmentsView.setNumber((long) paidInstallmentsPage.getNumber());
        mappedPagedPaidInstallmentsView.setTotalElements(paidInstallmentsPage.getTotalElements());
      }
    }
    return mappedPagedPaidInstallmentsView;
  }
}
