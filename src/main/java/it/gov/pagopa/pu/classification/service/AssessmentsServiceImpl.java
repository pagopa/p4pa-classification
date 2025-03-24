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

@Lazy
@Slf4j
@Service
public class AssessmentsServiceImpl implements AssessmentsService {

    private final InstallmentNoPIIService installmentNoPIIService;
    private final IngestionFlowFileService ingestionFlowFileService;
    private final DebtPositionTypeOrgService debtPositionTypeOrgService;
    private final AssessmentsRepository assessmentsRepository;
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * Constructs a new AssessmentsServiceImpl with the given dependencies.
     *
     * @param installmentNoPIIService the service for retrieving installment information
     * @param ingestionFlowFileService the service for retrieving ingestion flow files
     * @param debtPositionTypeOrgService the service for retrieving debt position type organization information
     * @param assessmentsRepository the repository for managing assessments
     */
    public AssessmentsServiceImpl(InstallmentNoPIIService installmentNoPIIService, IngestionFlowFileService ingestionFlowFileService, DebtPositionTypeOrgService debtPositionTypeOrgService, AssessmentsRepository assessmentsRepository) {
        this.installmentNoPIIService = installmentNoPIIService;
        this.ingestionFlowFileService = ingestionFlowFileService;
        this.debtPositionTypeOrgService = debtPositionTypeOrgService;
        this.assessmentsRepository = assessmentsRepository;
    }


    /**
     * {@inheritDoc}
     *
     */
    @Override
    public List<Assessments> createAssesment(Long receiptId, String accessToken) {
        List<InstallmentNoPIIResponse> installmentsList = installmentNoPIIService.getByReceiptId(receiptId, accessToken);

        return installmentsList.stream()
                .map(i -> {
                    Assessments a = this.buildAssessment(i, accessToken);
                    if (assessmentsRepository.getByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(a.getOrganizationId(), a.getDebtPositionTypeOrgCode(), a.getAssessmentName()) == null) {
                        return assessmentsRepository.save(a);
                    }
                    return a;
                })
                .toList();
    }


    /**
     * Builds an assessment based on the given installment information and access token.
     *
     * @param installmentNoPIIResponse the installment information
     * @param accessToken the access token for authentication
     * @return the built assessment
     */
    Assessments buildAssessment(InstallmentNoPIIResponse installmentNoPIIResponse, String accessToken) {
        IngestionFlowFile ingestionFlowFile = ingestionFlowFileService.getIngestionFlowFile(installmentNoPIIResponse.getIngestionFlowFileId(), accessToken);
        DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.getDebtPositionTypeOrgByInstallmentId(installmentNoPIIResponse.getInstallmentId(), accessToken);
        String debtPositionTypeOrgCode = debtPositionTypeOrg.getCode();
        String assessmentName = "";

        if (ingestionFlowFile != null)
            assessmentName = ingestionFlowFile.getFileName().replaceFirst("[.][^.]+$", "") + "_" + debtPositionTypeOrgCode;
        else
            assessmentName = "ACC" + LocalDate.now().format(DATE_TIME_FORMATTER) + "_" + debtPositionTypeOrgCode;

        return Assessments.builder()
                .organizationId(debtPositionTypeOrg.getOrganizationId())
                .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
                .status(AssessmentStatus.NEW)
                .assessmentName(assessmentName)
                .build();
    }

}
