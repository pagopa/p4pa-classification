package it.gov.pagopa.pu.classification.service.assessments;

import io.micrometer.common.util.StringUtils;
import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.exception.custom.InvalidNameException;
import it.gov.pagopa.pu.classification.mapper.PagedAssessmentsViewMapper;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AssessmentsServiceImpl implements AssessmentsService {

  private final InstallmentService installmentService;
  private final ReceiptService receiptService;
  private final DebtPositionTypeOrgService debtPositionTypeOrgService;
  private final AssessmentsRepository assessmentsRepository;
  private final AssessmentsDetailService assessmentsDetailService;
  public final PagedAssessmentsViewMapper pagedAssessmentsViewMapper;

  /**
   * Constructs a new AssessmentsServiceImpl with the given dependencies.
   *
   * @param installmentService         the service for retrieving installment information
   * @param debtPositionTypeOrgService the service for retrieving debt position type organization information
   * @param assessmentsRepository      the repository for managing assessments
   */
  public AssessmentsServiceImpl(InstallmentService installmentService, ReceiptService receiptService, DebtPositionTypeOrgService debtPositionTypeOrgService, AssessmentsRepository assessmentsRepository, AssessmentsDetailService assessmentsDetailService, PagedAssessmentsViewMapper pagedAssessmentsViewMapper) {
    this.installmentService = installmentService;
    this.receiptService = receiptService;
    this.debtPositionTypeOrgService = debtPositionTypeOrgService;
    this.assessmentsRepository = assessmentsRepository;
    this.assessmentsDetailService = assessmentsDetailService;
    this.pagedAssessmentsViewMapper = pagedAssessmentsViewMapper;
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
    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.getDebtPositionTypeOrgByInstallmentId(installment.getInstallmentId(), accessToken);
    String debtPositionTypeOrgCode = debtPositionTypeOrg.getCode();

    return Assessments.builder()
      .organizationId(debtPositionTypeOrg.getOrganizationId())
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .status(AssessmentStatus.NEW)
      .assessmentName(installment.getSourceFlowName())
      .build();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagedAssessmentsView getPagedAssessmentsView(String assessmentName, LocalDateTimeIntervalFilter updateDateTimeIntervalFilter, String iuv, List<String> debtPositionTypeOrgCodes, AssessmentStatus status, Pageable pageable) {
    Set<String> setDebtPositionTypeOrgCodes = null;

    if (debtPositionTypeOrgCodes != null && !debtPositionTypeOrgCodes.isEmpty()) {
      setDebtPositionTypeOrgCodes = debtPositionTypeOrgCodes.stream()
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.toSet());
    }

    Page<Assessments> pagedAssessments = assessmentsRepository.findPagedAssessments(assessmentName, updateDateTimeIntervalFilter, iuv, setDebtPositionTypeOrgCodes, status, pageable);
    return pagedAssessmentsViewMapper.map(pagedAssessments);
  }

  @Override
  public Assessments createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode) {

    if (assessmentsRepository.findByAssessmentName(assessmentName) != null) {
     throw new InvalidNameException("Assessment with the same name already exists");
    }

    return assessmentsRepository.save(
      Assessments.builder()
      .assessmentName(assessmentName)
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .flagManualGeneration(true)
      .status(AssessmentStatus.ACTIVE)
      .printed(false)
      .organizationId(organizationId)
      .build());
  }

}
