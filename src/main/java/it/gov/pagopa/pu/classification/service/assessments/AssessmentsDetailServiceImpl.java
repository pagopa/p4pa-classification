package it.gov.pagopa.pu.classification.service.assessments;

import it.gov.pagopa.pu.classification.connector.debtposition.DebtPositionTypeOrgBalanceCostService;
import it.gov.pagopa.pu.classification.connector.debtposition.InstallmentService;
import it.gov.pagopa.pu.classification.connector.debtposition.ReceiptService;
import it.gov.pagopa.pu.classification.connector.debtposition.TransferService;
import it.gov.pagopa.pu.classification.dto.BuildAssessmentsDetailParamsDTO;
import it.gov.pagopa.pu.classification.dto.generated.CreateAssessmentsDetail;
import it.gov.pagopa.pu.classification.exception.custom.InvalidRequestBodyException;
import it.gov.pagopa.pu.classification.exception.custom.NotFoundException;
import it.gov.pagopa.pu.classification.model.Assessments;
import it.gov.pagopa.pu.classification.model.AssessmentsDetail;
import it.gov.pagopa.pu.classification.model.AssessmentsRegistry;
import it.gov.pagopa.pu.classification.repository.AssessmentsDetailRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRegistryRepository;
import it.gov.pagopa.pu.classification.repository.AssessmentsRepository;
import it.gov.pagopa.pu.classification.service.BalanceMarshallingService;
import it.gov.pagopa.pu.classification.util.Constants;
import it.gov.pagopa.pu.classification.util.ErrorCodeConstants;
import it.gov.pagopa.pu.classification.util.Utilities;
import it.gov.pagopa.pu.debtposition.dto.generated.*;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtBilancio;
import it.veneto.regione.schemas._2012.pagamenti.ente.CtCapitolo;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Lazy
@Slf4j
@Service
public class AssessmentsDetailServiceImpl implements AssessmentsDetailService {

  private final AssessmentsDetailRepository assessmentsDetailRepository;
  private final BalanceMarshallingService balanceMarshallingService;
  private final AssessmentsRepository assessmentsRepository;
  private final AssessmentsRegistryRepository assessmentsRegistryRepository;
  private final InstallmentService installmentService;
  private final ReceiptService receiptService;
  private final TransferService transferService;
  private final DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostService;


  public AssessmentsDetailServiceImpl(AssessmentsDetailRepository assessmentsDetailRepository, BalanceMarshallingService balanceMarshallingService, AssessmentsRepository assessmentsRepository, AssessmentsRegistryRepository assessmentsRegistryRepository, InstallmentService installmentService, ReceiptService receiptService, TransferService transferService, DebtPositionTypeOrgBalanceCostService debtPositionTypeOrgBalanceCostService) {
    this.assessmentsDetailRepository = assessmentsDetailRepository;
    this.balanceMarshallingService = balanceMarshallingService;
    this.assessmentsRepository = assessmentsRepository;
    this.assessmentsRegistryRepository = assessmentsRegistryRepository;
    this.installmentService = installmentService;
    this.receiptService = receiptService;
    this.transferService = transferService;
    this.debtPositionTypeOrgBalanceCostService = debtPositionTypeOrgBalanceCostService;
  }

  @Transactional
  @Override
  public void createAssessmentDetail(Assessments assessments, ReceiptNoPII receipt, InstallmentNoPII installmentNoPII) {
    List<AssessmentsDetail> assessmentsDetailList = buildAssessmentDetail(receipt, installmentNoPII, assessments);
    assessmentsDetailList.forEach(assessmentDetail -> {
      AssessmentsDetail ad = assessmentsDetailRepository.findByDebtPositionTypeOrgCodeAndIuvAndIudAndOfficeCodeAndSectionCodeAndAssessmentCode(
        assessmentDetail.getDebtPositionTypeOrgCode(), assessmentDetail.getIuv(), assessmentDetail.getIud(),
        assessmentDetail.getOfficeCode(), assessmentDetail.getSectionCode(), assessmentDetail.getAssessmentCode());
      if (ad == null) {
        assessmentsDetailRepository.save(assessmentDetail);
      } else if (!ad.getAmountCents().equals(assessmentDetail.getAmountCents())) {
        ad.setAmountCents(assessmentDetail.getAmountCents());
        assessmentsDetailRepository.save(ad);
      }
    });
  }

  List<AssessmentsDetail> buildAssessmentDetail(ReceiptNoPII receipt, InstallmentNoPII installment, Assessments assessment) {
    CtBilancio balance = balanceMarshallingService.unmarshal(installment.getBalance(), null);

    List<CtCapitolo> capitoloList = balance.getCapitolo();
    return capitoloList.stream()
      .flatMap(capitolo -> capitolo.getAccertamento().stream()
        .map(accertamento ->
                buildAssessmentsDetail(
                  receipt,
                  installment,
                  assessment,
                  capitolo.getCodUfficio(),
                  capitolo.getCodCapitolo(),
                  accertamento.getCodAccertamento(),
                  Utilities.bigDecimalEuroToLongCentsAmount(accertamento.getImporto())
                ))
      ).toList();
  }

