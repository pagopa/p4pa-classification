package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentNoPIIService;
import it.gov.pagopa.pu.classification.connector.processexecutions.IngestionFlowFileService;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.IngestionFlowFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Lazy
@Slf4j
@Service
public class AssessmentsServiceImpl implements AssessmentsService {

    private final InstallmentNoPIIService installmentNoPIIService;
    private final IngestionFlowFileService ingestionFlowFileService;
    private final DebtPositionTypeOrgService debtPositionTypeOrgService;
    private final AssessmentsRepository assessmentsRepository;
    public static final String CURRENT_DATE_STRING = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

    public AssessmentsServiceImpl(InstallmentNoPIIService installmentNoPIIService, IngestionFlowFileService ingestionFlowFileService, DebtPositionTypeOrgService debtPositionTypeOrgService, AssessmentsRepository assessmentsRepository) {
        this.installmentNoPIIService = installmentNoPIIService;
        this.ingestionFlowFileService = ingestionFlowFileService;
        this.debtPositionTypeOrgService = debtPositionTypeOrgService;
        this.assessmentsRepository = assessmentsRepository;
    }

    @Override
    public List<Assessments> createAssesment(Long receiptId, String accessToken) {
        List<InstallmentNoPIIResponse> installmentsList = installmentNoPIIService.getByReceiptId(receiptId, accessToken);
        List<Assessments> assessmentsList = installmentsList.stream()
                .map(i -> {
                    Assessments a = this.buildAssessment(i, accessToken);
                    if (assessmentsRepository.getByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(a.getOrganizationId(), a.getDebtPositionTypeOrgCode(), a.getAssessmentName()) == null) {
                        return a;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();

        return assessmentsRepository.saveAll(assessmentsList);
    }

    Assessments buildAssessment(InstallmentNoPIIResponse installmentNoPIIResponse, String accessToken) {
        IngestionFlowFile ingestionFlowFile = ingestionFlowFileService.getIngestionFlowFile(installmentNoPIIResponse.getIngestionFlowFileId(), accessToken);
        DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.getDebtPositionTypeOrgByInstallmentId(installmentNoPIIResponse.getInstallmentId(), accessToken);
        String debtPositionTypeOrgCode = debtPositionTypeOrg.getCode();
        String assessmentName = "";

        if (ingestionFlowFile != null)
            assessmentName = ingestionFlowFile.getFileName() + "_" + debtPositionTypeOrgCode;
        else
            assessmentName = "ACC" + CURRENT_DATE_STRING + "_" + debtPositionTypeOrgCode;

        return Assessments.builder()
                .organizationId(debtPositionTypeOrg.getOrganizationId())
                .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
                .status(AssessmentStatus.NEW)
                .assessmentName(assessmentName)
                .build();
    }

}
