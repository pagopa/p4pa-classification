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

  public AssessmentsServiceImpl(InstallmentNoPIIService installmentNoPIIService, IngestionFlowFileService ingestionFlowFileService, DebtPositionTypeOrgService debtPositionTypeOrgService, AssessmentsRepository assessmentsRepository) {
    this.installmentNoPIIService = installmentNoPIIService;
    this.ingestionFlowFileService = ingestionFlowFileService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.assessmentsRepository = assessmentsRepository;
  }

  @Override
  public List<InstallmentNoPIIResponse> getInstallmentsByReceiptId(Long receiptId, String accessToken) {
    return installmentNoPIIService.getByReceiptId(receiptId, accessToken);
  }

  @Override
  public List<Assessments> createAssesment(Long receiptId, String accessToken) {
    List<InstallmentNoPIIResponse> installmentsList = this.getInstallmentsByReceiptId(receiptId, accessToken);
    List<Assessments> assessmentsList = installmentsList.stream()
      .map(i ->
        this.buildAssessment(i, accessToken))
      .toList();

    return assessmentsRepository.saveAll(assessmentsList);
  }

  Assessments buildAssessment(InstallmentNoPIIResponse installmentNoPIIResponse, String accessToken) {
    IngestionFlowFile ingestionFlowFile = ingestionFlowFileService.getIngestionFlowFile(installmentNoPIIResponse.getIngestionFlowFileId(), accessToken);
    DebtPositionTypeOrg debtPositionTypeOrg =debtPositionTypeOrgService.getDebtPositionTypeOrgByInstallmentId(installmentNoPIIResponse.getInstallmentId(), accessToken);
    String debtPositionTypeOrgCode = debtPositionTypeOrg.getCode();
    String assessmentName = "ACC" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "_" + debtPositionTypeOrgCode;

    if (ingestionFlowFile != null) {
      assessmentName = ingestionFlowFile.getFileName() + "_" + debtPositionTypeOrgCode;
    }

    return Assessments.builder()
      .organizationId(debtPositionTypeOrg.getOrganizationId())
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .status(AssessmentStatus.NEW)
      .assessmentName(assessmentName)
      .build();
  }

}
