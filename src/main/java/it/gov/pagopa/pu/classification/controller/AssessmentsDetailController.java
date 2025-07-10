package it.gov.pagopa.pu.classification.controller;

import it.gov.pagopa.pu.classification.controller.generated.AssessmentsDetailApi;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.service.assessments.AssessmentsDetailService;
import it.gov.pagopa.pu.classification.util.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class AssessmentsDetailController implements AssessmentsDetailApi {
    private final AssessmentsDetailService assessmentsDetailService;

    public AssessmentsDetailController(AssessmentsDetailService assessmentsDetailService) {
        this.assessmentsDetailService = assessmentsDetailService;
    }

    @Override
    public ResponseEntity<List<AssessmentsDetail>> createAssessmentsDetail(Long organizationId, Long assessmentId, CreateAssessmentsDetail createAssessmentsDetail) {
        log.debug("Create AssessmentsDetail having organizationId {} assessmentId {} request body {}",organizationId,assessmentId,createAssessmentsDetail);
        return ResponseEntity.ok(assessmentsDetailService.createAssessmentsDetail(organizationId,assessmentId,createAssessmentsDetail, SecurityUtils.getAccessToken()));
    }
}
