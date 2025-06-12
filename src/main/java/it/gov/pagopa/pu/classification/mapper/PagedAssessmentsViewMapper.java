package it.gov.pagopa.pu.classification.mapper;


import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.model.Assessments;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class PagedAssessmentsViewMapper {

  public PagedAssessmentsView map(Page<Assessments> assessmentsPage){
    PagedAssessmentsView mappedPagedAssessmentsView = new PagedAssessmentsView();

    if(assessmentsPage != null){
      if (!assessmentsPage.getContent().isEmpty()){
        mappedPagedAssessmentsView.setContent(assessmentsPage.stream().toList());
      }else {
        mappedPagedAssessmentsView.setContent(Collections.emptyList());
      }

      if( assessmentsPage.getPageable().isPaged()){
        mappedPagedAssessmentsView.setTotalPages((long) assessmentsPage.getTotalPages());
        mappedPagedAssessmentsView.setSize((long) assessmentsPage.getSize());
        mappedPagedAssessmentsView.setNumber(assessmentsPage.getNumber());
        mappedPagedAssessmentsView.setTotalElements(assessmentsPage.getTotalElements());
      }
    }
    return mappedPagedAssessmentsView;
  }
}