  private AssessmentsDetail buildAssessmentsDetail(
    ReceiptNoPII receipt,
    InstallmentNoPII installment,
    Assessments assessment,
    String officeCode,
    String sectionCode,
    String assessmentCode,
    Long amountCents
  ) {
    AssessmentsRegistry assessmentsRegistry = assessmentsRegistryRepository.findByOrganizationIdAndCodes(
      assessment.getOrganizationId(),
      assessment.getDebtPositionTypeOrgCode(),
      sectionCode,
      officeCode,
      assessmentCode,
      String.valueOf(Optional.ofNullable(receipt.getPaymentDateTime()).orElse(OffsetDateTime.now(Constants.ZONEID)).getYear())
    ).orElse(null);

    return this.buildAssessmentsDetail(BuildAssessmentsDetailParamsDTO.builder()
        .receipt(receipt)
        .installment(installment)
        .assessment(assessment)
        .officeCode(officeCode)
        .sectionCode(sectionCode)
        .assessmentCode(assessmentCode)
        .amountCents(amountCents)
        .officeDescription(assessmentsRegistry != null ? assessmentsRegistry.getOfficeDescription() : null)
        .sectionDescription(assessmentsRegistry != null ? assessmentsRegistry.getSectionDescription() : null)
        .assessmentDescription(assessmentsRegistry != null ? assessmentsRegistry.getAssessmentDescription() : null)
        .build());
  }

  private AssessmentsDetail buildAssessmentsDetail(BuildAssessmentsDetailParamsDTO params) {
    return AssessmentsDetail.builder()
            .assessmentId(params.getAssessment().getAssessmentId())
            .organizationId(params.getAssessment().getOrganizationId())
            .debtPositionTypeOrgId(params.getAssessment().getDebtPositionTypeOrgId())
            .debtPositionTypeOrgCode(params.getAssessment().getDebtPositionTypeOrgCode())
            .iuv(params.getInstallment().getIuv())
            .iud(params.getInstallment().getIud())
            .iur(params.getInstallment().getIur())
            .debtorFiscalCodeHash(params.getInstallment().getDebtorFiscalCodeHash())
            .paymentDateTime(params.getReceipt().getPaymentDateTime())
            .officeCode(params.getOfficeCode())
            .officeDescription(params.getOfficeDescription())
            .sectionCode(params.getSectionCode())
            .sectionDescription(params.getSectionDescription())
            .assessmentCode(params.getAssessmentCode())
            .assessmentDescription(params.getAssessmentDescription())
            .amountCents(params.getAmountCents())
            .receiptId(params.getReceipt().getReceiptId())
            .build();
  }

  @Transactional
  @Override
  public List<AssessmentsDetail> createAssessmentsDetail(Long organizationId, Long assessmentId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken) {
    Assessments assessments = assessmentsRepository.findById(assessmentId)
            .filter(a-> organizationId.equals(a.getOrganizationId()))
            .orElseThrow(()->
              new NotFoundException(ErrorCodeConstants.ERROR_CODE_ASSESSMENT_NOT_FOUND, "Assessment with id "+assessmentId+" not found"));
    List<InstallmentNoPII> installments = getInstallmentsByOrganizationIdAndIuds(organizationId, createAssessmentsDetail, accessToken);
    List<AssessmentsDetail> assessmentsDetails = new ArrayList<>();
    for (InstallmentNoPII installment : installments) {
      ReceiptNoPII receipt = getReceiptByReceiptIdAndDebtPositionTypeOrgCode(installment, assessments, accessToken);
      assessmentsDetails.addAll(saveAssessmentsDetail(
        organizationId,
        createAssessmentsDetail,
        installment,
        receipt,
        assessments,
        accessToken
      ));
    }
    return assessmentsDetails;
  }

  @Override
  public void deleteAssessmentDetailsByOrgAndInstallment(Long organizationId, String iuv, String iud) {
    assessmentsDetailRepository.deleteAllByOrganizationIdAndIuvAndIud(organizationId, iuv, iud);
  }

  private ReceiptNoPII getReceiptByReceiptIdAndDebtPositionTypeOrgCode(InstallmentNoPII installment, Assessments assessments, String accessToken) {
    if(installment.getReceiptId()==null){
      throw new InvalidRequestBodyException(ErrorCodeConstants.ERROR_CODE_INVALID_INSTALLMENT, "Installment having iud "+ installment.getIud()+" does not have a receiptId");
    }
    ReceiptNoPII receipt = receiptService.getByReceiptIdAndDebtPositionTypeOrgCode(installment.getReceiptId(), assessments.getDebtPositionTypeOrgCode(), accessToken);
    if(receipt==null) {
      throw new NotFoundException(ErrorCodeConstants.ERROR_CODE_RECEIPT_NOT_FOUND, "Receipt with id " + installment.getReceiptId() + " not found");
    }
    return receipt;
  }

