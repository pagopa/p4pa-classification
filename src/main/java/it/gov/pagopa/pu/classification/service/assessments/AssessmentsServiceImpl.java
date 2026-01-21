package it.gov.pagopa.pu.classification.service.assessments;

import io.micrometer.common.util.StringUtils;
import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.organization.service.OrganizationService;
import it.gov.pagopa.pu.classification.dto.LocalDateTimeIntervalFilter;
import it.gov.pagopa.pu.classification.dto.generated.PagedAssessmentsView;
import it.gov.pagopa.pu.classification.enums.AssessmentStatus;
import it.gov.pagopa.pu.classification.exception.custom.AssessmentConflictException;
import it.gov.pagopa.pu.classification.exception.custom.NotFoundException;
import it.gov.pagopa.pu.classification.mapper.PagedAssessmentsViewMapper;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.debtposition.dto.generated.DebtPositionTypeOrg;
import it.gov.pagopa.pu.debtposition.dto.generated.InstallmentNoPII;
import it.gov.pagopa.pu.debtposition.dto.generated.ReceiptNoPII;
import it.gov.pagopa.pu.organization.dto.generated.Organization;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AssessmentsServiceImpl implements AssessmentsService {

  private final InstallmentService installmentService;
  private final ReceiptService receiptService;
  private final OrganizationService organizationService;
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
  public AssessmentsServiceImpl(InstallmentService installmentService, ReceiptService receiptService, OrganizationService organizationService, DebtPositionTypeOrgService debtPositionTypeOrgService, AssessmentsRepository assessmentsRepository, AssessmentsDetailService assessmentsDetailService, PagedAssessmentsViewMapper pagedAssessmentsViewMapper) {
    this.installmentService = installmentService;
    this.receiptService = receiptService;
    this.organizationService = organizationService;
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
  public List<Assessments> createAssessment(Long receiptId, String operatorExternalUserId, String accessToken) {
    ReceiptNoPII receipt = receiptService.getById(receiptId, accessToken);
    Organization organization = organizationService.getOrganizationByFiscalCode(receipt.getOrgFiscalCode(), accessToken)
      .orElseThrow(() -> new NotFoundException("[ORGANIZATION_NOT_FOUND] Cannot find organization having fiscal code " + receipt.getOrgFiscalCode()));

    List<InstallmentNoPII> installmentsList = installmentService.getByReceiptId(organization.getOrganizationId(), receipt.getReceiptId(), accessToken);

    return installmentsList.stream()
      .map(installmentNoPII -> {
        assessmentsDetailService.deleteAssessmentDetailsByOrgAndInstallment(
          organization.getOrganizationId(),
          installmentNoPII.getIuv(),
          installmentNoPII.getIud()
        );

        return installmentNoPII;
      })
      .filter(i -> {
        if (i.getBalance() == null) {
          log.info("Balance is null for installmentId: {} and receiptId: {}", i.getInstallmentId(), receiptId);
          return false;
        }
        return true;
      })
      .map(i -> {
        DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.getDebtPositionTypeOrgByInstallmentId(i.getInstallmentId(), accessToken);

        // skipping creation of assessments for technical debtPositionTypes
        if(debtPositionTypeOrg.getDebtPositionTypeId() > 0) {
          Assessments assessment = buildAssessmentFromReceipt(i, operatorExternalUserId, debtPositionTypeOrg);
          assessmentsDetailService.createAssessmentDetail(assessment, receipt, i);
          return assessment;
        } else {
          return null;
        }
      })
      .filter(Objects::nonNull)
      .toList();
  }


  /**
   * Builds an assessment based on the given installment information, operatorExternalUserId and debtPositionTypeOrg.
   *
   * @param installment the installment information
   * @param operatorExternalUserId the external user ID of the operator
   * @param debtPositionTypeOrg the debt position type organization
   * @return the built assessment
   */
  Assessments buildAssessmentFromReceipt(InstallmentNoPII installment, String operatorExternalUserId, DebtPositionTypeOrg debtPositionTypeOrg) {
    String debtPositionTypeOrgCode = debtPositionTypeOrg.getCode();
    Assessments assessment = assessmentsRepository.findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(
      debtPositionTypeOrg.getOrganizationId(), debtPositionTypeOrgCode, installment.getSourceFlowName());

    if (assessment == null) {
      Assessments newAssessment = Assessments.builder()
        .organizationId(debtPositionTypeOrg.getOrganizationId())
        .debtPositionTypeOrgId(debtPositionTypeOrg.getDebtPositionTypeOrgId())
        .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
        .status(AssessmentStatus.CLOSED)
        .assessmentName(installment.getSourceFlowName())
        .operatorExternalUserId(operatorExternalUserId)
        .build();

      assessment = assessmentsRepository.save(newAssessment);
    }

    return assessment;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagedAssessmentsView getPagedAssessmentsView(Long organizationId, String assessmentName, LocalDateTimeIntervalFilter updateDateTimeIntervalFilter, String iuv, List<String> debtPositionTypeOrgCodes, AssessmentStatus status, Pageable pageable) {
    Set<String> setDebtPositionTypeOrgCodes = null;

    if (debtPositionTypeOrgCodes != null && !debtPositionTypeOrgCodes.isEmpty()) {
      setDebtPositionTypeOrgCodes = debtPositionTypeOrgCodes.stream()
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.toSet());
    }

    Page<Assessments> pagedAssessments = assessmentsRepository.findPagedAssessments(organizationId, assessmentName, updateDateTimeIntervalFilter, iuv, setDebtPositionTypeOrgCodes, status, pageable);
    return pagedAssessmentsViewMapper.map(pagedAssessments);
  }

  @Override
  public Assessments createAssessment(Long organizationId, String assessmentName, String debtPositionTypeOrgCode, String operatorExternalUserId, String accessToken) {

    if (assessmentsRepository.findByOrganizationIdAndDebtPositionTypeOrgCodeAndAssessmentName(organizationId, debtPositionTypeOrgCode, assessmentName) != null) {
     throw new AssessmentConflictException("[ASSESSMENT_ALREADY_EXISTS] Assessment with the same name %s and debtPositionTypeOrgCode %s already exists for the current organizationId %d".formatted(assessmentName, debtPositionTypeOrgCode, organizationId));
    }

    DebtPositionTypeOrg debtPositionTypeOrg = debtPositionTypeOrgService.getDebtPositionTypeOrgByDebtPositionTypeOrgCode(organizationId, debtPositionTypeOrgCode, accessToken);

    if (debtPositionTypeOrg == null) {
      throw new ResourceNotFoundException("[DEBT_POSITION_TYPE_ORG_NOT_FOUND] DebtPositionTypeOrg not found by organizationId=%d and debtPositionTypeOrgCode=%s".formatted(organizationId, debtPositionTypeOrgCode));
    }

    return assessmentsRepository.save(
      Assessments.builder()
      .assessmentName(assessmentName)
      .debtPositionTypeOrgId(debtPositionTypeOrg.getDebtPositionTypeOrgId())
      .debtPositionTypeOrgCode(debtPositionTypeOrgCode)
      .flagManualGeneration(true)
      .status(AssessmentStatus.ACTIVE)
      .printed(false)
      .organizationId(organizationId)
      .operatorExternalUserId(operatorExternalUserId)
      .build());
  }

}
