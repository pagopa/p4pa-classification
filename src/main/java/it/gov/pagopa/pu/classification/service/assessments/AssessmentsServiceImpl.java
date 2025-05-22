package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.processexecutions.IngestionFlowFileService;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import it.gov.pagopa.pu.p4paprocessexecutions.dto.generated.IngestionFlowFile;
import jakarta.transaction.Transactional;
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

  private final InstallmentService installmentService;
  private final ReceiptService receiptService;
  private final IngestionFlowFileService ingestionFlowFileService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final AssessmentsRepository assessmentsRepository;
  private final AssessmentsDetailService assessmentsDetailService;
  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

  /**
   * Constructs a new AssessmentsServiceImpl with the given dependencies.
   *
   * @param installmentService         the service for retrieving installment information
   * @param ingestionFlowFileService   the service for retrieving ingestion flow files
   * @param debtPositionTypeOrgService the service for retrieving debt position type organization information
   * @param assessmentsRepository      the repository for managing assessments
   */
  public AssessmentsServiceImpl(InstallmentService installmentService, ReceiptService receiptService, IngestionFlowFileService ingestionFlowFileService, DebtPositionTypeOrgService debtPositionTypeOrgService, AssessmentsRepository assessmentsRepository, AssessmentsDetailService assessmentsDetailService) {
    this.installmentService = installmentService;
    this.receiptService = receiptService;
    this.ingestionFlowFileService = ingestionFlowFileService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.assessmentsRepository = assessmentsRepository;
    this.assessmentsDetailService = assessmentsDetailService;
  }


  /**
   * {@inheritDoc}
   */
  @Transactional
  @Override
  public List<Assessments> createAssesment(Long receiptId, String accessToken) {
    ReceiptNoPII receipt = receiptService.getById(receiptId, accessToken);
    List<InstallmentNoPII> installmentsList = installmentService.getByReceiptId(receiptId, accessToken);

    return installmentsList.stream()
      .filter(i -> {
        if (i.getBalance() == null) {
          log.info("Balance is null for installmentId: {} and receiptId: {}", i.getInstallmentId(), receiptId);
          return false;
        }
        return true;
      })
      .map(i -> {
        Assessments assessment = buildAssessment(i, accessToken);
        if (assessmentsRepository.findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(
          assessment.getOrganizationId(), assessment.getDebtPositionTypeOrgCode(), assessment.getAssessmentName()) == null) {
          assessment = assessmentsRepository.save(assessment);
        }
        assessmentsDetailService.createAssessmentDetail(assessment, receipt, i);
        return assessment;
      })
      .toList();
  }


  /**
   * Builds an assessment based on the given installment information and access token.
   *
   * @param installment the installment information
   * @param accessToken the access token for authentication
   * @return the built assessment
   */
  Assessments buildAssessment(InstallmentNoPII installment, String accessToken) {
    IngestionFlowFile ingestionFlowFile = installment.getIngestionFlowFileId() != null
      ? ingestionFlowFileService.getIngestionFlowFile(installment.getIngestionFlowFileId(), accessToken)
      : null;
    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.getDebtPositionTypeOrgByInstallmentId(installment.getInstallmentId(), accessToken);
    String debtPositionTypeOrgCode = debtPositionTypeOrg.getCode();
    String assessmentName;

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