  private List<InstallmentNoPII> getInstallmentsByOrganizationIdAndIuds(Long organizationId, CreateAssessmentsDetail createAssessmentsDetail, String accessToken) {
    List<InstallmentNoPII> installments = installmentService.findByOrganizationIdAndIuds(organizationId, createAssessmentsDetail.getIuds(), accessToken);
    if(!CollectionUtils.isEmpty(createAssessmentsDetail.getIuds())
            && (CollectionUtils.isEmpty(installments) || createAssessmentsDetail.getIuds().size()!=installments.size())){
      throw new InvalidRequestBodyException(ErrorCodeConstants.ERROR_CODE_INVALID_IUD, "One or more iud is invalid. [organizationId: "+ organizationId +" iuds:"+ createAssessmentsDetail.getIuds()+"]");
    }
    return installments;
  }

  private List<AssessmentsDetail> saveAssessmentsDetail(Long organizationId, CreateAssessmentsDetail createAssessmentsDetail, InstallmentNoPII installment, ReceiptNoPII receipt, Assessments assessments, String accessToken) {
    AssessmentsRegistry assessmentsRegistry = assessmentsRegistryRepository.findById(createAssessmentsDetail.getAssessmentRegistryId())
            .filter(ar-> organizationId.equals(ar.getOrganizationId()))
            .orElseThrow(
            ()->new NotFoundException(ErrorCodeConstants.ERROR_CODE_ASSESSMENT_REGISTRY_NOT_FOUND, "AssessmentRegistry with id "+createAssessmentsDetail.getAssessmentRegistryId()+" not found")
    );
    List<AssessmentsDetail> assessmentsDetails = new ArrayList<>();
    Long amountCents = getAssessmentDetailAmount(installment.getInstallmentId(), receipt.getOrgFiscalCode(), accessToken);
    if (installment.getNotificationFeeCents() != null && installment.getNotificationFeeCents() > 0) {
      amountCents = Math.max(amountCents -  installment.getNotificationFeeCents(),0L);
      assessmentsDetails.add(saveNotificationFeeAssessmentDetail(installment, receipt, assessments, assessmentsRegistry.getOperatingYear(), accessToken));
    }
    assessmentsDetails.add(assessmentsDetailRepository.save(
            buildAssessmentsDetail(
              BuildAssessmentsDetailParamsDTO.builder()
                .receipt(receipt)
                .installment(installment)
                .assessment(assessments)
                .officeCode(assessmentsRegistry.getOfficeCode())
                .sectionCode(assessmentsRegistry.getSectionCode())
                .assessmentCode(assessmentsRegistry.getAssessmentCode())
                .amountCents(amountCents)
                .officeDescription(assessmentsRegistry.getOfficeDescription())
                .sectionDescription(assessmentsRegistry.getSectionDescription())
                .assessmentDescription(assessmentsRegistry.getAssessmentDescription())
                .build()
            ))
    );
    return assessmentsDetails;
  }

  private Long getAssessmentDetailAmount(Long installmentId, String orgFiscalCode, String accessToken){
    List<Transfer> transfers = transferService.getByInstallmentId(installmentId, accessToken);
    return transfers.stream().filter(t->orgFiscalCode.equals(t.getOrgFiscalCode())).map(Transfer::getAmountCents).reduce(0L,Long::sum);
  }

  private AssessmentsDetail saveNotificationFeeAssessmentDetail(InstallmentNoPII installment, ReceiptNoPII receipt, Assessments assessments, String operatingYear, String accessToken) {
    BuildAssessmentsDetailParamsDTO.BuildAssessmentsDetailParamsDTOBuilder<?, ?> builder = BuildAssessmentsDetailParamsDTO.builder()
      .receipt(receipt)
      .installment(installment)
      .assessment(assessments)
      .amountCents(installment.getNotificationFeeCents());
    DebtPositionTypeOrgBalanceCost dptoBalanceCost = debtPositionTypeOrgBalanceCostService.getDptoBalanceCostByInstallmentIdAndTypeAndOperatingYear(installment.getInstallmentId(),
          DebtPositionTypeOrgBalanceCostType.NOTIFICATION_COST, operatingYear, accessToken);
    if (dptoBalanceCost != null) {
      builder
        .officeCode(dptoBalanceCost.getOfficeCode())
        .sectionCode(dptoBalanceCost.getSectionCode())
        .assessmentCode(dptoBalanceCost.getAssessmentCode())
        .officeDescription(dptoBalanceCost.getOfficeDescription())
        .sectionDescription(dptoBalanceCost.getSectionDescription())
        .assessmentDescription(dptoBalanceCost.getAssessmentDescription());
    }else{
      builder
        .officeCode(Constants.DEFAULT_SEND_DPTOBC_CODE)
        .sectionCode(Constants.DEFAULT_SEND_DPTOBC_CODE)
        .assessmentCode(Constants.DEFAULT_SEND_DPTOBC_CODE);
    }
    return assessmentsDetailRepository.save(
      buildAssessmentsDetail(
        builder
          .build()
      ));
  }
}
