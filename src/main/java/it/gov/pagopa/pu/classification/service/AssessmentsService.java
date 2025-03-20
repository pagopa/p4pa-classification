package it.gov.pagopa.pu.classification.service;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentNoPIIService;
import it.gov.pagopa.pu.classification.connector.processexecutions.IngestionFlowFileService;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPIIResponse;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.IngestionFlowFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Lazy
@Slf4j
@Service
public class AssessmentsService {

  private final InstallmentNoPIIService installmentNoPIIService;
  private final IngestionFlowFileService ingestionFlowFileService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;

  public AssessmentsService(InstallmentNoPIIService installmentNoPIIService, IngestionFlowFileService ingestionFlowFileService, DebtPositionTypeOrgService debtPositionTypeOrgService) {
    this.installmentNoPIIService = installmentNoPIIService;
    this.ingestionFlowFileService = ingestionFlowFileService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
  }

  public List<InstallmentNoPIIResponse> getInstallmentsByReceiptId(Long receiptId, String accessToken) {
    return installmentNoPIIService.getByReceiptId(receiptId, accessToken);
  }

  public Assessments getAssessment(InstallmentNoPIIResponse installmentNoPIIResponse, String accessToken) {
    IngestionFlowFile ingestionFlowFile = ingestionFlowFileService.getIngestionFlowFile(installmentNoPIIResponse.getIngestionFlowFileId(), accessToken);
    String debtPositionTypeOrgCode = debtPositionTypeOrgService.getDebtPositionTypeOrgByInstallmentId(installmentNoPIIResponse.getInstallmentId(), accessToken).getCode();


    return Assessments.builder()
      .organizationId(ingestionFlowFile.getOrganizationId())
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .status(AssessmentStatus.NEW)
      .assessmentName(ingestionFlowFile.getFileName() + "_" + debtPositionTypeOrgCode)
      .build();
  }
}
